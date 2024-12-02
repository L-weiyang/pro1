package com.example.springboot.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.NewsDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.News;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

import java.util.List;

import static cn.hutool.core.date.DateUnit.*;

@Service
public class NewsService {

    @Resource
    private NewsDao newsDao;

    public List<News> GetAll(){

        return newsDao.selectAll();
    }
    public News add(News news) {
        String title = news.getTitle();
        news.setTime(DateUtil.now());
        if (ObjectUtil.isEmpty(title)){
            throw new CustomException(ResultCode.TITLE_ISNULL);
        }
        if (ObjectUtil.isEmpty(news.getImg())) {
            news.setImg("http://localhost:9090/files/1726307394800-0151de5976f221a8012193a3803206.jpg@2o.jpg");
        }
        newsDao.insertSelective(news);
        return news;
    }


    public News findById(Integer id) {
        return newsDao.findById(id);
    }




    public void delete(Integer id) {
        newsDao.deleteByPrimaryKey(id);
    }


    public PageInfo<News> findPage(News search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<News> all = findByCondition(search);

        return PageInfo.of(all);
    }

    public List<News> findByCondition(News search) {
        return newsDao.findBySearch(search);
    }



    public News newsEdit(News news) {

        String newsName = news.getTitle();
        //     1.先校验标题是不是为空： 如果用户名，为空，抛出异常
        if (ObjectUtil.isEmpty(newsName)){
            throw new CustomException(ResultCode.TITLE_ISNULL);
        }
        newsDao.updateByPrimaryKeySelective(news);

        return news;
    }
    //前端
    public List<News> GetNewData() {
        return newsDao.getNewdata();
    }

    public News getById(Integer id) {
        return newsDao.findById(id);
    }

}