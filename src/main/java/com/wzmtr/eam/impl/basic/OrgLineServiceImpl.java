package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.basic.OrgLineReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgLineResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.basic.OrgLineMapper;
import com.wzmtr.eam.service.basic.OrgLineService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class OrgLineServiceImpl implements OrgLineService {

    private static final Map<String, String> LINE_MAP = new HashMap<>();

    static {
        LINE_MAP.put("01", "S1线");
        LINE_MAP.put("02", "S2线");
    }

    @Autowired
    private OrgLineMapper orgLineMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<OrgLineResDTO> listOrgLine(String orgCode, String lineCode, PageReqDTO pageReqDTO) {
        List<String> orgCodes = new ArrayList<>();
        if (orgCode != null && !"".equals(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return orgLineMapper.pageOrgLine(pageReqDTO.of(), StringUtils.getSumArrayList(orgCodes), lineCode);
    }

    @Override
    public OrgLineResDTO getOrgLineDetail(String id) {
        return orgLineMapper.getOrgLineDetail(id);
    }

    @Override
    public void addOrgLine(OrgLineReqDTO orgLineReqDTO) {
        Integer result = orgLineMapper.selectOrgLineIsExist(orgLineReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        orgLineReqDTO.setRecId(TokenUtil.getUuId());
        orgLineReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        orgLineReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        orgLineMapper.addOrgLine(orgLineReqDTO);
    }

    @Override
    public void modifyOrgLine(OrgLineReqDTO orgLineReqDTO) {
        Integer result = orgLineMapper.selectOrgLineIsExist(orgLineReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        orgLineReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        orgLineReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        orgLineMapper.modifyOrgLine(orgLineReqDTO);
    }

    @Override
    public void deleteOrgLine(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            orgLineMapper.deleteOrgLine(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportOrgLine(String orgCode, String lineCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "组织机构代码", "组织机构名称", "线路", "记录状态", "备注", "创建者", "创建时间");
        List<String> orgCodes = new ArrayList<>();
        if (orgCode != null && !"".equals(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        List<OrgLineResDTO> orgLines = orgLineMapper.listOrgLine(StringUtils.getSumArrayList(orgCodes), lineCode);
        List<Map<String, String>> list = new ArrayList<>();
        if (orgLines != null && !orgLines.isEmpty()) {
            for (OrgLineResDTO orgLine : orgLines) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", orgLine.getRecId());
                map.put("组织机构代码", orgLine.getOrgCode());
                map.put("组织机构名称", orgLine.getOrgName());
                map.put("线路", LINE_MAP.get(orgLine.getLineCode()));
                map.put("记录状态", "10".equals(orgLine.getRecStatus()) ? "启用" : "禁用");
                map.put("备注", orgLine.getRemark());
                map.put("创建者", orgLine.getRecCreator());
                map.put("创建时间", orgLine.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("组织机构线别信息", listName, list, null, response);
    }

}
