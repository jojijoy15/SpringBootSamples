package com.example.componentscan.session;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    public static final Logger log = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    RequestSession requestSession;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/session")
    public ResponseEntity<String> getSession() {
        String sessionId = httpSession.getId();
        String userName = requestSession.getUserName();

        log.debug("Session Id: {}", sessionId);
        String response = """
                {
                "sessionId": "%s",
                "userName": "%s"
                }""".formatted(sessionId, userName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
