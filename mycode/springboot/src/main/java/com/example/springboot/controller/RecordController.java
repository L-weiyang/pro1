package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Record;
import com.example.springboot.service.RecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/record")
public class RecordController {
    @Resource
    RecordService recordService;


    //    查询所有数据
    @GetMapping("/alldata")
    public Result getData(){

        return Result.success(recordService.GetAll());

    }

    /**
     * 新增预约记录接口
     */
    @PostMapping
    public Result recordRegister(@RequestBody Record record){

        return Result.success(recordService.recordRegister(record));

    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        recordService.delete(id);
        return Result.success();
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Record search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(recordService.findPage(search, pageNum, pageSize));
    }

    /**
     * 编辑预约记录信息接口
     */
    @PostMapping("/edit")
    public Result recordEdit(@RequestBody Record record){

        return Result.success(recordService.recordEdit(record));
    }



}
