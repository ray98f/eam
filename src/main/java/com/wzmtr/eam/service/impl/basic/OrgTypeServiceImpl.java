package com.wzmtr.eam.service.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.OrgTypeReqDTO;
import com.wzmtr.eam.dto.res.OrgTypeResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.basic.OrgTypeMapper;
import com.wzmtr.eam.service.basic.OrgTypeService;
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
public class OrgTypeServiceImpl implements OrgTypeService {

    private static final Map<String, String> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("10", "点巡检/检修专用");
        TYPE_MAP.put("20", "故障维修专用");
        TYPE_MAP.put("30", "管理部门");
    }

    @Autowired
    private OrgTypeMapper orgTypeMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<OrgTypeResDTO> listOrgType(String orgCode, String orgType, PageReqDTO pageReqDTO) {
        List<String> orgCodes = new ArrayList<>();
        if (orgCode != null && !"".equals(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return orgTypeMapper.pageOrgType(pageReqDTO.of(), StringUtils.getSumArrayList(orgCodes), orgType);
    }

    @Override
    public OrgTypeResDTO getOrgTypeDetail(String id) {
        return orgTypeMapper.getOrgTypeDetail(id);
    }

    @Override
    public void addOrgType(OrgTypeReqDTO orgTypeReqDTO) {
        Integer result = orgTypeMapper.selectOrgTypeIsExist(orgTypeReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        orgTypeReqDTO.setRecId(TokenUtil.getUuId());
        orgTypeReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        orgTypeReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        orgTypeMapper.addOrgType(orgTypeReqDTO);
    }

    @Override
    public void modifyOrgType(OrgTypeReqDTO orgTypeReqDTO) {
        Integer result = orgTypeMapper.selectOrgTypeIsExist(orgTypeReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        orgTypeReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        orgTypeReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        orgTypeMapper.modifyOrgType(orgTypeReqDTO);
    }

    @Override
    public void deleteOrgType(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            orgTypeMapper.deleteOrgType(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        }
    }

    @Override
    public void exportOrgType(String orgCode, String orgType, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "组织机构代码", "组织机构名称", "类别", "记录状态", "备注", "创建者", "创建时间");
        List<String> orgCodes = new ArrayList<>();
        if (orgCode != null && !"".equals(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        List<OrgTypeResDTO> orgTypeList = orgTypeMapper.listOrgType(StringUtils.getSumArrayList(orgCodes), orgType);
        List<Map<String, String>> list = new ArrayList<>();
        if (orgTypeList != null && !orgTypeList.isEmpty()) {
            for (OrgTypeResDTO orgTypeRes : orgTypeList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", orgTypeRes.getRecId());
                map.put("组织机构代码", orgTypeRes.getOrgCode());
                map.put("组织机构名称", orgTypeRes.getOrgName());
                map.put("类别", TYPE_MAP.get(orgTypeRes.getOrgType()));
                map.put("记录状态", "10".equals(orgTypeRes.getRecStatus()) ? "启用" : "禁用");
                map.put("备注", orgTypeRes.getRemark());
                map.put("创建者", orgTypeRes.getRecCreator());
                map.put("创建时间", orgTypeRes.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("组织机构类别信息", listName, list, null, response);
    }

}
