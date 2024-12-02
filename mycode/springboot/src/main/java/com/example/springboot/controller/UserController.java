package com.example.springboot.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.Result;
import com.example.springboot.common.ResultCode;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.User;
import com.example.springboot.exception.CustomException;
import com.example.springboot.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;



    //    查询所有数据
    @GetMapping("/alldata")
    public Result getData(){

        return Result.success(userService.GetAll());

    }
    /**
     * 新增用户接口
     */
    @PostMapping
    public Result userRegister(@RequestBody User user){

        return Result.success(userService.userRegister(user));


    }


    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.delete(id);
        return Result.success();
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody User search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(userService.findPage(search, pageNum, pageSize));
    }


    /**
     * 编辑用户信息接口
     */
    @PostMapping("/edit")
    public Result userEdit(@RequestBody User user){

        return Result.success(userService.userEdit(user));

    }


    /**
     * 修改角色的接口

     */
    @PostMapping("/changeRole")
    public Result userRole(@RequestBody User user)
    {
        return Result.success(userService.userRole(user));
    }

    @PostMapping("/upload")

    public Result upload(MultipartFile file) throws IOException{
        List<User> infoList  = ExcelUtil.getReader(file.getInputStream()).readAll(User.class);
        if (!CollectionUtil.isEmpty(infoList)){
            for (User info : infoList){
                try{
                    userService.userRegister(info);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return Result.success();

    }
    @GetMapping("/export")

    public void export(HttpServletResponse response) throws IOException{
//        1、拿到所有用户数据
        List<User> all  = userService.GetAll();

        List<Map<String,Object>> list  = new ArrayList<>(all.size());
//        2、校验是否有数据，最好用枚举;如果有数据，则会进行下面的for循环
        if (CollectionUtil.isEmpty(all)){
            throw new CustomException(ResultCode.INFO_ISNULL);}
        for (User user :all)
        {
            Map<String,Object> row = new LinkedHashMap<>();//格式 表头，值
            row.put("用户名",user.getUserName());
            row.put("性别",user.getGender());
            row.put("年龄",user.getAge());
            row.put("电话",user.getPhone());
            row.put("角色标识",user.getRole());
            row.put("班级Id",user.getGradeId());
            row.put("所属班级",user.getGradeName());
            list.add(row);
        }
        //写数据
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list,true);//把key作为表头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=userInfoExcel.xlsx");//filename为导出的表名
        ServletOutputStream out = response.getOutputStream();//才开写数据
        writer.flush(out,true);
        writer.close();//关闭文件
        IoUtil.close(System.out);


    }
    /**
     * 批量删除
     * */
    @PostMapping("/delBatch")
        public Result delBatch(@RequestBody List<User> list){//如果前端传的是集合，则我们也要用集合来接收
            for (User user : list){
                userService.delete(user.getId());
            }
            return Result.success();
        }


    }


