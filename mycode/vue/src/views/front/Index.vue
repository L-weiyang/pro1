<template>
    <div class="main-content">
        <!--    轮播图-->
        <div class="main-line">
            <div class="main-line-1">
                <div class="main-img">
                    <el-carousel height="80vh" :interval="2000">
                        <el-carousel-item v-for="(item,index) in carousel_1_1" :key="index">
                            <img :src="item" alt=""  style="height: 800px">
                        </el-carousel-item>
                    </el-carousel>
                </div>
            </div>
        </div>
        <div style="padding: 0 80px;">

            <!--    首页导航文字-->
            <div class="index-advertising">
                <div class="index-advertising-box">
                    <div class="advertising-box-1">
                        <img src="@/assets/news.png" alt="" class="advertising-img">
                    </div>
                    <div>
                        <div class="advertising-title">新闻中心</div>
                        <div class="title-ad">看校园新闻</div>
                    </div>
                </div>
                <div class="index-advertising-box">
                    <div class="advertising-box-1">
                        <img src="@/assets/activity.png" alt="" class="advertising-img">
                    </div>
                    <div>
                        <div class="advertising-title">活动申请</div>
                        <div class="title-ad">预约社团活动</div>
                    </div>
                </div>
                <div class="index-advertising-box">
                    <div class="advertising-box-1">
                        <img src="@/assets/askleave.png" alt="" class="advertising-img">
                    </div>
                    <div>
                        <div class="advertising-title">请假申请</div>
                        <div class="title-ad">申请请假</div>
                    </div>
                </div>
                <div class="index-advertising-box">
                    <div class="advertising-box-1">
                        <img src="@/assets/leave.png" alt="" class="advertising-img">
                    </div>
                    <div>
                        <div class="advertising-title">申请记录</div>
                        <div class="title-ad">活动/请假申请记录</div>
                    </div>
                </div>
            </div>

            <!--    热门新闻展示-->
            <div>
                <!--      1 文字部分-->
                <div style="margin-top: 60px;margin-bottom: 60px">
                    <div style="text-align: left;margin:30px 0;display: flex;align-items: center">
                        <div style=" font-size: 28px;font-weight:550;margin-bottom: 20px; flex: 30">最新校园资讯</div>
                        <el-button round style="flex: 1">更多校园资讯</el-button>
                    </div>
                </div>
                <!--   3热门新闻展示-->
                <div>
                    <el-row>
                        <el-col :span="6" v-for="item in newsData" style="display: flex;justify-content: center;">
                            <el-card :body-style="{ padding: '0px' }" style="margin-bottom: 20px;width: 300px" >
                                <img :src="item.img" class="image">
                                <div style="padding: 14px;">
                                    <span>{{item.title}}</span>
                                    <div class="bottom clearfix">
                                        <time class="time">{{ item.time }}</time>
                                        <el-button type="text" class="button" @click="$router.push('/front/newsDetail?id=' + item.id)">点击查看</el-button>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                    </el-row>
                </div>
            </div>
        </div>


        <!--      网站介绍-->
        <div style="text-align: center;margin:50px auto" >
            <div style="color: #999797; font-size: 14px;margin-bottom: 20px">欢迎来到我们校园官网，这里提供快捷的请假或活动申请功能，同时你还可以在本网站浏览校园新闻。 </div>
            <div style="color: #999797; font-size: 14px;margin-bottom: 20px">在这里，您可以轻松地浏览各种校园动态，在线快捷参与校园活动。</div>
        </div>
    </div>
</template>

<script>
import request from "@/utils/request";

export default {

    data() {
        return {
            carousel_1_1: [
                require('@/assets/banner1.jpg'),
                require('@/assets/banner2.jpg'),
                require('@/assets/banner3.jpg'),
                require('@/assets/banner4.jpg'),
                require('@/assets/banner5.jpg'),
            ],
            newsData: [],
        }
    },
    // 监听路由
    watch: {
        $route(to, from) {
            if(to.path === '/front/index') {
                location.reload()
            }
        }
    },
    mounted() {
        this.loadNews();

    },
    // methods：本页面所有的点击事件或者其他函数定义区
    methods: {
        //加载热门新闻
        loadNews() {
            request.get("/news/newdata").then(res => {
                if (res.code === '0') {
                    this.newsData = res.data
                } else {
                    this.$notify.error(res.msg)
                }
            })
        },


    }
}
</script>