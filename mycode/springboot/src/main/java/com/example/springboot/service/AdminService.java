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
public class AdminService {
    @Resource
    UserDao userDao;
    public Admin findById(Integer id) {

        return adminDao.findById(id);
    }

    @Resource
    private AdminDao adminDao;

    public List<Admin> GetAll(){

        return adminDao.selectAll();
    }

    public Admin adminLogin(Admin admin) {

//        1.拿到用户传来的用户名，先到数据库查看这个用户名是否存在,如果不存在抛出异常：“用户不存在”
        Admin dbAdmin = adminDao.findByName(admin.getUserName());
        if (ObjectUtil.isEmpty(dbAdmin)){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
//        2.如果用户存在，则那用户输入的密码跟数据库找到的用户的密码比对，密码输入正确，允许登录
        String password = admin.getPassword(); //用户输入的密码
        String dbPassword = dbAdmin.getPassword(); // 数据库找到用户的密码

        if (!password.equalsIgnoreCase(dbPassword)){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }

        return dbAdmin;

    }
    public Admin adminRegister(Admin admin) {

        String userName = admin.getUserName();
//     1.先校验用户名是不是为空： 如果用户名，为空，抛出异常
        if (ObjectUtil.isEmpty(userName)){
            throw new CustomException(ResultCode.USERNAME_ISNULL);
        }
//     2.校验用户名的唯一性：
        Admin dbAdmin = adminDao.findByName(userName);
        if (ObjectUtil.isNotEmpty(dbAdmin)) {
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(admin.getAvatar())) {
            admin.setAvatar("http://localhost:9090/files/1726301457230-0151de5976f221a8012193a3803206.jpg@2o.jpg");
        }
        if (ObjectUtil.isEmpty(admin.getPassword())) {

            admin.setPassword("123456");
        }
        adminDao.insertSelective(admin);
        return admin;
    }


    public void delete(Integer id) {
        adminDao.deleteByPrimaryKey(id);
    }

    public Object adminEdit(Admin admin) {


            String userName = admin.getUserName();
//     1.先校验用户名是不是为空： 如果用户名，为空，抛出异常
            if (ObjectUtil.isEmpty(userName)){
                throw new CustomException(ResultCode.USERNAME_ISNULL);
            }

            adminDao.updateByPrimaryKeySelective(admin);

            return admin;
        }

    public PageInfo<Admin> findPage(Admin search, Integer pageNum, Integer pageSize) {
        // 分页查询和模糊查询的方法
//1、获取当前用户，判断当前用户时候登录状态
            Account user = JwtTokenUtils.getCurrentUser();
            if (ObjectUtil.isEmpty(user)) {
                throw new CustomException(ResultCode.USER_NOT_LOGIN);
            }
            //2、开启分页，开启分页这句代码，一定在操作数据的方法
            // 开启分页
            PageHelper.startPage(pageNum, pageSize);
            List<Admin> all = findByCondition(search);

            return PageInfo.of(all);
        }

// 根据条件查询的方法
        public List<Admin> findByCondition(Admin search) {
            return adminDao.findBySearch(search);
        }

    public Object adminRole(Admin admin) {
        //1、执行删除操作，在管理表删除这个用户
        Integer id = admin.getId();//拿到用户id
        delete(id);

        //2、把这个管理员信息新增到用户表
        User user = new User();
        user.setAvatar(admin.getAvatar());
        user.setPassword(admin.getPassword());
        user.setRole(2);
        user.setUserName(admin.getUserName());
        user.setGender(admin.getGender());
        user.setAge(admin.getAge());
        user.setPhone(admin.getPhone());

        userDao.insertSelective(user);

        return admin;
    }
}