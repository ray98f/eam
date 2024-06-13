package com.wzmtr.eam.utils;

import lombok.extern.slf4j.Slf4j;
 
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * 邮箱邮件工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/13
 */
@Slf4j
public class EmailUtil {
    /**
     * 邮件服务器主机名
     * QQ邮箱的 SMTP 服务器地址为: smtp.qq.com
     */
    private static final String MY_EMAIL_HOST = "smtp.qq.com";

    /**
     * 发件人邮箱
     */
    private static final String MY_EMAIL_ACCOUNT = "***@qq.com";

    /**
     * 发件人邮箱密码（授权码）
     * 在开启SMTP服务时会获取到一个授权码，把授权码填在这里
     */
    private static final String MY_EMAIL_PASSWORD = "*****";

    /**
     * 邮件单发（自由编辑短信，并发送，适用于私信）
     *
     * @param email 收件箱地址
     * @throws Exception
     */

    public static String sendEmail(String email, String title, String detail) {
        Transport ts = null;
        try {
            Properties prop = new Properties();
            //设置QQ邮件服务器
            prop.setProperty("mail.host", MY_EMAIL_HOST);
            //邮件发送协议
            prop.setProperty("mail.transport.protocol", "smtp");
            //需要验证用户名密码
            prop.setProperty("mail.smtp.auth", "true");

            //使用JavaMail发送邮件的5个步骤
            //1.创建定义整个应用程序所需的环境信息的Session对象
            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    //发件人邮件用户名、授权码
                    return new PasswordAuthentication(MY_EMAIL_ACCOUNT, MY_EMAIL_PASSWORD);
                }
            });

            //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(false);

            //2、通过session得到transport对象
            ts = session.getTransport();

            //3、使用邮箱的用户名和授权码连上邮件服务器
            ts.connect("smtp.qq.com", MY_EMAIL_ACCOUNT, MY_EMAIL_PASSWORD);

            //4，创建邮件
            //4-1.txt，创建邮件对象
            MimeMessage message = new MimeMessage(session);

            //4-2，指明邮件的发件人
            message.setFrom(new InternetAddress(MY_EMAIL_ACCOUNT));

            //4-3，指明邮件的收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

            //4-4，邮件标题
            message.setSubject(title);

            //4-5，邮件文本内容
            message.setContent(detail, "text/html;charset=UTF-8");

            //4-6，发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            //5，关闭连接
            ts.close();

        } catch (Exception e) {
            return "发送邮件失败";
        }
        return "发送成功";
    }
    
}