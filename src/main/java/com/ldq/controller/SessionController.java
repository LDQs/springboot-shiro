package com.ldq.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SessionController {
    @CrossOrigin
    @GetMapping("/session/{key}")
    public Object getSessionByKey(@PathVariable("key") String key, HttpServletRequest request) {
        return request.getSession().getAttribute(key);
    }
}
