package com.wzmtr.eam.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.wzmtr.eam.config.CustomCellWriteHeightConfig;
import com.wzmtr.eam.config.CustomCellWriteWidthConfig;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

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

    /**
     /**
     * 文件导入数据读取
     * @param file 文件
     * @param head 头部
     * @return 列表
     */
    public static <T> List<T> read(MultipartFile file, Class<T> head) {
        checkFileFormat(file);
        try {
            return checkData(EasyExcel.read(file.getInputStream(), head, null).doReadAllSync());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件导入数据读取(多sheet)
     * @param file 文件
     * @param head 头部
     * @param sheetNo sheet编号
     * @return 列表
     */
    public static <T> List<T> read(MultipartFile file, Class<T> head, Integer sheetNo) {
        checkFileFormat(file);
        try {
            return checkData(EasyExcel.read(file.getInputStream(), head, null).sheet(sheetNo).doReadSync());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 导入文件格式校验
     * @param file 文件
     */
    private static void checkFileFormat(MultipartFile file) {
        boolean fileBool = Objects.isNull(file.getOriginalFilename()) || (!Objects.isNull(file.getOriginalFilename())
                && !file.getOriginalFilename().endsWith(CommonConstants.XLSX) && !file.getOriginalFilename().endsWith(CommonConstants.XLS));
        if (fileBool) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "文件格式有误，请检查上传文件格式!");
        }
    }

    /**
     * 导入数据校验
     * @param list 导入数据
     * @return 导入数据
     * @param <T> 泛型
     */
    private static <T> List<T> checkData(List<T> list) {
        if (StringUtils.isEmpty(list)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "导入空模板，请填写数据后导入");
        }
        boolean bool = true;
        for (T t : list) {
            bool = allFieldsNull(t);
            if (!bool) {
                break;
            }
        }
        if (bool) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "导入模板错误，请检查模板");
        }
        return list;
    }

    /**
     * 判断对象元素是否都为null
     * @param obj 对象
     * @return 是否都为null
     */
    private static boolean allFieldsNull(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(obj) != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}