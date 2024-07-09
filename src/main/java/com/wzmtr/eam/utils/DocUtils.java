package com.wzmtr.eam.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/**
 * 文档工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/12
 */
@Slf4j
public class DocUtils {

    public static final String WIDTH = "width";

    public static MultipartFile saveWord(String name, String type, Map<String,Object> dataMap) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(DocUtils.class, "/");
        Template template = configuration.getTemplate("templates/" + type + ".xml");
        InputStreamSource streamSource = createWord(template, dataMap);
        InputStream inputStream = Objects.requireNonNull(streamSource).getInputStream();
        return new MockMultipartFile(name, name, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", inputStream);
    }

    public static InputStreamSource createWord(Template template, Map<String, Object> dataMap) {
        StringWriter out = null;
        Writer writer = null;
        try {
            out = new StringWriter();
            writer = new BufferedWriter(out, 1024);
            template.process(dataMap, writer);
            return new ByteArrayResource(out.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("exception message", e);
        } finally {
            try {
                Objects.requireNonNull(writer).close();
                out.close();
            } catch (IOException e) {
                log.error("exception message", e);
            }
        }
        return null;
    }

    public static InputStream returnBitMap(String path) {
        URL url = null;
        InputStream is =null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            log.error("exception message", e);
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) Objects.requireNonNull(url).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.76");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
        } catch (IOException e) {
            log.error("exception message", e);
        }
        return is;
    }


    /**
     * 将doc格式文件转成html
     * @param docPath  doc文件路径
     * @param imageDir doc文件中图片存储目录
     * @return html
     */
    public static String doc2Html(String docPath, final String imageDir) {
        String content = null;
        ByteArrayOutputStream baos = null;
        try {
            HWPFDocument wordDocument = new HWPFDocument(Files.newInputStream(Paths.get(docPath)));
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            if (com.wzmtr.eam.utils.StringUtils.isNotEmpty(imageDir)) {
                wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                    @Override
                    public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                        File file = new File(imageDir + suggestedName);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            fos.write(content);
                        } catch (IOException e) {
                            log.error("exception message", e);
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (Exception e) {
                                log.error("exception message", e);
                            }
                        }
                        return imageDir + suggestedName;
                    }
                });
            }
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            baos = new ByteArrayOutputStream();
            StreamResult streamResult = new StreamResult(baos);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
        } catch (Exception e) {
            log.error("exception message", e);
        } finally {
            try {
                if (baos != null) {
                    content = baos.toString("utf-8");
                    baos.close();
                }
            } catch (Exception e) {
                log.error("exception message", e);
            }
        }
        return content;
    }

    /**
     * 将docx格式文件转成html
     * @param docxPath docx文件路径
     * @param imageDir docx文件中图片存储目录
     * @return html
     */
    public static String docx2Html(String docxPath, String imageDir) {
        String content = null;

        InputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            // 1> 加载文档到XWPFDocument
            in = returnBitMap(docxPath);
            XWPFDocument document = new XWPFDocument(in);
            // 2> 解析XHTML配置（这里设置IURIResolver来设置图片存放的目录）
            XHTMLOptions options = XHTMLOptions.create();
            // 存放word中图片的目录
            if (com.wzmtr.eam.utils.StringUtils.isNotEmpty(imageDir)) {
                options.setExtractor(new FileImageExtractor(new File(imageDir)));
                options.URIResolver(new BasicURIResolver(imageDir));
                options.setIgnoreStylesIfUnused(false);
                options.setFragment(true);
            }
            // 3> 将XWPFDocument转换成XHTML
            baos = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, baos, options);
        } catch (Exception e) {
            log.error("exception message", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (baos != null) {
                    content = baos.toString("utf-8");
                    baos.close();
                }
            } catch (Exception e) {
                log.error("exception message", e);
            }
        }
        return content;
    }

    /**
     * 使用jsoup规范化html
     * @param html html内容
     * @return 规范化后的html
     */
    public static String formatHtml(String html) {
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        // 去除过大的宽度
        String style = doc.attr("style");
        if (StringUtils.isNotEmpty(style) && style.contains(WIDTH)) {
            doc.attr("style", "");
        }
        Elements divs = doc.select("div");
        for (Element div : divs) {
            String divStyle = div.attr("style");
            if (StringUtils.isNotEmpty(divStyle) && divStyle.contains(WIDTH)) {
                div.attr("style", "");
            }
        }
        // jsoup生成闭合标签
        doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return doc.html();
    }


    /**
     * html转成pdf
     * @param html          html
     * @param outputPdfPath 输出pdf路径
     */
    private static void htmlToPdf(String html, String outputPdfPath) {
        com.itextpdf.text.Document document = null;
        try {
            // 纸
            document = new com.itextpdf.text.Document(PageSize.A4);
            // 笔
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(outputPdfPath)));
            document.open();
            // html转pdf
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()),
                    StandardCharsets.UTF_8, new FontProvider() {
                        @Override
                        public boolean isRegistered(String s) {
                            return false;
                        }

                        @Override
                        public Font getFont(String s, String s1, boolean embedded, float size, int style, BaseColor baseColor) {
                            // 配置字体
                            Font font = null;
                            try {
                                BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                                font = new Font(bf, size, style, baseColor);
                                font.setColor(baseColor);
                            } catch (Exception e) {
                                log.error("exception message", e);
                            }
                            return font;
                        }
                    });
        } catch (Exception e) {
            log.error("exception message", e);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String basePath = "C:/Users/40302/Desktop/";
        String docPath = basePath + "222.doc";
        String docxPath = "http://zxgd.softether.net:9000/rectification-one/2022/11/10/dbc31e65-c8d3-48c3-b753-8a264b4395bb.docx";
        String pdfPath = basePath + "222.pdf";
        String imageDir = "C:/Users/40302/Desktop/image/";

        // 测试doc转pdf
        String docHtml = doc2Html(docPath, imageDir);
        docHtml = formatHtml(docHtml);
        System.out.println(docHtml);
        htmlToPdf(docHtml, pdfPath);

//        // 测试docx转pdf
//        String docxHtml = docx2Html(docxPath, imageDir);
//        docxHtml = formatHtml(docxHtml);
//        System.out.println(docxHtml);
//        docxHtml = docxHtml.replace("___", "22");
//        htmlToPdf(docxHtml, pdfPath);

    }

}