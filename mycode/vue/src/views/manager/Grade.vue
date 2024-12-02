<template>
    <div>
        <div style="width: 100%;height:1000px;padding:40px 40px; background-color: white;border-radius: 4px">
            <div style="margin-bottom: 40px;font-size: 18px;font-weight: 530;">班级管理</div>
            <div style="margin-bottom: 20px; display: flex">
                <div style="flex: 10;text-align: left">
                    <el-input placeholder="请输入班级名" size="small"  v-model="search.gradeName" style="width: 20%; margin-right: 20px"><i slot="suffix" class="el-input__icon el-icon-search"></i></el-input>
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center" @click="load">查询</el-button>
                </div>
                <div style="flex:2;text-align: right">
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center" @click="add" v-if = "user.role===1">新增</el-button>
                </div>
            </div>
            <el-table :data="tableData" border style="width: 100%" :fit="true" stripe :cell-style="{ textAlign: 'center' }" :header-cell-style="{ textAlign: 'center' }">

                <el-table-column prop="gradeName" label="班级名"></el-table-column>
                <el-table-column prop="id" label="班级ID"></el-table-column>
                <el-table-column prop="brief" label="简介"></el-table-column>

                <el-table-column fixed="right" label="操作"  v-if = "user.role===1">
                    <template slot-scope="scope">

                        <el-button type="text" size="small" @click="edit(scope.row)" >编辑</el-button>
                        <el-popconfirm title="确定删除吗？"@confirm="del(scope.row.id)" style="margin-left: 10px">
                            <el-button size="small" type="text" slot="reference">删除</el-button>
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



                <el-form-item label="班级名">
                    <el-input size="small" v-model="form.gradeName" placeholder="请输入班级名"></el-input>
                </el-form-item>

                <el-form-item label="简介">
                    <el-input size="small" v-model="form.brief" placeholder="请输入班级简介"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="small" @click="dialogVisible = false">取 消</el-button>
                <el-button size="small" type="primary" @click="save">保 存</el-button>
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

    handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
    },



//  当点击页码时，当前页码数赋值给data变量pageNum
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum;
            this.load();
        },

        // 点击编辑，打开模态框，回显当前对象的信息
        edit(row){
            this.dialogVisible = true;
            this.form = row;
        },

        // 根据ID删除某条数据
        del(id) {
            request.delete("/grade/" + id).then(res => {
                if (res.code === '0') {
                    this.$notify.success('删除成功');
                    this.load();
                } else {
                    this.$notify.error(res.msg);
                }
            });
        },
    load()  {
        request.post("/grade/page?pageNum=" + this.pageNum, this.search).then(res => {
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
                request.post("/grade",this.form).then(res =>{ //post请求把form对象传到后端，后端写逻辑把form存到数据库里
                    if (res.code === '0'){ //如果接口调用成功，则把模态框关闭，重新加载一下数据库的数据
                        this.$notify.success("新增成功");
                        this.dialogVisible = false;
                        this.load();
                    } else {
                        this.$notify.error(res.msg); // 如果不成功，返回报错信息
                    }
                })
            } else {
                request.post("/grade/edit",this.form).then(res =>{
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
            user:JSON.parse(localStorage.getItem("user") || {}),
            input:'',
            pageNum:1,
            total:0,
            search:{},
        tableData: []
    }
    }


}
</script>
