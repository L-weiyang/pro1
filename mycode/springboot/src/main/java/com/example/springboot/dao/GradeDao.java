package com.example.springboot.dao;

import com.example.springboot.entity.Grade;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface GradeDao extends Mapper<Grade> {


  //    根据用户名查询，查询跟参数相同的用户名 返回一个用户
  @Select("select * from grade where `gradeName` = #{gradeName}")
  Grade findByGradename(@Param("gradeName") String gradeName);

  @Select("select * from grade where `id` = #{id}")
  Grade findById(@Param("id")Integer id);

  List<Grade> findBySearch(@Param("search") Grade search);

}
