package com.example.springboot.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.AdminDao;
import com.example.springboot.dao.CommentDao;
import com.example.springboot.dao.UserDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Comment;
import com.example.springboot.entity.User;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Resource
    private CommentDao commentDao;

    @Resource
    private UserDao userDao;

    @Resource
    private AdminDao adminDao;


    public Comment add(Comment comment) {
        comment.setTime(DateUtil.now());//设评论时间为当前时间
        commentDao.insertSelective(comment);
        return comment;
    }


    public Comment findById(Integer id) {
        return commentDao.findById(id);
    }


    public void delete(Integer id) {
        commentDao.deleteByPrimaryKey(id);
    }

    public List<Comment> findAll(Integer newsId) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        List<Comment> list = commentDao.findByNewsIdAndCommentId(newsId, 0);
        for (Comment comment : list) {
            setData(comment);
            List<Comment> children = commentDao.findByNewsIdAndCommentId(newsId, comment.getId());
            if (CollectionUtil.isNotEmpty(children)) {
                for (Comment child : children) {
                    setData(child);
                }
                comment.setChildren(children);
            }
        }
        return list;
    }

    private void setData(Comment comment) {
        if (1 == comment.getRole()) {
            Admin admin = adminDao.selectByPrimaryKey(comment.getUserId());
            comment.setUserName(admin.getUserName());
            comment.setAvatar(admin.getAvatar());
        }
        if (2 == comment.getRole()) {
            User user = userDao.selectByPrimaryKey(comment.getUserId());
            comment.setUserName(user.getUserName());
            comment.setAvatar(user.getAvatar());
        }
    }

    public PageInfo<Comment> findPage(Comment search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> all = findByCondition(search,user);
        for (Comment comment : all) {
            Integer role = comment.getRole();
            if (1 == role){
                Admin admin = adminDao.selectByPrimaryKey(comment.getUserId());
                comment.setUserName(admin.getUserName());
            }
            if (2 == role){
                User dbuser = userDao.selectByPrimaryKey(comment.getUserId());
                comment.setUserName(dbuser.getUserName());
            }

        }

        return PageInfo.of(all);
    }

    public List<Comment> findByCondition(Comment search,Account user) {
        if (1 == user.getRole()){
            return commentDao.findBySearch(search,null,null);
        } else {
            return commentDao.findBySearch(search, user.getId(), user.getRole());
        }

    }


}