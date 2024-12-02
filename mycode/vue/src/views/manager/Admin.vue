<template>
    <div>
        <div style="width: 100%;height:1000px;padding:40px 40px; background-color: white;border-radius: 4px">
            <div style="margin-bottom: 40px;font-size: 18px;font-weight: 530;">管理员管理</div>
            <div style="margin-bottom: 20px; display: flex">
                <div style="flex: 10;text-align: left">
                    <el-input placeholder="请输入用户名" size="small"  v-model="search.userName" style="width: 20%; margin-right: 20px"><i slot="suffix" class="el-input__icon el-icon-search"></i></el-input>
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center" @click="load">查询</el-button>
                </div>
                <div style="flex:2;text-align: right">
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center" @click="add">新增</el-button>
                </div>
            </div>
            <el-table :data="tableData" border style="width: 100%" :fit="true" stripe :cell-style="{ textAlign: 'center' }" :header-cell-style="{ textAlign: 'center' }">
                <el-table-column label="头像">
                    <template v-slot="scope">
                        <el-image :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="姓名"></el-table-column>
                <el-table-column prop="phone" label="手机号"></el-table-column>
                <el-table-column prop="age" label="年龄"></el-table-column>
                <el-table-column prop="gender" label="性别"></el-table-column>
                <el-table-column fixed="right" label="操作">
                    <template slot-scope="scope">
                        <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
                        <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
                        <el-popconfirm title="确定删除吗？"@confirm="del(scope.row.id)" style="margin-left: 10px">
                            <el-button size="small" type="text" slot="reference">删除</el-button>
                        </el-popconfirm>
                        <el-popconfirm title="修改之后，该管理员所有相关信息都会删除，确定修改吗？"@confirm="changeRole(scope.row)" style="margin-left: 10px">
                            <el-button size="small" type="text" slot="reference">修改权限</el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
            <div class="block" style="text-align: right;margin-top: 50px">
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
                <el-form-item label="头像">
                    <el-upload
                        class="avatar-uploader"
                        action="http://localhost:9090/files/upload"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess">
                        <img v-if="form.avatar" :src="form.avatar" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="用户名">
                    <el-input size="small" v-model="form.userName" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group size="small" v-model="form.gender">
                        <el-radio label="男"></el-radio>
                        <el-radio label="女"></el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="年龄">
                    <el-input size="small" v-model="form.age" placeholder="请输入手机号"></el-input>
                </el-form-item>
                <el-form-item label="电话">
                    <el-input size="small" v-model="form.phone" placeholder="请输入手机号"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="small" @click="dialogVisible = false">取 消</el-button>
                <el-button size="small" type="primary" @click="save">保 存</el-button>
            </div>
        </el-dialog>


        <el-dialog title="请填写信息" :visible.sync="dialogRole" width="40%">
            <el-form :model="form" label-position="right" label-width="100px" style="padding-right: 40px">

                <el-form-item label="权限修改为">
                    <el-radio-group size="small" v-model="form.role" >
                        <el-radio lable="2">用户</el-radio>
                    </el-radio-group>
                </el-form-item>


            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="small" @click="dialogVisible = false">取 消</el-button>
                <el-button size="small" type="primary" @click="change">保 存</el-button>
            </div>
        </el-dialog>


    </div>
</template>

<script>
import request from "@/utils/request";
export default {
    // 页面加载时调用方法
    created(){
        this.load();
    },
    methods: {
        handleClick(row) {
            console.log(row);
        },
    handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
    },


        handleAvatarSuccess(res) {
            this.form.avatar = res.data;
        },
//  当点击页码时，当前页码数赋值给data变量pageNum
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum;
            this.load();
        },
        //修改权限
        changeRole(row){
            this.form = JSON.parse(JSON.stringify(row));
            this.dialogRole = true;
        },
        change(){
            request.post("/admin/changeRole",this.form).then(res =>{
                if(res.code ==='0'){
                    this.$notify.success("角色修改成功");
                    this.dialogRole=false;
                    this.load();
            }else{
                    this.$notify.error(res.msg);
            }
            })
        },


        // 点击编辑，打开模态框，回显当前对象的信息
        edit(row){
            this.dialogVisible = true;
            this.form = row;
        },

        // 根据ID删除某条数据
        del(id) {
            request.delete("/admin/" + id).then(res => {
                if (res.code === '0') {
                    this.$notify.success('删除成功');
                    this.load();
                } else {
                    this.$notify.error(res.msg);
                }
            });
        },
    load()  {
        request.post("/admin/page?pageNum=" + this.pageNum, this.search).then(res => {
            if (res.code === '0') {
                this.tableData = res.data.list;
                this.total = res.data.total;
            } else {
                this.$notify.error(res.msg);
            }
        });
    },
        // 点击新增按钮点击事件调用
        add(){
            this.form = {};
            this.dialogVisible = true;
        },
        //保存，新增的时候id时空的，判断有没有id，如果没有id，则是新增，否则就是编辑
        save(){
            if (!this.form.id){ //如果没有id 走新增接口
                this.form.role =1;
                request.post("/admin",this.form).then(res =>{ //post请求把form对象传到后端，后端写逻辑把form存到数据库里
                    if (res.code === '0'){ //如果接口调用成功，则把模态框关闭，重新加载一下数据库的数据
                        this.$notify.success("新增成功");
                        this.dialogVisible = false;
                        this.load();
                    } else {
                        this.$notify.error(res.msg); // 如果不成功，返回报错信息
                    }
                })
            } else {
                request.post("/admin/edit",this.form).then(res =>{
                    if (res.code === '0'){
                        this.$notify.success("修改成功");
                        this.dialogVisible = false;
                        this.load();
                    } else {
                        this.$notify.error(res.msg); // 如果不成功，返回报错信息
                    }
                })
            }
        },

    },


    data() {
        return {
            form:{},
            dialogVisible:false,
            dialogRole:false,
            input:'',
            pageNum:1,
            total:0,
            search:{},
        tableData: []
    }
    }


}
</script>
