package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String url;
    private final String type;
    private final String[] details;

    public ErrorInfo(CharSequence url, ErrorType type, String... details) {
        this.url = url.toString();
        this.type = type.getErrorCode();
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String[] getDetails() {
        return details;
    }
}