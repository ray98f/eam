package com.wzmtr.eam.utils.convert;

import com.spire.doc.Document;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.widget.PdfPageCollection;
import com.wzmtr.eam.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * Pdf 转 Word 工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/18
 */
@Slf4j
public class Pdf2WordUtils {

    /**
     * 涉及到的路径
     * 1、pdf所在的路径，真实测试种是从外部引入的
     * 2、如果是大文件，需要进行切分，保存的子pdf路径
     */
    String splitPath = "./split/";

    /**
     * 3、如果是大文件，需要对子pdf文件一个一个进行转化
     */
    String docPath = "./doc/";

    /**
     * pdf 转化为 word
     * @param srcPath 引入目录
     * @param desPath 生成目录
     */
    public void pdf2Word(String srcPath, String desPath) {
        boolean result = false;
        try {
            // 0、判断输入的是否是pdf文件
            // 第一步：判断输入的是否合法
            boolean flag = isPdfFile(srcPath);
            // 第二步：在输入的路径下新建文件夹
            boolean flag1 = create();

            if (flag && flag1) {
                // 1、加载pdf
                PdfDocument pdf = new PdfDocument();
                pdf.loadFromFile(srcPath);
                PdfPageCollection num = pdf.getPages();

                // 2、如果pdf的页数小于11，那么直接进行转化
                if (num.getCount() <= CommonConstants.TEN) {
                    pdf.saveToFile(desPath, com.spire.pdf.FileFormat.DOCX);
                }
                // 3、否则输入的页数比较多，就开始进行切分再转化
                else {
                    // 第一步：将其进行切分,每页一张pdf
                    pdf.split(splitPath + "test{0}.pdf", 0);

                    // 第二步：将切分的pdf，一个一个进行转换
                    File[] fs = getSplitFiles(splitPath);
                    for (File f : fs) {
                        PdfDocument sonpdf = new PdfDocument();
                        sonpdf.loadFromFile(f.getAbsolutePath());
                        sonpdf.saveToFile(docPath + f.getName().substring(0, f.getName().length() - 4) + ".docx",
                            FileFormat.DOCX);
                    }
                    // 第三步：对转化的doc文档进行合并，合并成一个大的word
                    try {
                        result = this.merge(docPath, desPath);
                    } catch (Exception e) {
                        log.error("exception message", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("exception message", e);
        } finally {
            // 4、把刚刚缓存的split和doc删除
            if (result) {
                this.clearFiles(splitPath);
                this.clearFiles(docPath);
            }
        }
    }

    /**
     * 新建文件夹
     * @return 成功
     */
    private boolean create() {
        File f = new File(splitPath);
        File f1 = new File(docPath);
        if (!f.exists()) {
            boolean b = f.mkdirs();
            if (!b) {
                log.error("mkdirs error");
            }
        }
        if (!f.exists()) {
            boolean b = f1.mkdirs();
            if (!b) {
                log.error("mkdirs error");
            }
        }
        return true;
    }

    /**
     * 判断是否是pdf文件
     * @param srcPath2 地址
     * @return 是否为pdf文件
     */
    private boolean isPdfFile(String srcPath2) {
        File file = new File(srcPath2);
        String filename = file.getName();
        return filename.endsWith(".pdf");
    }

    /**
     * 取得某一路径下所有的pdf
     * @param path 路径
     * @return 所有pdf
     */
    private File[] getSplitFiles(String path) {
        File f = new File(path);
        return f.listFiles();
    }

    /**
     * 清空文件
     * @param workspaceRootPath 地址
     */
    public void clearFiles(String workspaceRootPath) {
        File file = new File(workspaceRootPath);
        if (file.exists()) {
            deleteFile(file);
        }
    }

    /**
     * 删除文件
     * @param file 文件
     */
    public void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File value : files) {
                deleteFile(value);
            }
        }
        boolean b = file.delete();
        if (!b) {
            log.error("delete error");
        }
    }

    /**
     * 合并目录
     * @param docPath 文件目录
     * @param desPath 目标目录
     * @return 是否合并成功
     */
    private boolean merge(String docPath, String desPath) {
        File[] fs = this.getSplitFiles(docPath);
        System.out.println(docPath);
        Document document = new Document(docPath + "test0.docx");

        for (int i = 1; i < fs.length; i++) {
            document.insertTextFromFile(docPath + "test" + i + ".docx", com.spire.doc.FileFormat.Docx_2013);
        }
        // 第四步：对合并的doc进行保存2
        document.saveToFile(desPath);
        return true;
    }

}
