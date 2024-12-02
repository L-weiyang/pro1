<template>
    <div>
        <!--头部-->
        <div class="header">
            <div class="header-left">
                <img src="@/assets/logo.png" alt="">
                <div class="title">7天毕设速成教程</div>
            </div>

            <div class="header-center">
                <div class="header-nav">
                    <el-menu :default-active="$route.path" mode="horizontal" router>
                        <el-menu-item index="/front/index">首页</el-menu-item>
                        <el-menu-item index="/front/news">新闻中心</el-menu-item>
                        <el-menu-item index="/front/activity">活动预约</el-menu-item>
                        <el-menu-item index="/front/record">预约记录</el-menu-item>
                    </el-menu>
                </div>
            </div>

            <div class="header-right">
                <!--        搜索框-->
                <div style="width: 400px; margin-right: 30px;position: relative">
                    <el-input size="medium" style="width: 300px;" v-model="name" clearable placeholder="请输入商品名称搜索"> </el-input>
                    <i class="el-icon-search" style="position: absolute;top: 44px;left: 368px;" ></i>
                </div>
                <!--        登录和头像-->
                <div v-if="!user.username"  @click="$router.push('/login')" style="display: flex;">
                    <div style="width: 50px;height: 50px;border-radius: 40px;border: 1px solid #c8c9cb;margin: auto 0;position: relative" >
                        <i class="el-icon-user" style="position: relative;bottom:25px;right: 16px;"></i>
                    </div>
                </div>
                <div v-else>
                    <el-dropdown>
                        <div class="header-dropdown">
                            <img :src="user.avatar" alt="">
                            <div style="margin-left: 10px">
                                <span>{{ user.username }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
                            </div>
                        </div>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item>
                                <div @click="goToPerson()">个人中心</div>
                            </el-dropdown-item>
                            <el-dropdown-item>
                                <div style="text-decoration: none" @click="logout">退出</div>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </div>

        </div>
        <!--主体-->
        <div>
            <router-view ref="child" />
        </div>

    </div>

</template>

<script>
import request from "@/utils/request";


export default {
    name: "FrontLayout",

    data () {
        return {
            top: '',
            user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
            name: ''
        }
    },

    mounted() {

    },

    methods: {
        goToPerson() {
            if (this.user.role === 1) {
                this.$router.push("/end/pAdmin")
            }
            if (this.user.role === 2) {
                this.$router.push("/end/pUser")
            }

        },
        // 退出登录
        logout() {
            localStorage.removeItem("user");
            this.$router.push("/login");
        },

    }

}
</script>

<style scoped>
@import "@/assets/front.css";
@import "@/assets/view.css";

</style>