package com.example.springboot.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.TakeOffDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.TakeOff;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakeOffService {

    @Resource
    private TakeOffDao takeOffDao;

    public List<TakeOff> GetAll(){

        return takeOffDao.selectAll();
    }
    public TakeOff takeOffRegister(TakeOff takeOff) {
        takeOffDao.insertSelective(takeOff);
        return takeOff;
    }


    public TakeOff findById(Integer id) {
        return takeOffDao.findById(id);
    }




    public void delete(Integer id) {
        takeOffDao.deleteByPrimaryKey(id);
    }


    public PageInfo<TakeOff> findPage(TakeOff search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<TakeOff> all = findByCondition(search,user);

        return PageInfo.of(all);
    }

    public List<TakeOff> findByCondition(TakeOff search,Account user) {
        if (1 == user.getRole()){
            return takeOffDao.findBySearch(search,null,null);
        } else {
            return takeOffDao.findBySearch(search, user.getId(), user.getRole());
        }
    }



    public TakeOff takeOffEdit(TakeOff takeOff) {
        takeOffDao.updateByPrimaryKeySelective(takeOff);
        return takeOff;
    }

}
