<template>
    <div class="main-content">
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



        <!--    热门商品展示-->
        <div style="background-color: rgba(180,180,238,0.07);padding: 20px 260px">
            <!--      1 文字部分-->
            <div style="margin-top: 60px;margin-bottom: 60px">
                <div style="text-align: center;margin:30px auto">
                    <div style="color: #2c2b2b; font-size: 28px;font-weight:550;margin-bottom: 20px; font-family: 'Dubai Medium'">新闻中心</div>
                </div>
            </div>
            <!--   3热门商品展示-->
            <div >
                <el-row >
                    <el-col :span="6" v-for="obj in goodsData" style="margin-bottom: 10px;"  >
                        <el-card :body-style="{ padding: '0px' }" style="height: 400px;width: 95%;margin: auto" >
                            <img :src="obj.img" alt="" style="width: 100%;height: 300px;" class="image" @click="$router.push('/front/detail?id=' + obj.id)">
                            <div style="padding: 14px; text-align: center">
                                <div class="overflowShow" style="font-weight: 550;font-size: 16px; color:#5b5a5a;letter-spacing: 1px;margin-bottom: 24px">{{obj.name}}</div>
                                <div class="bottom clearfix">
                                    <span class="time">￥{{obj.price}}</span>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </div>
        </div>

        <!--      商城介绍-->
        <div style="text-align: center;margin:50px auto" >
            <div style="color: #666666; font-size: 30px;margin-bottom: 30px">海量商品 品质生活</div>
            <div style="color: #747070; font-size: 18px;margin-bottom: 20px">全新商城 为您精心打造</div>
            <div style="color: #999797; font-size: 14px;margin-bottom: 20px">欢迎来到我们的商城！我们提供高质量的产品和优质的服务，让您的购物体验更加愉快和舒适。 </div>
            <div style="color: #999797; font-size: 14px;margin-bottom: 20px">在这里，您可以轻松地浏览各种商品，并享受优惠的价格和促销活动。</div>
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
            activeName: '',
            typeData: [],
            goodsData: [],
            advertisingData: [],
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

    },
    // methods：本页面所有的点击事件或者其他函数定义区
    methods: {
        //加载热门商品
        load() {
            request.get("/goods/hotgoods").then(res => {
                if (res.code === '0') {
                    this.goodsData = res.data
                } else {
                    this.$notify.error(res.msg)
                }
            })
        },
        //加载广告位
        loadAdvertising() {
            request.get("/advertising").then(res => {
                if (res.code === '0') {
                    this.advertisingData = res.data
                } else {
                    this.$notify.error(res.msg)
                }
            })
        },
        //  加载轮播图
        loadBanner(){
            request.get("/banner").then(res => {
                if (res.code === '0'){
                    this.carousel_1_1 = res.data;
                    console.log(this.carousel_1_1)
                } else {
                    this.$notify.error(res.msg);
                }
            })
        },
        handleClick(tab) {
            this.load(tab.name)
        },
    }
}
</script>