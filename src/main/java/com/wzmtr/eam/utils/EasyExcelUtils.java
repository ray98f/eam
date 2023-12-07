package com.wzmtr.eam.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.wzmtr.eam.config.CustomCellWriteHeightConfig;
import com.wzmtr.eam.config.CustomCellWriteWidthConfig;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * EasyExcel导入导出工具类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/06
 */
public class EasyExcelUtils {

    /**
     * 设置excel样式
     */
    public static HorizontalCellStyleStrategy getStyleStrategy() {

        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        // 头字号
        headWriteFont.setFontHeightInPoints((short) 12);
        // 字体样式
        headWriteFont.setFontName("宋体");
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 自动换行
        headWriteCellStyle.setWrapped(true);
        // 设置细边框
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);
        // 设置边框颜色 25灰度
        headWriteCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headWriteCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headWriteCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headWriteCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        // 水平对齐方式
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 垂直对齐方式
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        WriteCellStyle contentStyle = new WriteCellStyle();
        // 设置垂直居中
        contentStyle.setWrapped(true);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        WriteFont contentWriteFont = new WriteFont();
        // 内容字号
        contentWriteFont.setFontHeightInPoints((short) 12);
        // 字体样式
        contentWriteFont.setFontName("宋体");
        contentStyle.setWriteFont(contentWriteFont);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentStyle);
    }

    /**
     * 导出设置响应信息
     *
     * @param response 响应
     * @param fileName 文件名称
     * @throws UnsupportedEncodingException
     */
    public static void setResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        // 文件名
        String sheetName = URLEncoder.encode(fileName, "UTF-8") + ".xlsx";
        // contentType 响应内容的类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 设置字符
        response.setCharacterEncoding("utf-8");
        // 设置文件名
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + sheetName);
    }

    /**
     * 普通导出
     *
     * @param response 响应
     * @param name     名称
     * @param list     数据集合
     * @throws IOException
     */
    public static void export(HttpServletResponse response, String name, List<?> list) throws IOException {
        if (!list.isEmpty()) {
            setResponse(response, name);
            OutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, list.get(0).getClass())
                    .sheet(name)
                    .registerWriteHandler(new CustomCellWriteWidthConfig())
                    .registerWriteHandler(new CustomCellWriteHeightConfig())
                    .registerWriteHandler(EasyExcelUtils.getStyleStrategy())
                    .doWrite(list);
        }
    }
}