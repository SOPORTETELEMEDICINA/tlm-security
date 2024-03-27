package net.amentum.security.utils.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Email {

    private String from;

    private List<String> to;

    private List<String> cc;

    private String subject;

    private String message;

    private boolean isHtml;

    public Email() {
        this.to = new ArrayList<>();
        this.cc = new ArrayList<>();
    }

    public Email(String from, String toList, String subject, String message) {
        this();
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.to.addAll(Arrays.asList(splitByComma(toList)));
    }

    public Email(String from, String toList, String ccList, String subject, String message) {
        this();
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.to.addAll(Arrays.asList(splitByComma(toList)));
        this.cc.addAll(Arrays.asList(splitByComma(ccList)));
    }


    private String[] splitByComma(String toMultiple) {
        String[] toSplit = toMultiple.split(",");
        return toSplit;
    }

    public String getToAsList() {
        return String.join(",",this.to);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    @Override
    public String toString() {
        return "Email{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", cc=" + cc +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", isHtml=" + isHtml +
                '}';
    }
}
