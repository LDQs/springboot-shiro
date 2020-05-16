package com.ldq.dao;

import com.ldq.domain.Right;
import com.ldq.mapper.RightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RightServiceImpl implements RightMapper{
    @Autowired
    private RightMapper rightMapper;

    @Override
    public List<Right> getAllRights() {
        return rightMapper.getAllRights();
    }

    @Override
    public Integer findByRightName(String rightName) {
        return rightMapper.findByRightName(rightName);
    }
}
