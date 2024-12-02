package com.example.springboot.dao;

import com.example.springboot.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CommentDao extends Mapper<Comment> {

    @Select("select * from comment where `id` = #{id}")
    Comment findById(@Param("id")Integer id);

    List<Comment> findBySearch(@Param("search") Comment search,@Param("userId") Integer userId,@Param("role") Integer role);
    @Select("select * from comment where newsId = #{newsId} and commentId = #{commentId}")
    List<Comment> findByNewsIdAndCommentId(@Param("newsId") Integer newsId, @Param("commentId") Integer commentId);

}