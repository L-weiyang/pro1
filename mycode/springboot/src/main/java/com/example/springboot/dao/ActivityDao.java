package com.example.springboot.dao;

import com.example.springboot.entity.Activity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ActivityDao extends Mapper<Activity> {


    @Select("select * from news where `id` = #{id}")
    Activity findById(@Param("id")Integer id);

    List<Activity> findBySearch(@Param("search") Activity search);

}