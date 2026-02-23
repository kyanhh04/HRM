package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.notifications.DateRangeInfo;
import com.vatek.hrmtool.notifications.MailNotificationsParam;
import com.vatek.hrmtool.notifications.MailOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.vatek.hrmtool.constant.NotificationMessages;
import com.vatek.hrmtool.dto.LandingPageContact.DiscussFormSubmitDto;
import com.vatek.hrmtool.dto.LandingPageContact.RequestFormSubmitDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

@Service
public class NotificationsService {

    @Autowired
    private JavaMailSender javaMailSender;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void sendMail(MailOptions mailOptions) {
        try {
            // If HTML content is available, send as HTML email; otherwise send as plain text
            if (mailOptions.getHtml() != null && !mailOptions.getHtml().isEmpty()) {
                sendHtmlMail(mailOptions);
            } else {
                sendPlainTextMail(mailOptions);
            }
        } catch (MessagingException | MailException e) {
            System.err.println("Failed to send email to: " + mailOptions.getTo());
            e.printStackTrace();
        }
    }

    private void sendPlainTextMail(MailOptions mailOptions) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailOptions.getFrom());
        message.setTo(mailOptions.getTo().toArray(new String[0]));
        message.setSubject(mailOptions.getSubject());
        message.setText(mailOptions.getText());

        javaMailSender.send(message);
        System.out.println("Plain text email sent to: " + mailOptions.getTo());
    }

    private void sendHtmlMail(MailOptions mailOptions) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(mailOptions.getFrom());
        helper.setTo(mailOptions.getTo().toArray(new String[0]));
        helper.setSubject(mailOptions.getSubject());
        helper.setText(mailOptions.getText(), mailOptions.getHtml());
        
        javaMailSender.send(message);
        System.out.println("HTML email sent to: " + mailOptions.getTo());
    }

    public MailOptions handleParseMailOption(MailNotificationsParam param) {
        MailOptions mailOptions = new MailOptions();
        mailOptions.setTo(Arrays.asList(param.getReceivers()));
        mailOptions.setFrom(NotificationMessages.NOTIFICATIONS_FROM_MAIL);
        DateRangeInfo dateRange = formatDateRange(param.getDayFrom(), param.getDayTo());

        switch (param.getNotificationsType()) {
            case APPROVE_LEAVE:
                mailOptions.setSubject("Leave Approval for " + getDateRangeText(dateRange));
                mailOptions.setHtml(param.getReplacedHtml());
                mailOptions.setText(param.getReplacedHtml());
                break;

            case REJECT_LEAVE:
                mailOptions.setSubject("Leave Disapproval for " + getDateRangeText(dateRange));
                mailOptions.setHtml(param.getReplacedHtml());
                mailOptions.setText(param.getReplacedHtml());
                break;

            case FORGOT_PASSWORD:
                mailOptions.setSubject(param.getFullName() + " - Change Password");
                mailOptions.setHtml(param.getReplacedHtml());
                mailOptions.setText(param.getReplacedHtml());
                break;

            case ALL_DAY:
            case MORNING:
            case AFTERNOON:
                mailOptions.setSubject(param.getUsername() + " - Time Off Request");
                mailOptions.setText(buildTimeOffRequestText(param, dateRange));
                break;

            case BIRTHDAY:
                mailOptions.setSubject("Happy Birthday!");
                mailOptions.setText(buildBirthdayText(param.getFullName()));
                break;

            case ADMIN_CONFIRM:
                mailOptions.setSubject("Time Off Request");
                mailOptions.setText(buildAdminConfirmText(param.getStatus(), param.getUsername()));
                break;

            case NEW_ACCOUNT:
                mailOptions.setSubject("New Account Created");
                mailOptions.setText(buildNewAccountText(param));
                break;

            case LANDING_PAGE_REQUEST_FORM_SUBMIT:
                mailOptions.setSubject("Landing Page Form Details");
                mailOptions.setText(buildRequestFormText(param.getRequestFormData()));
                break;

            case LANDING_PAGE_DISCUSS_FORM_SUBMIT:
                mailOptions.setSubject("Landing Page Form Details");
                mailOptions.setText(buildDiscussFormText(param.getDiscussFormData()));
                break;

            default:
                mailOptions.setSubject("Notification");
                mailOptions.setText("Email notification");
                break;
        }
        return mailOptions;
    }

    private DateRangeInfo formatDateRange(Date dayFrom, Date dayTo) {
        if (dayFrom == null || dayTo == null) {
            return new DateRangeInfo(true, "", "");
        }

        boolean isDate = dayFrom.getTime() == dayTo.getTime();
        LocalDate fromDate = dayFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate toDate = dayTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return new DateRangeInfo(isDate, fromDate.format(DATE_FORMATTER), toDate.format(DATE_FORMATTER));
    }

    private String getDateRangeText(DateRangeInfo dateRange) {
        if (dateRange.isDate()) {
            return dateRange.getDayFrom();
        }
        return "from " + dateRange.getDayFrom() + " to " + dateRange.getDayTo();
    }
        private String buildTimeOffRequestText(MailNotificationsParam param, DateRangeInfo dateRange) {
            String dateRangeText = dateRange.isDate()
                    ? dateRange.getDayFrom()
                    : "from " + dateRange.getDayFrom() + " to " + dateRange.getDayTo();
            return "Subject: " + param.getUsername() + " - Leave Request\n\n" +
                    "Dear VATEK,\n\n" +
                    "I would kindly like to ask for your approval for my leave request, which I'm planning to take on date "+ dateRangeText + "\n" +
                    "With reason: " + param.getReason() + "\n\n" +
                    "Best regards,\n" + param.getUsername() + "\n\n" +
                    "=========================\n" +
                    "This is an automatic message, please do not reply to this email!";
        }

        private String buildNewAccountText(MailNotificationsParam param) {
            return "Subject: New Account Created\n\n" +
                    "Dear " + param.getFullName() + ",\n\n" +
                    "Welcome to Vatek — we are excited to have you aboard and look forward to working with you.\n\n" +
                    "Here's your account to use HRM:\n" +
                    "Username: " + param.getUsername() + "\n" +
                    "Password: " + param.getPassword() + "\n\n" +
                    "This is our HRM link: http://hrm.vatek.asia\n\n" +
                    "Once you have access to HRM, you can go to \"My Profile\" to change your password.\n" +
                    "If you have any issues, please let HR or your Project Manager know.\n\n" +
                    "Welcome aboard! We look forward to working with you, and we're happy to have you on the team.\n\n" +
                    "Best regards,\n" +
                    "Vatek Team\n\n" +
                    "=========================\n" +
                    "This is an automatic message, please do not reply to this email!";
        }

        private String buildBirthdayText(String fullName) {
            return "Subject: Happy Birthday!\n\n" +
                    "Dear " + fullName + ",\n\n" +
                    "To an amazing employee, colleague, and friend, Happy Birthday! You are a valuable employee and deserve nothing but the best. " +
                    "We really enjoy having you here and hope you have a wonderful day today. Happy birthday!";
        }

        private String buildAdminConfirmText(String status, String username) {
            String statusText = "APPROVED".equals(status) ? "approved" : "rejected";
            return "VATEK Dayoff request\n\n" +
                    "Your request have been " + statusText + "\n" +
                    "By: " + username + "\n\n" +
                    "Have a nice day!!\n" +
                    "For more information please contact to your PM or HR\n" +
                    "Best regards!!";
        }

        private String buildRequestFormText(RequestFormSubmitDto data) {
            return String.format(
                    "=== Request Form Submission ===\n\n" +
                            "Project Stage: %s\n" +
                            "Product Types: %s\n" +
                            "Technologies: %s\n" +
                            "Design Needed: %s\n" +
                            "Project Size: %s\n" +
                            "Preferred Team: %s\n" +
                            "Time to Deliver: %s\n" +
                            "Expected Budget: %s\n\n" +
                            "--- Client Information ---\n" +
                            "Name: %s\nCompany: %s\nPhone: %s\nEmail: %s",
                    data.getProjectStage(),
                    String.join(", ", data.getProductType()),
                    String.join(", ", data.getTechnologies()),
                    data.getIsDesignNeeded() ? "Yes" : "No",
                    data.getProjectSize(),
                    data.getPreferedTeam(),
                    data.getTimeToDeliver(),
                    data.getExpectedBudget(),
                    data.getClientName(),
                    data.getCompanyName(),
                    data.getPhoneNumber(),
                    data.getEmail()
            );
        }

        private String buildDiscussFormText(DiscussFormSubmitDto data) {
            return String.format(
                    "=== Discuss Form Submission ===\n\n" +
                            "Name: %s\nCompany: %s\nPhone: %s\nEmail: %s\nMessage: %s",
                    data.getName(),
                    data.getCompanyName(),
                    data.getPhoneNumber(),
                    data.getEmail(),
                    data.getMessage()
            );
        }
}
