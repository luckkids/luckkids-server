package com.luckkids.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Logger;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        // HandlerMethod(Controller메소드인지)가 아니거나 Filter에서 Wrapping한 reequest가 아니라면 return
        if(!(handler instanceof HandlerMethod) || !(request instanceof ContentCachingRequestWrapper cachingRequest))
            return;

        // Spring Security관련 요청은 return
        if (request.getClass().getName().contains("SecurityContextHolderAwareRequestWrapper")) {
            return;
        }

        // Filter에서 Wrapping한 response
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

        String url = request.getRequestURL().toString();
        String queryStr = request.getQueryString();
        String method = cachingRequest.getMethod();
        String requestStr = "";
        String responseStr = "";

        //요청 응답이 appliction/json일 경우에만 내용 처리
        if (cachingRequest.getContentType() != null && cachingRequest.getContentType().contains("application/json")) {
            cachingRequest.getContentAsByteArray();
            if (cachingRequest.getContentAsByteArray().length != 0){
                byte[] contentBytes = cachingRequest.getContentAsByteArray();
                requestStr = cleanString(new String(contentBytes, StandardCharsets.UTF_8));
            }
        }
        if (cachingResponse.getContentType() != null && cachingResponse.getContentType().contains("application/json")) {
            cachingResponse.getContentAsByteArray();
            if (cachingResponse.getContentAsByteArray().length != 0) {
                byte[] contentBytes = cachingResponse.getContentAsByteArray();
                responseStr = cleanString(new String(contentBytes, StandardCharsets.UTF_8));
            }
        }

        logging(url, queryStr, method, requestStr, responseStr);
    }

    public void logging(String url, String queryStr, String method, String requestStr, String responseStr) {
        StringBuilder logMessageBuilder = new StringBuilder("\n");
        if("GET".equals(method)) {
            url+="/"+ Optional.ofNullable(queryStr).orElse("");
        }
        logMessageBuilder.append("┌───────────────────────────────────────────────────────────────────────────────────────\n");
        logMessageBuilder.append("│Request URL: ").append(url).append("\n");
        logMessageBuilder.append("│Request Method: ").append(method).append("\n");
        if(!"".equals(requestStr)&&requestStr!=null) {
            logMessageBuilder.append("│Request Body: ").append(requestStr).append("\n");
        }
        if(!"".equals(responseStr)&&responseStr!=null) {
            logMessageBuilder.append("│Response Body: ").append(responseStr.replace("\n", "")).append("\n");
        }
        logMessageBuilder.append("└───────────────────────────────────────────────────────────────────────────────────────\n");
        log.info(logMessageBuilder.toString());
    }

    public String cleanString(String str) {
        return str.replace("\n", "").replace("\r", "").replace(" ", "");
    }
}
