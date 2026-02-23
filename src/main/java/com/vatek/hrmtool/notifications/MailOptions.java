package com.vatek.hrmtool.notifications;

import lombok.Data;

import java.util.List;

@Data
public class MailOptions {
    private List<String> to;
    private String from;
    private String subject;
    private String text;
    private String html;

}
