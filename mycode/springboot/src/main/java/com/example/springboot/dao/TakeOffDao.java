package com.example.springboot.dao;

import com.example.springboot.entity.Record;
import com.example.springboot.entity.TakeOff;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TakeOffDao extends Mapper<TakeOff> {



    @Select("select * from takeoff where `id` = #{id}")
    TakeOff findById(@Param("id")Integer id);

    List<TakeOff> findBySearch(@Param("search") TakeOff search, @Param("userId") Integer userId, @Param("role") Integer role);


}
