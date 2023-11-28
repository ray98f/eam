package com.wzmtr.eam.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Author: Li.Wang
 * Date: 2023/11/27 16:32
 */
public class EasyExcelUtil {
    /**
     *
     * EasyExcel 填充报表
     *
     * @param response
     * @param list  填充集合
     * @param map     填充单个的值
     * @param sheetNo   填充到哪个Sheet页 Index of sheet, 0 base.
     * @param filename   文件名
     * @param inputStream   文件流.
     */
    // public void fillReportWithEasyExcel(HttpServletResponse response, Integer sheetNo, List<?> list, Map<String, String> map, String filename, InputStream inputStream){
    //     ExcelWriter excelWriter = null;
    //     try {
    //         OutputStream outputStream = response.getOutputStream();
    //         response.setHeader("Content-disposition", "attachment; filename=" + filename);
    //         response.setContentType("application/msexcel;charset=UTF-8");//设置类型
    //         response.setHeader("Pragma", "No-cache");//设置头
    //         response.setHeader("Cache-Control", "no-cache");//设置头
    //         response.setDateHeader("Expires", 0);//设置日期头
    //         excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();
    //         WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo).build();
    //         FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
    //         excelWriter.fill(list, fillConfig, writeSheet);
    //         excelWriter.fill(map, writeSheet);
    //         excelWriter.writeContext().writeWorkbookHolder().getWorkbook().setForceFormulaRecalculation(true);
    //     }catch (Exception e){
    //         e.printStackTrace();
    //     }finally {
    //         if (excelWriter != null) {
    //             excelWriter.close();
    //         }
    //         try {
    //             inputStream.close();
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    /**
     *
     * EasyExcel 填充报表
     *
     * @param response
     * @param sheetAndDataMap  key:sheet页，value:填充的list集合
     * @param map     填充单个的值
     * @param filename   文件名
     * @param inputStream   文件流.
     */
    public static void fillReportWithEasyExcel(HttpServletResponse response, Map<String, List<?>> sheetAndDataMap,
                                               Map<String, String> map, String filename, InputStream inputStream){
        ExcelWriter excelWriter = null;
        try {
            OutputStream outputStream = response.getOutputStream();
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.setContentType("application/vnd.ms-excel");//设置类型
            response.setHeader("Pragma", "No-cache");//设置头
            response.setHeader("Cache-Control", "no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();
            for(Map.Entry<String, List<?>> entry : sheetAndDataMap.entrySet()){
                List<?> value = entry.getValue();
                WriteSheet writeSheet = EasyExcel.writerSheet(Integer.valueOf(entry.getKey())).build();
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                excelWriter.fill(value, fillConfig, writeSheet);
                excelWriter.fill(map, writeSheet);
                excelWriter.writeContext().writeWorkbookHolder().getWorkbook().setForceFormulaRecalculation(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
