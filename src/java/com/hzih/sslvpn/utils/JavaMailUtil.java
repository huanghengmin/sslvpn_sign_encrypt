package com.hzih.sslvpn.utils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JavaMailUtil {
	public static void sendMail(Map<String, String> sender, String subject,
			String text, String contact) throws AddressException,
			MessagingException {
		String host = sender.get("server"); // 指定的smtp服务器
		String port = sender.get("port"); // 指定的smtp服务器端口
		String from = sender.get("email"); // 邮件发送人的邮件地址
		final String username = sender.get("account"); // 发件人的邮件帐户
		final String password = sender.get("password"); // 发件人的邮件密码

		// 创建Properties 对象
		Properties props = System.getProperties();

		// 添加smtp服务器属性
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", (port == null || port.length() == 0) ? 25
				: port);// 默认25
		props.put("mail.smtp.auth", "true"); // 163的stmp不是免费的也不公用的，需要验证

		// 创建邮件会话
		Session session = Session.getDefaultInstance(props,
				new Authenticator() { // 验账账户
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}

				});

		// 定义邮件信息
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		if (contact != null && !"".equals(contact)) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					contact.toString()));
		}
		message.setSubject(subject);
		message.setText(text);

		// 发送消息
		// session.getTransport("smtp").send(message); //也可以这样创建Transport对象
		Transport.send(message);

	}

	public static void main(String args[]) {
		String subject = "最近怎么样";
		String text = "还好吧？";

		String server = "smtp.qq.com"; // 指定的smtp服务器
		String email = "1452065568@qq.com"; // 邮件发送人的邮件地址
		String account = "1452065568@qq.com"; // 发件人的邮件帐户
		String password = "qhzwbsa013689911"; // 发件人的邮件密码
		String contact = "364234171@qq.com";

		Map<String, String> sender = new HashMap<String, String>();
		sender.put("server", server);
		sender.put("email", email);
		sender.put("account", account);
		sender.put("password", password);

		try {
			sendMail(sender, subject, text, contact);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
