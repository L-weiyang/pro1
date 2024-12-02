<template>
    <div>
        <div style="width: 100%;height:1000px;padding:40px 40px; background-color: white;border-radius: 4px">
            <!--按钮 和 搜索框样式-->
            <div style="margin-bottom: 40px;font-size: 18px;font-weight: 530;">预约管理</div>
            <div style="margin-bottom: 20px; display: flex">
                <div style="flex: 9;text-align: left">
                    <el-input placeholder="请输入预约人姓名查询" size="small"  v-model="search.userName" style="width: 20%; margin-right: 20px"><i slot="suffix" class="el-input__icon el-icon-search"></i></el-input>
                    <el-button type="success" size="small" style="border-radius: 1px;width: 100px;text-align: center" @click="load">查询</el-button>
                </div>

            </div>
            <!--      表格样式-->
            <el-table :data="tableData" border  stripe style="width: 100%"
                      :cell-style="{ textAlign: 'center' }" :header-cell-style="{ textAlign: 'center' }">

                <el-table-column prop="id" label="预约记录ID"></el-table-column>
                <el-table-column prop="activityName" label="活动名"></el-table-column>
                <el-table-column prop="userName" label="预约人"></el-table-column>
                <el-table-column prop="time" label="预约时间"></el-table-column>
                <el-table-column fixed="right" label="操作" v-if="user.role === 1">
                    <template slot-scope="scope">
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
            request.post("/record/page?pageNum=" + this.pageNum, this.search).then(res => {
                if (res.code === '0') {
                    this.tableData = res.data.list;
                    this.total = res.data.total;
                } else {
                    this.$notify.error(res.msg);
                }
            });
        },

        del(id) {
            request.delete("/record/" + id).then(res => {
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
            form: {}
        }
    }
}
</script>