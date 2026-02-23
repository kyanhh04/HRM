package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.dto.LandingPageContact.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vatek.hrmtool.notifications.*;
import com.vatek.hrmtool.enumeration.RequestNotificationType;

@Service
public class LandingPageContactService {

    @Autowired
    private NotificationsService notificationsService;

    private final String[] adminEmails = {"info@vatek.asia"};

    public void submitRequestForm(RequestFormSubmitDto data) {
        MailNotificationsParam param = new MailNotificationsParam();
        param.setReceivers(adminEmails);
        param.setNotificationsType(RequestNotificationType.LANDING_PAGE_REQUEST_FORM_SUBMIT);
        param.setRequestFormData(data);
        MailOptions payload = notificationsService.handleParseMailOption(param);
        notificationsService.sendMail(payload);
    }

    public void submitDiscussForm(DiscussFormSubmitDto data) {
        MailNotificationsParam param = new MailNotificationsParam();
        param.setReceivers(adminEmails);
        param.setNotificationsType(RequestNotificationType.LANDING_PAGE_DISCUSS_FORM_SUBMIT);
        param.setDiscussFormData(data);
        MailOptions payload = notificationsService.handleParseMailOption(param);
        notificationsService.sendMail(payload);
    }
}
