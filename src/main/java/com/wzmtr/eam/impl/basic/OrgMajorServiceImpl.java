package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.OrgMajorReqDTO;
import com.wzmtr.eam.dto.res.basic.FaultResDTO;
import com.wzmtr.eam.dto.res.basic.FaultRespAndRepairDeptResDTO;
import com.wzmtr.eam.dto.res.basic.OrgMajorResDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelOrgMajorResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.FaultMapper;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.service.basic.OrgMajorService;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private FaultMapper faultMapper;

    @Override
    public Page<OrgMajorResDTO> listOrgMajor(String orgCode, String majorCode, PageReqDTO pageReqDTO) {
        List<String> orgCodes = new ArrayList<>();
        if (StringUtils.isNotEmpty(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return orgMajorMapper.pageOrgMajor(pageReqDTO.of(), StringUtils.getSumArrayList(orgCodes), majorCode);
    }

    @Override
    public List<OrgMajorResDTO> listUseOrgMajor(String majorCode) {
        return orgMajorMapper.listUseOrgMajor(majorCode);
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
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            orgMajorMapper.deleteOrgMajor(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportOrgMajor(String orgCode, String majorCode, HttpServletResponse response) throws IOException {
        List<String> orgCodes = new ArrayList<>();
        if (StringUtils.isNotEmpty(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        List<OrgMajorResDTO> orgMajors = orgMajorMapper.listOrgMajor(StringUtils.getSumArrayList(orgCodes), majorCode);
        if (orgMajors != null && !orgMajors.isEmpty()) {
            List<ExcelOrgMajorResDTO> resList = new ArrayList<>();
            for (OrgMajorResDTO resDTO : orgMajors) {
                ExcelOrgMajorResDTO res = ExcelOrgMajorResDTO.builder()
                        .recId(resDTO.getRecId())
                        .orgCode(resDTO.getOrgCode())
                        .orgName(resDTO.getOrgName())
                        .majorCode(MAJOR_MAP.get(resDTO.getMajorCode()))
                        .recStatus(CommonConstants.TEN_STRING.equals(resDTO.getRecStatus()) ? "启用" : "禁用")
                        .remark(resDTO.getRemark())
                        .recCreator(resDTO.getRecCreator())
                        .recCreateTime(resDTO.getRecCreateTime())
                        .build();
                resList.add(res);
            }
            EasyExcelUtils.export(response, "组织机构专业信息", resList);
        }
    }

    @Override
    public FaultRespAndRepairDeptResDTO queryTypeAndDeptCode(String lineCode, String majorCode) {
        FaultRespAndRepairDeptResDTO faultRespAndRepairDeptResDTO = new FaultRespAndRepairDeptResDTO();
        String organType = "30";
        // List<FaultResDTO> list = getEquipTypeByMajorCode(majorCode, lineCode);
        List<OrgMajorResDTO> respDept = orgMajorMapper.queryTypeAndDeptCode(organType, majorCode, lineCode);
        faultRespAndRepairDeptResDTO.setResp(respDept);
        organType = "20";
        List<OrgMajorResDTO> repairDept = orgMajorMapper.queryTypeAndDeptCode(organType, majorCode, lineCode);
        faultRespAndRepairDeptResDTO.setRepair(repairDept);
        return faultRespAndRepairDeptResDTO;
    }


    private List<FaultResDTO> getEquipTypeByMajorCode(String majorCode, String lineCode) {
        return faultMapper.listFault("E", null, lineCode, null);
    }

}
