package com.huce.project.service;

import java.util.List;

public interface EmailService {
    void sendEmailWithAttachment(String to, String subject, String body, List<byte[]> attachmentData, List<String> fileName, String transaction_id);
}
