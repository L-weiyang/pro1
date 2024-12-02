package com.example.springboot.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 评论
 */
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 评论内容 */
    @Column(name = "content")
    private String content;
    /** 评论时间 */
    @Column(name = "time")
    private String time;
    /** 新闻标题 */
    @Column(name = "title")
    private String title;
    /** 评论人id */
    @Column(name = "userId")
    private Integer userId;
    /** 评论人角色 */
    @Column(name = "role")
    private Integer role;
    /** 新闻id */
    @Column(name = "newsId")
    private Integer newsId;
    /** 评论ID */
    @Column(name = "commentId")
    private Integer commentId;
    @Transient
    private String userName; // 用于展示评论人用户名
    @Transient
    private List<Comment> children; // 评论的子评论集合，用于前端渲染子评论
    @Transient
    private String avatar; // 用于展示评论人头像

    @Transient
    private String reply;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;

    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}