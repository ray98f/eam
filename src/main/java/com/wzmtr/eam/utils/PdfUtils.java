package com.wzmtr.eam.utils;

import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

/**
 * 生成pdf文件工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/12
 */
@Slf4j
public class PdfUtils {
    /**
     * 存放字体资源文件的地址
     */
    private final static String FONT_BASE_PATH = "C:\\Users\\40302\\Desktop\\";
    /**
     * 默认字体资源文件（[宋体][simsun.ttc]）
     */
    private final static String DEFAULT_FONT = "simsun.ttc";
    /**
     * 指定编码
     */
    private final static String ENCODING = "UTF-8";
    /**
     * 列表模板名称
     */
    public final static String PDF_DEMO_LIST_TEMPLATE = "rectification-two";

    /**
     * 生成pdf
     * @param templateCode  模板
     * @param data  传入到freemarker模板里的数据
     * @param out   生成的pdf文件流
     */
    public static void createPdf(String templateCode, Map<String, Object> data, OutputStream out) {
        try {
            // 创建一个FreeMarker实例, 负责管理FreeMarker模板的Configuration实例
            Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            // 指定FreeMarker模板文件的位置
            cfg.setClassForTemplateLoading(DocUtils.class, "/");
            ITextRenderer renderer = new ITextRenderer();
            // 设置 css中 的字体样式（暂时仅支持宋体和黑体）
            renderer.getFontResolver().addFont(FONT_BASE_PATH + DEFAULT_FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            // 设置模板的编码格式
            cfg.setEncoding(Locale.CHINA, ENCODING);
            // 获取模板文件 template.ftl
            Template template = cfg.getTemplate("templates/" + templateCode + ".ftl");
            StringWriter writer = new StringWriter();
            // 将数据输出到html中
            template.process(data, writer);
            writer.flush();
            String html = writer.toString();
            // 把html代码传入渲染器中
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(out, false);
            renderer.finishPDF();
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("PDF导出异常", e);
        }
    }

    public static void drawPdf(String templateCode, Map<String, Object> data, OutputStream out) {

    }

}
