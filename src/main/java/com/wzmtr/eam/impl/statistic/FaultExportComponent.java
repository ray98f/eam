package com.wzmtr.eam.impl.statistic;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Maps;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.fault.FaultQueryDetailReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.DynamicSource;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.utils.ExcelTemplateUtils;
import com.wzmtr.eam.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: Li.Wang
 * Date: 2023/9/27 16:59
 */
@Component
@Slf4j
public class FaultExportComponent {
    @Autowired
    private FaultQueryMapper faultQueryMapper;

    public void exportByTemplate(FaultQueryDetailReqDTO reqDTO, HttpServletResponse response) {
        if (StringUtils.isEmpty(reqDTO.getFillinTimeStart()) || StringUtils.isEmpty(reqDTO.getFillinTimeEnd())) {
            reqDTO.setFillinTimeStart(getNowWeek(1));
            reqDTO.setFillinTimeEnd(getNowWeek(2));
        }
        List<FaultDetailResDTO> data;
        // 根据导出类型导出对应数据
        if (Objects.isNull(reqDTO.getExportType())) {
            data = faultQueryMapper.listExcludeZtt(reqDTO);
        } else {
            data = faultQueryMapper.listZtt(reqDTO);
        }
        if (StringUtils.isEmpty(data)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "该时间段内未查询到数据！");
        }
        try {
            Calendar calendar = Calendar.getInstance();
            Map<String, String> staticSource = new HashMap<>();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTimeInMillis(System.currentTimeMillis());
            // 获取当前时间为本年的第几周
            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
            staticSource.put("week", String.valueOf(weekOfYear));
            staticSource.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
            List<Map<String, String>> dataList = getDataList(data);
            // 模板自定义的head
            List<String> list = Arrays.asList("a", "list1");
            List<String> optimizedList = new ArrayList<>(list);
            List<DynamicSource> dynamicSourceList = DynamicSource.createList(optimizedList, dataList);
            // 从resources下加载模板并替换
            InputStream resourceAsStream = getClass().getResourceAsStream("/excel_template/faulttemplate.xlsx");
            // 遍历所有的sheet
            Workbook workbook = ExcelTemplateUtils.buildByTemplate(resourceAsStream, staticSource, dynamicSourceList);
            // 2.保存到本地
            ExcelTemplateUtils.save(workbook, Objects.isNull(reqDTO.getExportType()) ? "故障列表" : "中铁通故障列表", response);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    /**
     * 根据类型获取本周开始结束时间
     * @param type 1 开始时间 2 结束时间
     * @return 本周开始结束时间
     */
    private String getNowWeek(int type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        while (calendar.get(Calendar.DAY_OF_WEEK) != firstDayOfWeek) {
            calendar.add(Calendar.DATE, -1);
        }
        if (type == CommonConstants.ONE) {
            // 将日期调整到本周第一天（星期一）
            calendar.add(Calendar.DATE, 1);
            String startTime = sdf.format(calendar.getTime());
            return startTime.split(CommonConstants.BLANK)[0] + " 00:00:00";
        } else {
            // 将日期调整到下一周最后一天（星期天）
            calendar.add(Calendar.DATE, 7);
            String endTime = sdf.format(calendar.getTime());
            return endTime.split(CommonConstants.BLANK)[0] + " 23:59:59";
        }
    }

    public void faultExportWithTemplateUseEasyExcel(FaultQueryDetailReqDTO reqDTO, HttpServletResponse response) {
        List<FaultDetailResDTO> data = faultQueryMapper.list(reqDTO);
        InputStream resourceAsStream = getClass().getResourceAsStream("/excel_template/faulttemplate.xlsx");
        Map<String, String> map = buildSingleMap();
        Map<String, List<?>> sheetAndDataMap = buildSheetAndDataMap(data);
        ExcelTemplateUtils.fillReportWithEasyExcel(response, sheetAndDataMap, map, "test.xls", resourceAsStream);
    }

    private Map<String, List<?>> buildSheetAndDataMap(List<FaultDetailResDTO> data) {
        Map<String, List<?>> dataMap = Maps.newHashMap();
        dataMap.put("0", data);
        return dataMap;
    }

    private Map<String, String> buildSingleMap() {
        Calendar calendar = Calendar.getInstance();
        Map<String, String> map = Maps.newHashMap();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 获取当前时间为本年的第几周
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        map.put("week", String.valueOf(weekOfYear));
        map.put("year", String.valueOf(calendar.get(Calendar.YEAR)));
        return map;
    }

    @NotNull
    private static List<Map<String, String>> getDataList(List<FaultDetailResDTO> data) {
        List<Map<String, String>> dataList = new ArrayList<>();
        for (FaultDetailResDTO detailResDTO : data) {
            Map<String, String> rowMap = new HashMap<>();
            rowMap.put("faultNo", Optional.ofNullable(detailResDTO.getFaultNo()).orElse(CommonConstants.EMPTY));
            rowMap.put("objectName", Optional.ofNullable(detailResDTO.getObjectName()).orElse(CommonConstants.EMPTY));
            rowMap.put("partName", Optional.ofNullable(detailResDTO.getPartName()).orElse(CommonConstants.EMPTY));
            rowMap.put("faultWorkNo", Optional.ofNullable(detailResDTO.getFaultWorkNo()).orElse(CommonConstants.EMPTY));
            rowMap.put("objectCode", Optional.ofNullable(detailResDTO.getObjectCode()).orElse(CommonConstants.EMPTY));
            rowMap.put("faultStatus", Optional.ofNullable(detailResDTO.getFaultStatus()).orElse(CommonConstants.EMPTY));
            rowMap.put("repairDeptName", Optional.ofNullable(detailResDTO.getRepairDeptName()).orElse(CommonConstants.EMPTY));
            rowMap.put("fillinDeptName", Optional.ofNullable(detailResDTO.getFillinDeptName()).orElse(CommonConstants.EMPTY));
            rowMap.put("fillinUserName", Optional.ofNullable(detailResDTO.getFillinUserName()).orElse(CommonConstants.EMPTY));
            rowMap.put("discovererPhone", Optional.ofNullable(detailResDTO.getDiscovererPhone()).orElse(CommonConstants.EMPTY));
            rowMap.put("fillinTime", Optional.ofNullable(detailResDTO.getFillinTime()).orElse(CommonConstants.EMPTY));
            rowMap.put("discovererName", Optional.ofNullable(detailResDTO.getDiscovererName()).orElse(CommonConstants.EMPTY));
            rowMap.put("discoveryTime", Optional.ofNullable(detailResDTO.getDiscoveryTime()).orElse(CommonConstants.EMPTY));
            rowMap.put("levelFault", Optional.ofNullable(detailResDTO.getFaultLevel()).orElse(CommonConstants.EMPTY));
            rowMap.put("faultAffect", Optional.ofNullable(detailResDTO.getFaultAffect()).orElse(CommonConstants.EMPTY));
            rowMap.put("lineCode", Optional.ofNullable(detailResDTO.getLineCode()).orElse(CommonConstants.EMPTY));
            rowMap.put("trainTrunk", Optional.ofNullable(detailResDTO.getTrainTrunk()).orElse(CommonConstants.EMPTY));
            rowMap.put("positionName", Optional.ofNullable(detailResDTO.getPositionName()).orElse(CommonConstants.EMPTY));
            rowMap.put("position2Name", Optional.ofNullable(detailResDTO.getPosition2Name()).orElse(CommonConstants.EMPTY));
            rowMap.put("majorName", Optional.ofNullable(detailResDTO.getMajorName()).orElse(CommonConstants.EMPTY));
            rowMap.put("systemName", Optional.ofNullable(detailResDTO.getSystemName()).orElse(CommonConstants.EMPTY));
            rowMap.put("equipTypeName", Optional.ofNullable(detailResDTO.getEquipTypeName()).orElse(CommonConstants.EMPTY));
            rowMap.put("faultModule", Optional.ofNullable(detailResDTO.getFaultModule()).orElse(CommonConstants.EMPTY));
            rowMap.put("replacementName", Optional.ofNullable(detailResDTO.getFaultModule()).orElse(CommonConstants.EMPTY));
            rowMap.put("oldRepNo", Optional.ofNullable(detailResDTO.getFaultModule()).orElse(CommonConstants.EMPTY));
            rowMap.put("newRepNo", Optional.ofNullable(detailResDTO.getFaultModule()).orElse(CommonConstants.EMPTY));
            rowMap.put("faultDisplayDetail", Optional.ofNullable(detailResDTO.getFaultDisplayDetail()).orElse(CommonConstants.EMPTY));
            rowMap.put("faultDetail", Optional.ofNullable(detailResDTO.getFaultDetail()).orElse(CommonConstants.EMPTY));
            rowMap.put("operateCostTime", Optional.ofNullable(detailResDTO.getFaultModule()).orElse(CommonConstants.EMPTY));
            rowMap.put("dealerUnit", Optional.ofNullable(detailResDTO.getDealerUnit()).orElse(CommonConstants.EMPTY));
            rowMap.put("dealerNum", Optional.ofNullable(detailResDTO.getDealerNum()).orElse(CommonConstants.EMPTY));
            dataList.add(rowMap);
        }
        return dataList;
    }
}
