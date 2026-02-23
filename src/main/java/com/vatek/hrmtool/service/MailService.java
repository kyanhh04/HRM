package com.vatek.hrmtool.service;

import java.time.LocalDate;

public interface MailService {
    String generateLeaveTemplate(String fullName, LocalDate fromDay, LocalDate toDay, String templateFilePath);
    String forgotPasswordTemplate(String fullName, String token);
}
