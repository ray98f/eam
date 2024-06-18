package com.wzmtr.eam.utils.convert;

import com.aspose.words.SaveFormat;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.utils.FileUploadUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.IURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Word 转 Html 工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/18
 */
@Slf4j
public class Word2HtmlUtils {

    /**
     * `word` 转 `html`
     * @param wordBytes: word字节码
     * @return html文件字节码数据
     */
    @SneakyThrows(Exception.class)
    public static byte[] wordBytes2HtmlBytes(byte[] wordBytes) {
        // 创建临时word转html后生成的html文件
        String tmpHtmlFilePath =
                CommonConstants.DEFAULT_FOLDER_TMP_GENERATE + "/" + System.currentTimeMillis() + "-" + getUuid32() + ".html";
        com.aspose.words.Document doc = new com.aspose.words.Document(new ByteArrayInputStream(wordBytes));
        doc.save(tmpHtmlFilePath, SaveFormat.HTML);
        byte[] htmlBytes = FileUploadUtils.readBytes(tmpHtmlFilePath);
        // 删除临时word文件
        FileUploadUtils.deleteFileOrFolder(tmpHtmlFilePath);
        return htmlBytes;
    }

    /**
     * `word` 转 `html`
     * @param wordBytes:    word字节码
     * @param htmlFilePath: html文件路径
     * @return html文件数据
     */
    @SneakyThrows(Exception.class)
    public static File wordBytes2HtmlFile(byte[] wordBytes, String htmlFilePath) {
        // Load word document from disk.
        com.aspose.words.Document doc = new com.aspose.words.Document(new ByteArrayInputStream(wordBytes));
        // Save the document into MHTML.
        doc.save(htmlFilePath, SaveFormat.HTML);
        return new File(htmlFilePath);
    }

    /**
     * 获取32位的uuid
     * @return java.lang.String
     */
    private static String getUuid32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * word2003-2007转换成html
     * 支持 .doc and .docx
     * @param fileRootPath: 文件根位置
     * @param wordFileName: 需转换的word文件名
     * @param imagePath:    图片存放路径
     * @return 返回html内容
     */
    @SneakyThrows(Exception.class)
    public static String word2Html(String fileRootPath, String wordFileName, String imagePath) {
        // word 文件路径
        final String wordFilePath = fileRootPath + "/" + wordFileName;
        // 文件后缀名
        final String wordFileNameSuffix = wordFileName.substring(wordFileName.lastIndexOf(".") + 1);

        // 判断是否为`docx`文件
        boolean ifDocxSuffix = "docx".equals(wordFileNameSuffix);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (ifDocxSuffix) {
            // step 1 : load DOCX into XWPFDocument
            InputStream inputStream = Files.newInputStream(Paths.get(wordFilePath));
            XWPFDocument document = new XWPFDocument(inputStream);
            // step 2 : prepare XHTML options
            XHTMLOptions options = XHTMLOptions.create();
            // 存放图片的文件夹
            options.setExtractor(new FileImageExtractor(new File(imagePath)));
            options.setIgnoreStylesIfUnused(false);
            options.setFragment(true);
            // html中图片的路径
            options.URIResolver(new IURIResolver() {
                // step 3 : convert XWPFDocument to XHTML
                public String resolve(String uri) {
                    return imagePath + "/" + uri;
                }
            });
            XHTMLConverter.getInstance().convert(document, out, options);
        } else {
            HWPFDocument wordDocument = new HWPFDocument(Files.newInputStream(Paths.get(wordFilePath)));
            WordToHtmlConverter wordToHtmlConverter =
                    new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

            // 设置图片存储位置，并保存
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                @SneakyThrows(Exception.class)
                public String savePicture(byte[] content, PictureType pictureType, String suggestedName,
                                          float widthInches, float heightInches) {
                    // 首先要判断图片是否能识别
                    if (pictureType.equals(PictureType.UNKNOWN)) {
                        return "";
                    }
                    String htmlImgPath = imagePath + "/" + suggestedName;
                    FileOutputStream os = new FileOutputStream(FileUploadUtils.touch(htmlImgPath));
                    os.write(content);
                    os.close();
                    // 可将文件上传到第三方存储文件服务器，然后返回相应图片地址
                    return htmlImgPath;
                }
            });

            // 解析word文档
            wordToHtmlConverter.processDocument(wordDocument);

            // save pictures
            List<Picture> picList = wordDocument.getPicturesTable().getAllPictures();
            if (!CollectionUtils.isEmpty(picList)) {
                picList.forEach(pic -> {
                    FileOutputStream outputStream;
                    try {
                        outputStream = new FileOutputStream(imagePath + "/" +
                        pic.suggestFullFileName());
                        pic.writeImageContent(outputStream);
                    } catch (Exception e) {
                        log.error("exception message", e);
                    }
                });
            }

            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(out);

            // 这个应该是转换成xml的
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
        }
        // 关闭流
        out.close();
        // 返回html文件内容
        return out.toString();
    }

    /**
     * `word` 转 `html`
     * @param fileRootPath: 文件根位置
     * @param wordFileName: 需转换的word文件名
     * @param htmlFileName: 最后生成后的html文件名
     * @return 生成的html文件信息
     */
    public static File word2HtmlFile(String fileRootPath, String wordFileName, String htmlFileName) {
        final String htmlFilePath = fileRootPath + "/" + htmlFileName;
        // 获取word转html文件内容
        String htmlContent = Word2HtmlUtils.word2HtmlContent(fileRootPath, wordFileName, htmlFileName);
        // 生成html文件
        return FileUploadUtils.writeFileContent(htmlContent, htmlFilePath);
    }

    /**
     * `word` 转 `html`
     * @param fileRootPath: 文件根位置
     * @param wordFileName: 需转换的word文件名
     * @param htmlFileName: 最后生成后的html文件名
     * @return 生成的html文件信息
     */
    public static String word2HtmlContent(String fileRootPath, String wordFileName, String htmlFileName) {
        final String imagePath = fileRootPath + "/image";
        return Word2HtmlUtils.word2Html(fileRootPath, wordFileName, imagePath);
    }

}
