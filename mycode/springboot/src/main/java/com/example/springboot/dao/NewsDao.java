package com.example.springboot.dao;

import com.example.springboot.entity.News;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NewsDao extends Mapper<News> {


//  @Select("select * from news where `id` = #{id}")
//  News findById(@Param("id")Integer id);

  List<News> findBySearch(@Param("search") News search);
  //前端
  @Select("select * from news order by time desc limit 8")
  List<News> getNewdata();

  @Select("select * from news where `id` = #{id}")
  News findById(@Param("id")Integer id);

}