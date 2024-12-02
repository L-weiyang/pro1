<template>
    <div>
        <div style="width: 100%;height:1000px;padding:40px 40px; background-color: white;border-radius: 4px">
            <!--按钮 和 搜索框样式-->
            <div style="margin-bottom: 40px;font-size: 18px;font-weight: 530;">请假管理</div>
            <div style="margin-bottom: 20px; display: flex">
                <div style="flex: 9;text-align: left">
                    <el-input placeholder="请输入姓名查询" size="small"  v-model="search.userName" style="width: 20%; margin-right: 20px"><i slot="suffix" class="el-input__icon el-icon-search"></i></el-input>
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center" @click="load">查询</el-button>
                </div>
                <div class="button_box">
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center" @click="add" v-if="user.role === 2">新增</el-button>
                </div>

            </div>
            <!--      表格样式-->
            <el-table :data="tableData" border  stripe style="width: 100%"
                      :cell-style="{ textAlign: 'center' }" :header-cell-style="{ textAlign: 'center' }">

                <el-table-column prop="id" label="请假ID"></el-table-column>
                <el-table-column prop="userName" label="姓名"></el-table-column>
                <el-table-column prop="reason" label="请假缘由"></el-table-column>
                <el-table-column prop="examine" label="审核结果"></el-table-column>
                <el-table-column fixed="right" label="操作" v-if="user.role === 1">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
                        <el-button type="text" size="small" @click="edit(scope.row)">审核</el-button>
                        <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)" style="margin-left: 10px">
                            <el-button size="small" type="text" slot="reference" >删除</el-button>
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
            <el-dialog title="请填写信息" :visible.sync="dialogVisible" width="40%">
                <el-form :model="form" label-position="right" label-width="100px" style="padding-right: 40px">
                    <el-form-item label="请假缘由">
                        <el-input size="small" v-model="form.reason" placeholder="请输入请假缘由 "></el-input>
                    </el-form-item>
                    <el-form-item label="审核状态"  v-if="user.role === 1">
                        <el-radio-group size="small" v-model="form.examine">
                            <el-radio label="审核通过"></el-radio>
                            <el-radio label="审核不通过"></el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>


                <div slot="footer" class="dialog-footer">
                    <el-button size="small" @click="dialogVisible = false">取 消</el-button>
                    <el-button size="small" type="primary" @click="save">保 存</el-button>
                </div>
            </el-dialog>
        </div>
    </div>
</template>

<script>
import request from "@/utils/request";
export default {
    created() {
        this.load();
    },
    // 方法
    methods: {

        //  当点击页码时，当前页码数赋值给data变量pageNum
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum;
            this.load();
        },


        // 分页加载表格数据
        load() {
            request.post("/takeoff/page?pageNum=" + this.pageNum, this.search).then(res => {
                if (res.code === '0') {
                    this.tableData = res.data.list;
                    this.total = res.data.total;
                } else {
                    this.$notify.error(res.msg);
                }
            });
        },
        // 点击新增，打开模态框，并且把模态框的数据清空
        add() {
            this.dialogVisible = true;
            this.form = {}
        },
        // 点击编辑，打开模态框，回显当前对象的信息
        edit(row) {
            this.dialogVisible = true;
            this.form = JSON.parse(JSON.stringify(row));
        },
        // 点击保存，新增的时候id是空的，判断有没有id，如果没有id的话是新增，如果有id的话 是编辑
        save() {

            if (!this.form.id) { //如果没有id 走新增接口
                this.form.userId = this.user.id;
                this.form.userName = this.user.userName;
                request.post("/takeoff", this.form).then(res => { //post请求把form对象传到后端，后端写逻辑把form存到数据库里
                    if (res.code === '0') { //如果接口调用成功，则把模态框关闭，重新加载一下数据库的数据
                        this.$notify.success("新增成功");
                        this.dialogVisible = false;
                        this.load();
                    } else {
                        this.$notify.error(res.msg); // 如果不成功，返回报错信息
                    }
                })
            } else {
                request.post("/takeoff/edit", this.form).then(res => {
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
        // 根据ID删除某条数据
        del(id) {
            request.delete("/takeoff/" + id).then(res => {
                if (res.code === '0') {
                    this.$notify.success('删除成功');
                    this.load();
                } else {
                    this.$notify.error(res.msg);
                }
            });
        },

    },

    data() {
        return {
            // 定义User对象，从缓存里取值
            user: JSON.parse(localStorage.getItem("user") || {}),
            input: '',
            pageNum: 1,
            total: 0,
            search: {},
            tableData: [],
            dialogVisible: false,
            dialogRole: false,
            form: {}
        }
    }
}
</script>