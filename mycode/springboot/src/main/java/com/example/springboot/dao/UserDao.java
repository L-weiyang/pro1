package com.example.springboot.dao;


import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserDao extends Mapper<User> {

  //    根据用户名查询，查询跟参数相同的用户名 返回一个用户
  @Select("select * from user where `userName` = #{userName}")
  User findByName(@Param("userName") String userName);
  @Select("select * from user where `id` = #{id}")
  User findById(@Param("id")Integer id);
  List<User> findBySearch(@Param("search") User search);
}