package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.OrgLineReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgLineResDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelOrgLineResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.OrgLineMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.service.basic.OrgLineService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (StringUtils.isNotEmpty(orgCode)) {
            orgCodes = organizationMapper.downRecursion(orgCode);
        }
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return orgLineMapper.pageOrgLine(pageReqDTO.of(), StringUtils.getSumArrayList(orgCodes), lineCode);
    }

    @Override
    public OrgLineResDTO getOrgLineDetail(String id) {
        return orgLineMapper.getOrgLineDetail(id);
    }

    @Override
    public void addOrgLine(OrgLineReqDTO orgLineReqDTO) {
        String orgId = organizationMapper.getIdByAreaId(orgLineReqDTO.getOrgCode());
        if (StringUtils.isNotEmpty(orgId)) {
            List<String> ids = organizationMapper.downRecursionId(orgId);
            if (StringUtils.isNotEmpty(ids)) {
                List<SysOffice> offices = organizationMapper.getAreaIdsByIds(ids);
                orgLineReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
                orgLineReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
                for (SysOffice office : offices) {
                    orgLineReqDTO.setOrgCode(office.getAreaId());
                    orgLineReqDTO.setOrgName(office.getName());
                    Integer result = orgLineMapper.selectOrgLineIsExist(orgLineReqDTO);
                    if (result > CommonConstants.ONE) {
                        continue;
                    }
                    orgLineReqDTO.setRecId(TokenUtils.getUuId());
                    orgLineMapper.addOrgLine(orgLineReqDTO);
                }
            }
        }
    }

    @Override
    public void modifyOrgLine(OrgLineReqDTO orgLineReqDTO) {
        addOrgLine(orgLineReqDTO);
    }

    @Override
    public void deleteOrgLine(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            orgLineMapper.deleteOrgLine(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportOrgLine(List<String> ids, HttpServletResponse response) throws IOException {
        List<OrgLineResDTO> orgLines = orgLineMapper.listOrgLine(ids);
        if (orgLines != null && !orgLines.isEmpty()) {
            List<ExcelOrgLineResDTO> resList = new ArrayList<>();
            for (OrgLineResDTO resDTO : orgLines) {
                ExcelOrgLineResDTO res = ExcelOrgLineResDTO.builder()
                        .recId(resDTO.getRecId())
                        .orgCode(resDTO.getOrgCode())
                        .orgName(resDTO.getOrgName())
                        .lineCode(LINE_MAP.get(resDTO.getLineCode()))
                        .recStatus(CommonConstants.TEN_STRING.equals(resDTO.getRecStatus()) ? "启用" : "禁用")
                        .remark(resDTO.getRemark())
                        .recCreator(resDTO.getRecCreator())
                        .recCreateTime(resDTO.getRecCreateTime())
                        .build();
                resList.add(res);
            }
            EasyExcelUtils.export(response, "组织机构线别信息", resList);
        }
    }

}
