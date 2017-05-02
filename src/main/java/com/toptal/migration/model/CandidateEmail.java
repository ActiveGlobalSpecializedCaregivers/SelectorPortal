package com.toptal.migration.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <code>CandidateEmail</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class CandidateEmail {
    private static final SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private String commId;
    private String prospectId;
    private String senderEmail;
    private String recipientEmail;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private Date sent;

    public CandidateEmail(String[] fields) {
        commId = fields[0];
        prospectId = fields[1];
        senderEmail = fields[2];
        recipientEmail = fields[3];
        cc = fields[4];
        bcc = fields[5];
        subject = fields[6];
        body = StringUtil.stripQuotes(fields[7]);
        sent = DateUtils.parse(fields[8], dateParser);
    }

    public String getBcc() {
        return bcc;
    }

    public String getBody() {
        return body;
    }

    public String getCc() {
        return cc;
    }

    public String getCommId() {
        return commId;
    }

    public static SimpleDateFormat getDateParser() {
        return dateParser;
    }

    public String getProspectId() {
        return prospectId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public Date getSent() {
        return sent;
    }

    public String getSubject() {
        return subject;
    }
}
