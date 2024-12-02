package com.example.springboot.dao;

import com.example.springboot.entity.Record;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RecordDao extends Mapper<Record> {

    @Select("select * from record where `id` = #{id}")
    Record findById(@Param("id")Integer id);

    List<Record> findBySearch(@Param("search") Record search,@Param("userId") Integer userId,@Param("role") Integer role);

}