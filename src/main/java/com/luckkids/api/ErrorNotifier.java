package com.luckkids.api;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;

@Component
public class ErrorNotifier {

    private final String webhookUrl;

    private ErrorNotifier(@Value("${slack.webhook-url}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void sendErrorToSlack(Exception e) throws IOException {
        Slack slack = Slack.getInstance();
        String detailedErrorMessage = buildErrorMessage(e);
        Payload payload = Payload.builder()
            .text(detailedErrorMessage)
            .build();
        slack.send(webhookUrl, payload);
    }

    private String buildErrorMessage(Exception e) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("*Error occurred at*: `").append(getFormattedDateTime()).append("`\n")
            .append("*Exception*: `").append(e.getClass().getName()).append("`\n")
            .append("*Message*: `").append(e.getMessage()).append("`\n");

        if (e.getStackTrace().length > 0) {
            errorMessage.append("*At*: `").append(e.getStackTrace()[0]).append("`");
        }

        return errorMessage.toString();
    }

    private String getFormattedDateTime() {
        return now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
