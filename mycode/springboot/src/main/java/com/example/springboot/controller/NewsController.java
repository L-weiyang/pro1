package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.News;
import com.example.springboot.service.NewsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    NewsService newsService;



    //    查询所有数据
    @GetMapping("/alldata")
    public Result getData(){

        return Result.success(newsService.GetAll());

    }
    //前端
    /**
     * 查询最新发布的8条数据
     */
    @GetMapping("/newdata")
    public Result getNewData(){

        return Result.success(newsService.GetNewData());
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){

        return Result.success(newsService.getById(id));
    }


    /**
     * 新增新闻接口
     */
    @PostMapping
    public Result newsRegister(@RequestBody News news){

        return Result.success(newsService.add(news));


    }


    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        newsService.delete(id);
        return Result.success();
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody News search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(newsService.findPage(search, pageNum, pageSize));
    }


    /**
     * 编辑新闻信息接口
     */
    @PostMapping("/edit")
    public Result newsEdit(@RequestBody News news){

        return Result.success(newsService.newsEdit(news));

    }







    }


