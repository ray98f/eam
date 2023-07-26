package com.wzmtr.eam.service.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.OrgMajorReqDTO;
import com.wzmtr.eam.dto.res.OrgMajorResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.service.basic.OrgMajorService;
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
public class OrgMajorServiceImpl implements OrgMajorService {

    private static final Map<String, String> MAJOR_MAP = new HashMap<>();

    static {
        MAJOR_MAP.put("01", "PSD系统");
        MAJOR_MAP.put("02", "电扶梯系统");
        MAJOR_MAP.put("03", "给排水系统");
        MAJOR_MAP.put("04", "通风空调系统");
        MAJOR_MAP.put("05", "低压供电系统");
        MAJOR_MAP.put("06", "车辆工艺设备");
        MAJOR_MAP.put("07", "车辆");
        MAJOR_MAP.put("08", "通信");
        MAJOR_MAP.put("09", "信号");
        MAJOR_MAP.put("10", "综合监控");
        MAJOR_MAP.put("11", "AFC系统");
        MAJOR_MAP.put("12", "接触网系统");
        MAJOR_MAP.put("13", "供电系统");
        MAJOR_MAP.put("14", "轨道");
        MAJOR_MAP.put("15", "人防门");
        MAJOR_MAP.put("17", "工程车");
        MAJOR_MAP.put("70", "土建");
    }

    @Autowired
    private OrgMajorMapper orgMajorMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<OrgMajorResDTO> listOrgMajor(String orgCode, String majorCode, PageReqDTO pageReqDTO) {
        List<String> orgCodes = new ArrayList<>();
        if (orgCode != null && !"".equals(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return orgMajorMapper.pageOrgMajor(pageReqDTO.of(), StringUtils.getSumArrayList(orgCodes), majorCode);
    }

    @Override
    public OrgMajorResDTO getOrgMajorDetail(String id) {
        return orgMajorMapper.getOrgMajorDetail(id);
    }

    @Override
    public void addOrgMajor(OrgMajorReqDTO orgMajorReqDTO) {
        Integer result = orgMajorMapper.selectOrgMajorIsExist(orgMajorReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        orgMajorReqDTO.setRecId(TokenUtil.getUuId());
        orgMajorReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        orgMajorReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        orgMajorMapper.addOrgMajor(orgMajorReqDTO);
    }

    @Override
    public void modifyOrgMajor(OrgMajorReqDTO orgMajorReqDTO) {
        Integer result = orgMajorMapper.selectOrgMajorIsExist(orgMajorReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        orgMajorReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        orgMajorReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        orgMajorMapper.modifyOrgMajor(orgMajorReqDTO);
    }

    @Override
    public void deleteOrgMajor(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            orgMajorMapper.deleteOrgMajor(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportOrgMajor(String orgCode, String majorCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "组织机构代码", "组织机构名称", "专业", "记录状态", "备注", "创建者", "创建时间");
        List<String> orgCodes = new ArrayList<>();
        if (orgCode != null && !"".equals(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        List<OrgMajorResDTO> orgMajors = orgMajorMapper.listOrgMajor(StringUtils.getSumArrayList(orgCodes), majorCode);
        List<Map<String, String>> list = new ArrayList<>();
        if (orgMajors != null && !orgMajors.isEmpty()) {
            for (OrgMajorResDTO orgMajor : orgMajors) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", orgMajor.getRecId());
                map.put("组织机构代码", orgMajor.getOrgCode());
                map.put("组织机构名称", orgMajor.getOrgName());
                map.put("专业", MAJOR_MAP.get(orgMajor.getMajorCode()));
                map.put("记录状态", "10".equals(orgMajor.getRecStatus()) ? "启用" : "禁用");
                map.put("备注", orgMajor.getRemark());
                map.put("创建者", orgMajor.getRecCreator());
                map.put("创建时间", orgMajor.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("组织机构专业信息", listName, list, null, response);
    }

}
