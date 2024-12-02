<template>
    <div>
        <div class="main-content">
            <div class="detail-container">
                <div class="detail-title">详情信息</div>
                <div class="detail-content">
                    <div class="detail-richtext">
                        <div class="detail-richtext-title">{{obj.title}}</div>
                        <div class="detail-richtext-subtitle">
                            <span style="padding-left: 10px">发布时间：{{obj.time}}</span>
                        </div>
                        <div class="detail-richtext-content w-e-text" v-html="obj.content"></div>
                    </div>

                </div>
                <!--        评论样式-->
                <div class="detail-title">评论信息</div>
                <!--        首条评论框-->
                <div class="detail-comment">
                    <div style="margin-bottom: 50px">
                        <el-input type="textarea" placeholder="请输入内容" v-model="content" rows="4"></el-input>
                        <div style="margin-top: 10px; text-align: right">
                            <el-button type="primary" @click="submit(0,content)">提 交</el-button>
                        </div>
                    </div>
                    <!--          评论列表-->
                    <div>
                        <el-row v-for="item in comments" style="margin-bottom: 10px">
                            <el-col :span="3" style="display: flex; align-items: center;">
                                <img :src="item.avatar" style="width: 50px; height: 50px; border-radius: 50%;">
                                <div style="margin-left: 10px; flex: 1" class="overflowShow">{{item.userName}}</div>
                            </el-col>
                            <el-col :span="21">
                                <el-row>
                                    <el-col :span="18" style="height: 50px; line-height: 50px">{{item.content}}</el-col>
                                    <el-col :span="6" style="height: 50px; line-height: 50px; text-align: right">{{item.time}}</el-col>
                                </el-row>
                                <el-row :span="3" style="margin-top: 20px" v-for="child in item.children">
                                    <el-col :span="5" style="display: flex; align-items: center;">
                                        <img :src="child.avatar" style="width: 50px; height: 50px; border-radius: 50%;">
                                        <div style="margin-left: 10px; flex: 1" class="overflowShow">{{child.userName}} 回复：</div>
                                    </el-col>
                                    <el-col :span="14" style="height: 50px; line-height: 50px">{{child.content}}</el-col>
                                    <el-col :span="5" style="height: 50px; line-height: 50px; text-align: right">{{child.time}}</el-col>
                                </el-row>

                                <el-row :span="3" style="margin-top: 20px">
                                    <el-col :span="15">
                                        <el-input v-model="item.reply"></el-input>
                                    </el-col>
                                    <el-col :span="4" style="margin-left: 10px">
                                        <el-button type="primary" @click="submit(item.id, item.reply)">回复</el-button>
                                    </el-col>
                                </el-row>
                            </el-col>
                        </el-row>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<script>
import request from "@/utils/request";

export default {

    data() {
        let objId = this.$route.query.id
        return {
            user: JSON.parse(localStorage.getItem('user') || {}),
            objId: objId,
            obj: {},
            content:'',
            title:'',
            comments:[],
        }
    },
    mounted() {
        this.load();
        this.loadComment();
    },
    // methods：本页面所有的点击事件或者其他函数定义区
    methods: {
        load() {
            request.get('/news/' + this.objId).then(res => {
                this.obj = res.data
            })
        },

        loadComment() {
            request.get('/comment/'+ this.objId).then(res => {
                if (res.code === '0') {
                    this.comments = res.data
                } else {
                    this.$notify.error(res.msg)
                }
            })
        },
        submit(commentId, content) {
            // 1.校验评论是否为空，如果为空return
            if (!content) {
                this.$notify.warning("请输入内容")
                return
            }
            // 2.定义一个临时对象，把我们想要传的值放到该data对象里
            let data = {content: content, userId: this.user.id, role: this.user.role, commentId: commentId, newsId: this.objId,title: this.obj.title}
            request.post("/comment", data).then(res => {
                if (res.code ==='0') { //3.调接口，把data传到后端
                    this.$notify.success("评论成功")
                    this.loadComment(); //4.评论成功后，重新加载评论数据
                    this.content = "" //5.清空输入框
                } else {
                    this.$notify.error(res.msg)
                }
            })
        }


    }
}
</script>