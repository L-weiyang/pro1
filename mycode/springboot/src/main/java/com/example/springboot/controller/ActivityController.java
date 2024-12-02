package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Activity;
import com.example.springboot.service.ActivityService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Resource
    ActivityService activityService;


    //    查询所有数据
    @GetMapping("/alldata")
    public Result getData(){

        return Result.success(activityService.GetAll());

    }
    /**
     * 新增新闻接口
     */
    @PostMapping
    public Result activityRegister(@RequestBody Activity activity){

        return Result.success(activityService.add(activity));

    }


    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        activityService.delete(id);
        return Result.success();
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Activity search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(activityService.findPage(search, pageNum, pageSize));
    }


    /**
     * 编辑新闻信息接口
     */
    @PostMapping("/edit")
    public Result activityEdit(@RequestBody Activity activity){

        return Result.success(activityService.activityEdit(activity));

    }



}