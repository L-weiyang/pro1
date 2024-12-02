package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Comment;
import com.example.springboot.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    CommentService commentService;


    //    查询所有数据
    @GetMapping("/{newsId}")
    public Result getData(@PathVariable Integer newsId){
        return Result.success(commentService.findAll(newsId));
    }

    /**
     * 新增评论接口
     */
    @PostMapping
    public Result commentRegister(@RequestBody Comment comment){

        return Result.success(commentService.add(comment));

    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        commentService.delete(id);
        return Result.success();
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Comment search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(commentService.findPage(search, pageNum, pageSize));
    }





}