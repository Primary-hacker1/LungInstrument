package com.just.machine.util;

import android.content.Context;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.just.news.R;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.*;
import java.util.Map;

/**
 * 报告工具类
 */
public class WordUtil {

    private static Configuration configuration = null;

    private static WordUtil tplm = null;

    /**
     * 根据模板生成相应的文件
     *
     * @param root     保存数据的map
     * @param template 模板文件的地址
     * @return
     */
    public static synchronized ByteArrayOutputStream process(Map<?, ?> root, String template) {

        if (null == root) {
            throw new RuntimeException("数据不能为空");
        }

        if (null == template) {
            throw new RuntimeException("模板文件不能为空");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String templatePath = template.substring(0, template.lastIndexOf("/"));
        String templateName = template.substring(template.lastIndexOf("/") + 1, template.length());

        if (null == configuration) {
            configuration = new Configuration(Configuration.VERSION_2_3_23);  // 这里Configurantion对象不能有两个，否则多线程访问会报错
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassicCompatible(true);
        }
        configuration.setClassForTemplateLoading(WordUtil.class, templatePath);

        Template t = null;
        try {
            t = configuration.getTemplate(templateName);
            Writer w = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            t.process(root, w);  // 这里w是一个输出地址，可以输出到任何位置，如控制台，网页等
            w.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return outputStream;
    }

    private WordUtil(Context context) {
        configuration = new Configuration();
        try {
            // 注册tmlplate的load路径
            // cfg.setClassForTemplateLoading(this.getClass(), "/template/");
            configuration.setDirectoryForTemplateLoading(new File(context.getExternalFilesDir("").getAbsolutePath()));
        } catch (Exception e) {

        }
    }

    private static Template getTemplate(Context context,String name) throws IOException {
        if (tplm == null) {
            tplm = new WordUtil(context);
        }
        return tplm.configuration.getTemplate(name);
    }

    public static void process(Context context,String templatefile, Map param, Writer out) throws IOException, TemplateException {
        // 获取模板
        Template template = WordUtil.getTemplate(context,templatefile);
        template.setOutputEncoding("UTF-8");
        // 合并数据
        template.process(param, out);
        if (out != null) {
            out.close();
        }
    }

    /**
     * word转pdf
     *
     * @param wordPath
     * @param pdfPath
     */
    public static void wordToPdf(Context context, String wordPath, String pdfPath) {
        FileOutputStream os = null;
        try {
            //凭证 不然切换后有水印
//            InputStream is = context.getResources().openRawResource(R.raw.license);
//            License aposeLic = new License();
//            aposeLic.setLicense(is);
            //生成一个空的PDF文件
            File file = new File(pdfPath);
            os = new FileOutputStream(file);
            //要转换的word文件
            com.aspose.words.Document doc = new com.aspose.words.Document(wordPath);
            doc.save(os, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static Paper getPaper() {
//        Paper paper = new Paper();
//        // 默认为A4纸张，对应像素宽和高分别为 595, 842
//        int width = 595;
//        int height = 842;
//        // 设置边距，单位是像素，10mm边距，对应 28px
//        int marginLeft = 0;
//        int marginRight = 0;
//        int marginTop = 0;
//        int marginBottom = 0;
//        paper.setSize(width, height);
//        // 下面一行代码，解决了打印内容为空的问题
//        paper.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));
//        return paper;
//    }

    /**
     * 静默打印word文档
     *
     * @param pdfPath
     * @param printName
     */
//    public static void printWord(String pdfPath, String printName, int pageType) {
//        // String printerName = "Brother MFC-8535DN Printer (副本 1)";//打印机名包含字串
//        String printerName = "Lenovo L100";
//        PDDocument document = null;
//        try {
//            //String pdfFile = "D:\\六分钟步行试验报告\\测试1\\320125199506150000_8.pdf";//文件路径
//            File file = new File(pdfPath);
//            document = PDDocument.load(file);
//            PrinterJob printJob = PrinterJob.getPrinterJob();
//            printJob.setJobName(printName);
//            if (printerName != null) {
//                // 查找并设置打印机
//                //获得本台电脑连接的所有打印机
//                PrintService[] printServices = PrinterJob.lookupPrintServices();
//                if (printServices == null || printServices.length == 0) {
//                    System.out.print("打印失败，未找到可用打印机，请检查。");
//                    return;
//                }
//                PrintService printService = null;
//                //匹配指定打印机
//                for (int i = 0; i < printServices.length; i++) {
//                    //System.out.println(printServices[i].getName());
//                    if (printServices[i].getName().contains(printerName)) {
//                        printService = printServices[i];
//                        break;
//                    }
//                }
//                if (printService != null) {
//                    printJob.setPrintService(printService);
//                } else {
//                    System.out.print("打印失败，未找到名称为" + printerName + "的打印机，请检查。");
//                    return;
//                }
//            }
//            //设置纸张及缩放
//            PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
//            //设置多页打印
//            Book book = new Book();
//            PageFormat pageFormat = new PageFormat();
//            //设置打印方向
//            pageFormat.setOrientation(PageFormat.PORTRAIT);//纵向
//            pageFormat.setPaper(getPaper());//设置纸张
//            int pageInt = document.getNumberOfPages();
//            if (pageType == 2) {
//                //System.out.println("word的页面==" + pageInt + "页===============");
//                if (pageInt == 5) {
//                    pageInt = pageInt - 1;
//                }
//                pageInt = pageInt - 1;
//            }
//            book.append(pdfPrintable, pageFormat, pageInt);
//            printJob.setPageable(book);
//            printJob.setCopies(1);//设置打印份数
//            //添加打印属性
//            HashPrintRequestAttributeSet pars = new HashPrintRequestAttributeSet();
//            pars.add(Sides.ONE_SIDED); //设置单双页
//            printJob.print(pars);
//        } catch (InvalidPasswordException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (PrinterException e) {
//            e.printStackTrace();
//        } finally {
//            if (document != null) {
//                try {
//                    document.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
