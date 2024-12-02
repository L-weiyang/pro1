<template>
    <div>
        <div style="width: 100%;height:1000px;padding:40px 40px; background-color: white;border-radius: 4px">
            <!--按钮 和 搜索框样式-->
            <div style="margin-bottom: 40px;font-size: 18px;font-weight: 530;">用户管理</div>
            <div style="margin-bottom: 20px; display: flex">
                <div class="input_box">
                    <el-input placeholder="请输入查询内容" size="small"  v-model="search.userName" style="width: 20%; margin-right: 20px"><i slot="suffix" class="el-input__icon el-icon-search"></i></el-input>
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center" @click="load">查询</el-button>
                    <el-popconfirm title="确定删除吗？" @confirm="delBatch()">
                        <el-button slot="reference" size="small" type="danger" style="margin-left: 10px;border-radius: 1px;width: 100px;text-align: center" >批量删除</el-button>
                    </el-popconfirm>
                </div>
                <div class="button-box">
                    <el-button type="success" size="small" style="width: 100px;text-align: center;margin-right: 23px" @click="add">新增</el-button>
                    <el-upload :action="'http://localhost:9090/user/upload?token='+ token" style="display: inline-block":on-success="fileLoad":show-file-list="false">
                        <el-button size="small" type="primary" style="bwidth: 100px;text-align: center;margin-right: 20px">导入excel</el-button>
                    </el-upload>
                    <el-button size="small" type="primary" style="background:darkorange" @click="exp">导出excel</el-button>

                </div>
            </div>
            <!--      表格样式-->
            <el-table :data="tableData" border  stripe style="width: 100%"  :cell-style="{ textAlign: 'center' }" :header-cell-style="{ textAlign: 'center' }" :row-key="getRowKeys" @selection-change="handleSelectionChange">
                <el-table-column ref="table" type="selection" align="center":reserve-selection="true"></el-table-column>

<!--                <el-table-column prop="id" label="ID" ></el-table-column>-->

                <el-table-column label="头像">
                    <template v-slot="scope">
                        <el-image :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="姓名" ></el-table-column>
                <el-table-column prop="gender" label="性别" ></el-table-column>
                <el-table-column prop="age" label="年龄"></el-table-column>
                <el-table-column prop="phone" label="电话" ></el-table-column>
                <el-table-column prop="gradeName" label="所属班级" ></el-table-column>
                <el-table-column fixed="right" label="操作" >
                    <template slot-scope="scope">
                        <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
                        <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
                        <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)" style="margin-left: 10px">
                            <el-button size="small" type="text" slot="reference">删除</el-button>
                        </el-popconfirm>
                        <el-popconfirm title="修改之后，该用户所有相关信息都会删除，确定修改吗？"@confirm="changeRole(scope.row)" style="margin-left: 10px">
                            <el-button size="small" type="text" slot="reference">修改权限</el-button>
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
                <el-form-item label="所属班级">
                    <el-select size="small" style="width: 100%" v-model="form.gradeId">
                        <el-option v-for="item in gradeData" :value="item.id" :label="item.gradeName" :key="item.id"></el-option>
                    </el-select>
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
                        <el-radio lable="1">管理员</el-radio>
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
    // 自定义变量
    data() {
        return {
            gradeData:{},
            form: {},
            dialogVisible: false,
            dialogRole:false,
            //接收要批量删除的数据
            selectionData:[],
            tableData: [],
            pageNum: 1,
            total: 0,
            search: {},
            token:localStorage.getItem("token"),
        }
    },
    // 页面加载时调用方法
    created() {
        this.load();
        this.loadGrade();
    },
    methods: {
        // 加载班级分类信息
        loadGrade(){
            request.get("grade/alldata").then(res =>{
                if (res.code === '0'){
                    this.gradeData = res.data;
                } else {
                    this.$notify.error(res.msg)
                }

            });
        },

        handleClick(row) {
            console.log(row);
        },
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
            request.post("/user/changeRole",this.form).then(res =>{
                if(res.code ==='0'){
                    this.$notify.success("角色修改成功");
                    this.dialogRole=false;
                    this.load();
                }else{
                    this.$notify.error(res.msg);
                }
            })
        },

        // 根据ID删除某条数据
        del(id) {
            request.delete("/user/" + id).then(res => {
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
            request.post("/user/page?pageNum=" + this.pageNum, this.search).then(res => {
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
            this.$nextTick(() => {
                this.$refs.tx.clearFiles()
            });
            this.dialogVisible = true;
        },
        // 点击新增按钮点击事件调用
        add() {
            this.form = {};
            this.$nextTick(() => {
                this.$refs.tx.clearFiles()
            });

            this.dialogVisible = true;
        },
        // 点击保存，新增的时候id是空的，判断有没有id，如果没有id的话是新增，如果有id的话 是编辑
        save() {
            if (!this.form.id) { //如果没有id 走新增接口
                this.form.role =2;
                request.post("/user", this.form).then(res => { //post请求把form对象传到后端，后端写逻辑把form存到数据库里
                    if (res.code === '0') { //如果接口调用成功，则把模态框关闭，重新加载一下数据库的数据
                        this.$notify.success("新增成功");
                        this.dialogVisible = false;
                        this.load();
                    } else {
                        this.$notify.error(res.msg); // 如果不成功，返回报错信息
                    }
                })
            } else {
                request.post("/user/edit", this.form).then(res => {
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

        exp(){
            this.$notify.success('导出成功');

            location.href = 'http://localhost:9090/user/export?token=' + this.token
        },

        //批量删除相关函数
        getRowKeys(row){
            return row.id;
        },
        handleSelectionChange(val) {
            this.selectionData = val;
        },
        delBatch(){
          request.post("user/delBatch",this.selectionData).then(res=>{
              if (res.code ==='0'){
                  this.$notify.success("批量删除成功");
                  this.load();
              }else {
                  this.$notify.error(res.msg);

              }
          })  ;

        },



        handleAvatarSuccess(res) {
            this.form.avatar = res.data;
        },
        fileLoad () {
            this.$notify.success('导入成功');
            this.load()
        },


    },


}
</script>