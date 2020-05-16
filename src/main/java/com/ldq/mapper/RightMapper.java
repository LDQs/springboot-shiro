package com.ldq.mapper;

import com.ldq.domain.Right;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RightMapper {
    List<Right> getAllRights();

    Integer findByRightName(String rightName);
}
