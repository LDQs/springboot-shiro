package com.ldq.controller;

import com.ldq.dao.RightServiceImpl;
import com.ldq.domain.Right;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RightController {
    @Autowired
    private RightServiceImpl rightService;

    @RequestMapping("/rights/list")
    @CrossOrigin
    public List<Right> getAllRights() {
        List<Right> rights = rightService.getAllRights();
        return rights;
    }
}
