package com.huce.project.service.Impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.huce.project.entity.PaymentEntity;
import com.huce.project.entity.PaymentStatus;
import com.huce.project.repository.PaymentRepository;
import com.huce.project.service.EmailService;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final PaymentRepository paymentRepository;

    @Override
    public void sendEmailWithAttachment(String to, String subject, String body, List<byte[]> attachmentDataList,
            List<String> fileNames, String transactionId) {

        // Check if Payment exists and if the status is PENDING
        Optional<PaymentEntity> paymentEntityOpt = paymentRepository.findByTransactionId(transactionId);
        if (paymentEntityOpt.isPresent()) {
            PaymentStatus paymentStatus = paymentEntityOpt.get().getPaymentStatus();
            if (paymentStatus == PaymentStatus.PENDING) {
                logger.warn("Payment with transaction ID {} is still pending.", transactionId);
                throw new IllegalArgumentException("Order is pending");
            }
        }

        // Validate input parameters
        if (to == null || subject == null || body == null ||
                attachmentDataList == null || fileNames == null ||
                attachmentDataList.isEmpty() || fileNames.isEmpty() ||
                attachmentDataList.size() != fileNames.size()) {
            throw new IllegalArgumentException("Invalid email details or attachments");
        }

        // Create a MIME message for the email
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);

            // Add email body
            StringBuilder enhancedBody = new StringBuilder(body);
            enhancedBody.append("\n\nAttached QR Codes:\n");
            for (String fileName : fileNames) {
                enhancedBody.append("- ").append(fileName).append("\n");
            }
            helper.setText(enhancedBody.toString());

            // Add attachments
            for (int i = 0; i < attachmentDataList.size(); i++) {
                DataSource attachment = new ByteArrayDataSource(attachmentDataList.get(i), "image/png");
                helper.addAttachment(fileNames.get(i), attachment);
            }

            // Send the email
            mailSender.send(message);
            logger.info("Email with {} attachments sent successfully to {} with subject '{}'",
                    attachmentDataList.size(), to, subject);
        } catch (MessagingException e) {
            logger.error("Error sending email with attachments to {}: {}",
                    to, e.getMessage());
            throw new RuntimeException("Error sending email with attachments", e);
        }
    }
}
