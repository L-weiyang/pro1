package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.TakeOff;
import com.example.springboot.service.TakeOffService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/takeoff")
public class TakeOffController {
    @Resource
    TakeOffService takeOffService;


    //    查询所有数据
    @GetMapping("/alldata")
    public Result getData(){

        return Result.success(takeOffService.GetAll());

    }
    /**
     * 新增请假接口
     */
    @PostMapping
    public Result takeOffRegister(@RequestBody TakeOff takeOff){

        return Result.success(takeOffService.takeOffRegister(takeOff));

    }


    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        takeOffService.delete(id);
        return Result.success();
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody TakeOff search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(takeOffService.findPage(search, pageNum, pageSize));
    }


    /**
     * 编辑请假信息接口
     */
    @PostMapping("/edit")
    public Result takeOffEdit(@RequestBody TakeOff takeOff){

        return Result.success(takeOffService.takeOffEdit(takeOff));

    }



}
