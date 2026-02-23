package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.vatek.hrmtool.constant.DateConstant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${fe.base.url:http://hrm.vatek.asia}")
    private String feBaseUrl;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DateConstant.DD_MM_YYYY);

    @Override
    public String generateLeaveTemplate(String fullName, LocalDate fromDay, LocalDate toDay, String templateFilePath) {
        String date;
        if (fromDay.isEqual(toDay)) {
            date = fromDay.format(DATE_FORMAT);
        } else {
            date = fromDay.format(DATE_FORMAT) + " to " + toDay.format(DATE_FORMAT);
        }
        String dateToday = LocalDate.now().format(DATE_FORMAT);

        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("fromDay", fromDay);
        context.setVariable("toDay", toDay);
        context.setVariable("date", date);
        context.setVariable("dateToday", dateToday);
        String templatePath = templateFilePath.startsWith("mail/") ? templateFilePath : "mail/" + templateFilePath;
        return templateEngine.process(templatePath, context);
    }

    @Override
    public String forgotPasswordTemplate(String fullName, String token) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("resetLink", feBaseUrl + "/reset_password/" + token);

        return templateEngine.process("mail/forgot_password", context);
    }
}
