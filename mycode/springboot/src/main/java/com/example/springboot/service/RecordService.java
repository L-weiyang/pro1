package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.RecordDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Record;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    @Resource
    private RecordDao recordDao;

    public List<Record> GetAll(){

        return recordDao.selectAll();
    }
    public Record recordRegister(Record record) {
        record.setTime(DateUtil.now());
        recordDao.insertSelective(record);
        return record;
    }


    public Record findById(Integer id) {
        return recordDao.findById(id);
    }




    public void delete(Integer id) {
        recordDao.deleteByPrimaryKey(id);
    }


    public PageInfo<Record> findPage(Record search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Record> all = findByCondition(search,user);

        return PageInfo.of(all);
    }

    public List<Record> findByCondition(Record search,Account user) {
        if (1 == user.getRole()){
            return recordDao.findBySearch(search,null,null);
        } else {
            return recordDao.findBySearch(search, user.getId(), user.getRole());
        }

    }


    public Record recordEdit(Record record) {
        recordDao.updateByPrimaryKeySelective(record);

        return record;
    }

}