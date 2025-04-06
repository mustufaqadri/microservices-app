package com.dev.ecommerce.email;

import com.dev.ecommerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_RELATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.port}")
    private int mailPort;

    @Async
    public void sendPaymentSuccessEmail(String customerEmail, String customerName, BigDecimal amount, String orderReference) throws MessagingException {
        System.out.println("\n MAIL PORT: " + mailPort);
        if (mailSender instanceof org.springframework.mail.javamail.JavaMailSenderImpl impl) {
            System.out.println("💡 JavaMailSender Config → Host: " + impl.getHost() + ", Port: " + impl.getPort());
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_RELATED, UTF_8.name());
        helper.setFrom("contact@dev.com");

        final String templateName = EmailTemplate.PAYMENT_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("customerEmail", customerEmail);

        Context context = new Context();
        context.setVariables(variables);
        helper.setSubject(EmailTemplate.PAYMENT_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            helper.setText(htmlTemplate, true);
            helper.setTo(customerEmail);
            mailSender.send(mimeMessage);
            log.info("INFO: Email sent successfully to customer " + customerEmail);
        } catch (MessagingException e) {
            log.warn("ERROR: Email sent successfully to customer " + customerEmail);
        }
    }

    @Async
    public void sendOrderConfirmationEmail(String customerEmail, String customerName, BigDecimal amount, String orderReference, List<Product> products) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_RELATED, UTF_8.name());
        helper.setFrom("contact@dev.com");

        final String templateName = EmailTemplate.ORDER_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("total amount", amount);
        variables.put("products", products);
        variables.put("customerEmail", customerEmail);

        Context context = new Context();
        context.setVariables(variables);
        helper.setSubject(EmailTemplate.ORDER_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            helper.setText(htmlTemplate, true);
            helper.setTo(customerEmail);
            mailSender.send(mimeMessage);
            log.info("INFO: Email sent successfully to customer " + customerEmail);
        } catch (MessagingException e) {
            log.warn("ERROR: Email sent successfully to customer " + customerEmail);
        }
    }
}
