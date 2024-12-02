package com.example.springboot.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.AdminDao;
import com.example.springboot.dao.UserDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.User;
import com.example.springboot.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private AdminDao adminDao;

    public List<User> GetAll(){

        return userDao.selectAll();
    }
    public User userRegister(User user) {
// 插入数据库前做一下用户名唯一性校验，
        String userName = user.getUserName();
// 先拿到用户输入的用户名
        User dbUser = userDao.findByName(userName);
        if (ObjectUtil.isNotEmpty(dbUser)){
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getAvatar())) {
            user.setAvatar("http://localhost:9090/files/1726301457230-0151de5976f221a8012193a3803206.jpg@2o.jpg");
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {

            user.setPassword("123456");
        }
        userDao.insertSelective(user);
        return user;
    }

    public User findById(Integer id) {
        return userDao.findById(id);
    }

    public User userLogin(User user) {

//        1.拿到用户传来的用户名，先到数据库查看这个用户名是否存在,如果不存在抛出异常：“用户不存在”
        User dbUser = userDao.findByName(user.getUserName());
        if (ObjectUtil.isEmpty(dbUser)){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
//        2.如果用户存在，则那用户输入的密码跟数据库找到的用户的密码比对，密码输入正确，允许登录
        String password = user.getPassword(); //用户输入的密码
        String dbPassword = dbUser.getPassword(); // 数据库找到用户的密码

        if (!password.equalsIgnoreCase(dbPassword)){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }

        return dbUser;

    }


    public void delete(Integer id) {
        userDao.deleteByPrimaryKey(id);
    }


    public PageInfo<User> findPage(User search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<User> all = findByCondition(search);

        return PageInfo.of(all);
    }

    public List<User> findByCondition(User search) {
        return userDao.findBySearch(search);
    }



    public User userEdit(User user) {

        String userName = user.getUserName();
//     1.先校验用户名是不是为空： 如果用户名，为空，抛出异常
        if (ObjectUtil.isEmpty(userName)){
            throw new CustomException(ResultCode.USERNAME_ISNULL);
        }
        userDao.updateByPrimaryKeySelective(user);

        return user;
    }


    public Object userRole(User user) {
        //1、执行删除操作，在用户表删除这个用户
        Integer id = user.getId();//拿到用户id
        delete(id);

        //2、把这个用户信息新增到管理表
        Admin admin = new Admin();
        admin.setAvatar(user.getAvatar());
        admin.setPassword(user.getPassword());
        admin.setRole(1);
        admin.setUserName(user.getUserName());
        admin.setGender(user.getGender());
        admin.setAge(user.getAge());
        admin.setPhone(user.getPhone());

        adminDao.insertSelective(admin);

        return user;
    }
}
