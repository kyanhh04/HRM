package com.vatek.hrmtool.notifications;

import java.util.Date;
import com.vatek.hrmtool.enumeration.RequestNotificationType;
import com.vatek.hrmtool.dto.LandingPageContact.DiscussFormSubmitDto;
import com.vatek.hrmtool.dto.LandingPageContact.RequestFormSubmitDto;
import lombok.Data;

@Data
public class MailNotificationsParam {
    private String[] receivers;
    private String username;
    private String reason;
    private Date dayFrom;
    private Date dayTo;
    private String status;
    private RequestNotificationType notificationsType;
    private String password;
    private String fullName;
    private RequestFormSubmitDto requestFormData;
    private DiscussFormSubmitDto discussFormData;
    private String replacedHtml;

}
