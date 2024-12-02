package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.ActivityDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Activity;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    @Resource
    private ActivityDao activityDao;

    public List<Activity> GetAll(){

        return activityDao.selectAll();
    }
    public Activity add(Activity activity) {
        String title = activity.getTitle();
        activity.setTime(DateUtil.now()); //设置当前时间为发布时间
        if (ObjectUtil.isEmpty(title)){
            throw new CustomException(ResultCode.TITLE_ISNULL);
        }
        if (ObjectUtil.isEmpty(activity.getImg())) {
            activity.setImg("http://localhost:9090/files/default.png");
        }

        activityDao.insertSelective(activity);
        return activity;
    }


    public Activity findById(Integer id) {
        return activityDao.findById(id);
    }




    public void delete(Integer id) {
        activityDao.deleteByPrimaryKey(id);
    }


    public PageInfo<Activity> findPage(Activity search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> all = findByCondition(search);

        return PageInfo.of(all);
    }

    public List<Activity> findByCondition(Activity search) {
        return activityDao.findBySearch(search);
    }



    public Activity activityEdit(Activity activity) {

        String activityName = activity.getTitle();
        //     1.先校验标题是不是为空： 如果用户名，为空，抛出异常
        if (ObjectUtil.isEmpty(activityName)){
            throw new CustomException(ResultCode.USERNAME_ISNULL);
        }
        activityDao.updateByPrimaryKeySelective(activity);

        return activity;
    }

}