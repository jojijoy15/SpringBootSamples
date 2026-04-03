package com.example.beanscopes.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
//@Scope(scopeName = SCOPE_REQUEST)
public class RequestScoped {

    Logger log = LoggerFactory.getLogger(RequestScoped.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    public void responseWithRequestId() {
        String requestId =  request.getHeader("requestId");
        log.info("requestId={}", requestId);
        response.setHeader("requestId", requestId);
    }
}
