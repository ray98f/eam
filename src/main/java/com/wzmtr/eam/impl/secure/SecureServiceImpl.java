package com.wzmtr.eam.impl.secure;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.secure.SecureCheckAddReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckDetailReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordDeleteReqDTO;
import com.wzmtr.eam.dto.req.secure.SecureCheckRecordListReqDTO;
import com.wzmtr.eam.dto.res.secure.SecureCheckRecordListResDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.secure.SecureMapper;
import com.wzmtr.eam.service.secure.SecureService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Author: Li.Wang
 * Date: 2023/8/1 10:37
 */
@Service
@Slf4j
public class SecureServiceImpl implements SecureService {

    @Autowired
    private SecureMapper secureMapper;

    @Override
    public Page<SecureCheckRecordListResDTO> list(SecureCheckRecordListReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<SecureCheckRecordListResDTO> list = secureMapper.query(reqDTO.of());
        if (null != list) {
            return list;
        }
        return new Page<>();
    }

    @Override
    public SecureCheckRecordListResDTO detail(SecureCheckDetailReqDTO reqDTO) {
        if (null == reqDTO.getSecRiskId()) {
            return null;
        }
        return secureMapper.detail(reqDTO.getSecRiskId());
    }

    @Override
    public void export(String secRiskId, String inspectDate, String restoreDesc, String workFlowInstStatus, String riskRank, HttpServletResponse response) {
        List<String> listName = Arrays.asList("检查问题单号", "发现日期", "检查问题", "检查部门", "检查人", "地点", "整改措施",
                "计划完成日期", "整改部门", "整改情况", "记录状态");
        List<SecureCheckRecordListResDTO> list = secureMapper.list(secRiskId, restoreDesc, riskRank, inspectDate, workFlowInstStatus);
        List<Map<String, String>> exportList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (SecureCheckRecordListResDTO res : list) {
                Map<String, String> map = new HashMap<>();
                map.put("检查问题单号", res.getSecRiskId());
                map.put("发现日期", res.getInspectDate());
                map.put("检查问题", res.getSecRiskDetail());
                map.put("检查部门", res.getInspectDeptCode());
                map.put("检查人", res.getInspectorCode());
                map.put("地点", res.getPositionDesc());
                map.put("整改措施", res.getRestoreDetail());
                map.put("计划完成日期", res.getPlanDate());
                map.put("整改部门", res.getRestoreDeptCode());
                map.put("整改情况", res.getRestoreDesc());
                map.put("记录状态", res.getRecStatus());
                exportList.add(map);
            }
        }
        ExcelPortUtil.excelPort("安全/质量/消防整改信息", listName, exportList, null, response);
    }

    @Override
    public void delete(SecureCheckRecordDeleteReqDTO reqDTO) {
        if (reqDTO.getIds() != null && !reqDTO.getIds().isEmpty()) {
            secureMapper.deleteByIds(reqDTO.getIds());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void add(SecureCheckAddReqDTO reqDTO) {
        reqDTO.setRecId(TokenUtil.getUuId());
        reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        secureMapper.add(reqDTO);
    }
}
