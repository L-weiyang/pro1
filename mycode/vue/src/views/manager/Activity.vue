<template>
    <div>
        <div style="width: 100%;height:1000px;padding:40px 40px; background-color: white;border-radius: 4px">
            <!--按钮 和 搜索框样式-->
            <div style="margin-bottom: 40px;font-size: 18px;font-weight: 530;">用户管理</div>
            <div style="margin-bottom: 20px; display: flex">
                <div style="flex: 9;text-align: left">
                    <el-input placeholder="请输入查询活动标题" size="small"  v-model="search.title" style="width: 30%; margin-right: 20px"><i slot="suffix" class="el-input__icon el-icon-search"></i></el-input>
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center" @click="load">查询</el-button>
                </div>
                <div style="flex:5;text-align: right">
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center;margin-right: 10px;" @click="add" v-if="user.role === 1">新增</el-button>
                </div>
            </div>
            <!--      表格样式-->
            <el-table :data="tableData" border  stripe style="width: 100%"
                      :cell-style="{ textAlign: 'center' }" :header-cell-style="{ textAlign: 'center' }">
                <el-table-column label="封面">
                    <template v-slot="scope">
                        <el-image :src="scope.row.img" :preview-src-list="[scope.row.img]" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
                    </template>
                </el-table-column>
                <el-table-column prop="title" label="标题" ></el-table-column>
                <el-table-column label="内容">
                    <template v-slot="scope">
                        <el-button size="small" type="primary" @click="initEditorView(scope.row.content)">查看</el-button>
                    </template>
                </el-table-column>
                <el-table-column prop="time" label="发布时间"></el-table-column>
                <el-table-column fixed="right" label="操作" >
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="edit(scope.row)"  v-if="user.role === 1">编辑</el-button>
                        <el-button type="text" size="small" @click="book(scope.row)" v-if="user.role === 2">预约</el-button>
                        <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)" style="margin-left: 10px">
                            <el-button size="small" type="text" slot="reference"  v-if="user.role === 1">删除</el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页样式-->
            <div class="block" style="text-align: right;margin-top: 20px">
                <el-pagination
                        background
                        @current-change="handleCurrentChange"
                        :current-page="pageNum"
                        :page-size="5"
                        layout="total, prev, pager, next"
                        :total="total">
                </el-pagination>
            </div>
        </div>
        <el-dialog title="请填写信息" :visible.sync="dialogVisible" width="40%">
            <el-form :model="form" label-position="right" label-width="100px" style="padding-right: 40px">
                <el-form-item label="活动封面">
                    <el-upload
                            class="avatar-uploader"
                            action="http://localhost:9090/files/upload"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess">
                        <img v-if="form.img" :src="form.img" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="活动标题">
                    <el-input size="small" v-model="form.title" placeholder="请输入活动标题"></el-input>
                </el-form-item>
                <el-form-item prop="content" label="活动内容">
                    <div id="editor"></div>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="small" @click="dialogVisible = false">取 消</el-button>
                <el-button size="small" type="primary" @click="save">保 存</el-button>
            </div>
        </el-dialog>
        <el-dialog title="内容查看" :visible.sync="richTextDialogVisible" :close-on-click-modal="false" destroy-on-close>
            <div class="w-e-text" v-html="this.viewContent"></div>
        </el-dialog>
    </div>
</template>

<script>
import request from "@/utils/request";
import E from 'wangeditor';




export default {

    // 自定义变量
    data() {
        return {
            editor:null,
            viewContent:null,
            form: {},
            dialogVisible: false,
            richTextDialogVisible: false,
            tableData: [],
            selectionData: [],
            pageNum: 1,
            total: 0,
            search: {},
            gradeData: {},
            token: localStorage.getItem("token"),
            user:JSON.parse(localStorage.getItem("user") || {})
        }
    },
    // 页面加载时调用方法
    created() {
        this.load();
    },
    methods: {
        handleClick(row) {
            console.log(row);
        },
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum;
            this.load();
        },

        // 根据ID删除某条数据
        del(id) {
            request.delete("/activity/" + id).then(res => {
                if (res.code === '0') {
                    this.$notify.success('删除成功');
                    this.load();
                } else {
                    this.$notify.error(res.msg);
                }
            });
        },

        // 分页加载表格数据
        load() {
            request.post("/activity/page?pageNum=" + this.pageNum, this.search).then(res => {
                if (res.code === '0') {
                    this.tableData = res.data.list;
                    this.total = res.data.total;
                } else {
                    this.$notify.error(res.msg);
                }
            });
        },
        // 点击编辑，打开模态框，回显当前对象的信息
        edit(row) {
            this.form = JSON.parse(JSON.stringify(row));
            this.dialogVisible = true;
            this.initWangEditor(row.content || ''); //回显编辑器内容，获取当前对象的content作为参数执行初始化编辑器即可。

        },
        // 点击新增按钮点击事件调用
        add() {
            this.form = {};
            this.$nextTick(() => {
                if (this.form.img != null){
                    this.$refs.tx.clearFiles();
                }
            });
            this.dialogVisible = true;
            this.initWangEditor(''); //每次点击新增按钮，都清空编辑器文本内容

        },
        // 点击保存，新增的时候id是空的，判断有没有id，如果没有id的话是新增，如果有id的话 是编辑
        save() {
            this.form.content = this.editor.txt.html();
            if (!this.form.id) { //如果没有id 走新增接口
                this.form.role = 1;
                request.post("/activity", this.form).then(res => { //post请求把form对象传到后端，后端写逻辑把form存到数据库里
                    if (res.code === '0') { //如果接口调用成功，则把模态框关闭，重新加载一下数据库的数据
                        this.$notify.success("新增成功");
                        this.dialogVisible = false;
                        this.load();
                    } else {
                        this.$notify.error(res.msg); // 如果不成功，返回报错信息
                    }
                })
            } else {
                request.post("/activity/edit", this.form).then(res => {
                    if (res.code === '0') {
                        this.$notify.success("修改成功");
                        this.dialogVisible = false;
                        this.load();
                    } else {
                        this.$notify.error(res.msg); // 如果不成功，返回报错信息
                    }
                })
            }
        },

        handleAvatarSuccess(res) {
            this.form.img = res.data;
        },


        initEditorView(content) {
            this.viewContent = content;
            this.richTextDialogVisible = true;
        },

        // 初始化编辑器
        initWangEditor(content) { //用户输入的文本作为参数传进函数。
            this.$nextTick(() => { //1.dom元素加载好后，先判断editor是否已经完成初始化，如果已经完成初始化，那么把editor销毁，清空。
                if (this.editor){
                    this.editor.destroy();
                    this.editor = null;
                }
                this.editor = new E('#editor') //2.新建一个editor
                this.editor.config.placeholder = '请输入内容' // 配置编辑器默认展示的文本
                this.editor.config.uploadFileName = 'file' //配置编辑器文件上传的名称
                this.editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'  //配置编辑器文件上传的接口
                this.editor.create() //执行创建编辑器
                setTimeout(() => { //延时函数，这里没有设置延时时间，即为实时获取编辑器的文本给到this.editor.txt.html()。
                    this.editor.txt.html(content)
                })

            })
        },

        book(row){
            this.form.activityId = row.id;
            this.form.userId = this.user.id;
            this.form.role = this.user.role;
            this.form.userName = this.user.userName;
            request.post("/record", this.form).then(res => { //post请求把form对象传到后端，后端写逻辑把form存到数据库里
                if (res.code === '0') { //如果接口调用成功，则把模态框关闭，重新加载一下数据库的数据
                    this.$notify.success("预约成功");
                    this.dialogVisible = false;
                    this.load();
                } else {
                    this.$notify.error(res.msg); // 如果不成功，返回报错信息
                }
            })

        },

    },

}
</script>