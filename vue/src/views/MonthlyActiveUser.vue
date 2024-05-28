<template>
  <div>
    <div class="flex gap-4 mb-4 items-center">
      <el-input
          v-model="name"
          style="width: 240px"
          size="large"
          placeholder="请输入姓名"
          :suffix-icon="Search"
          @input="handleInput"
      />
    </div>
    <div style="margin-top: 20px">
      <el-upload action="http://localhost:9090/monthlyActiveUser/import" accept="xlsx" :show-file-list="false" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" plain icon="Upload">导入本月数据</el-button>
        <el-button type="success" plain icon="Upload">导入同比数据</el-button>
      </el-upload>
      <el-button class="ml-10" type="warning" plain @click="exp" icon="Download">导出数据</el-button>
      <el-popconfirm
          width="220"
          confirm-button-text="确认删除！"
          cancel-button-text="我再想想"
          :icon="InfoFilled"
          icon-color="red"
          title="你确定要删除吗？"
          @confirm="delBatch"
      >
        <template #reference>
          <el-button type="danger" plain style="margin-left: 10px" icon="Delete">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <div>
      <div style="margin-top: 20px">
        <el-table :data="tableData" border height="600" style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"/>
          <el-table-column prop="sendUser" label="发帖用户"/>
          <el-table-column prop="activityUser" label="上进活动人数" />
          <el-table-column prop="customUser" label="打卡上进习惯人数"/>
          <el-table-column prop="shareUser" label="帖子分享人数"/>
          <el-table-column prop="likeUser" label="帖子点赞人数"/>
          <el-table-column prop="commentUser" label="帖子评论人数"/>
          <el-table-column prop="payUser" label="商城下单人数"/>
          <el-table-column prop="signInUser" label="每日签到人数"/>
          <el-table-column prop="rewardUser" label="直播广场打赏"/>
          <el-table-column prop="shareActUser" label="分享活动人数"/>
          <el-table-column prop="removingUser" label="行为合计去重人数"/>
          <el-table-column prop="createTime" label="更新时间">
            <template #default="{ row }">
              {{ row ? formatDate(row.createTime) : '' }}
            </template>
          </el-table-column>


          <el-table-column label="操作">
            <template #default="scope">

              <!--<el-button @click="handleEdit(scope.$index, scope.row)" type="success">编辑</el-button>-->
              <el-popconfirm
                  width="220"
                  confirm-button-text="确认删除！"
                  cancel-button-text="我再想想"
                  :icon="InfoFilled"
                  icon-color="red"
                  title="你确定要删除吗？"
                  @confirm="delUserList(scope.row.id)"
              >
                <template #reference>
                  <el-button type="danger" icon="Delete">删除</el-button>
                </template>
              </el-popconfirm>

            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="margin-top: 20px">
        <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :page-sizes="[10, 15, 20, 25]"
            :small="small"
            :disabled="disabled"
            :background="background"
            layout="total, sizes, prev, pager, next"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import request from "@/utils/request";
import {ElMessage, ElTable} from "element-plus";
import {InfoFilled, Search} from '@element-plus/icons-vue'
import dayjs from 'dayjs';

const name = ref('')
async function handleInput(){
  await getList()
}

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])

//获取用户信息
async function getList(){
  try {
    const res = await request.get("/monthlyActiveUser/page",{
      pageNum: pageNum.value,
      pageSize: pageSize.value,
    })
    if (res.code === '200'){
      tableData.value = res.data.records
      total.value = res.data.total

    }
  } catch (error) {
    console.error('获取用户数据出错：', error)
  }
}


const form = ref({})
async function saveUserList(){
  const res = await request.post("/monthlyActiveUser",form.value)

  if (res.code === '200'){
    ElMessage({
      message: '保存成功！',
      type: 'success',
    })
    await getList()
    centerDialogVisible.value = false
  }else {
    ElMessage({
      message: '保存失败！',
      type: 'warning',
    })
  }
}
//删除用户信息
async function delUserList(id){
  const res = await request.delete("/monthlyActiveUser/"+id)
  if (res.code === '200'){
    ElMessage({
      message: '删除成功！',
      type: 'success',

    })
    centerDialogVisible.value = false
    await getList()
  }else {
    ElMessage({
      message: '删除失败！',
      type: 'warning',
    })
  }
}
//批量删除用户信息
async function delBatch(){
  const ids = multipleSelection.value.map(v => v.id)
  const res = await request.post("/monthlyActiveUser/del/batch",ids)
  if(res.code === '200'){
    ElMessage({
      message: '删除成功！',
      type: 'success',
    })
    await getList()
  }else {
    ElMessage({
      message: '删除失败！',
      type: 'warning',
    })
  }
}

// 在组件加载时获取用户列表数据
getList()

const small = ref(false)
const background = ref(false)
const disabled = ref(false)

const handleSizeChange = (val: number) => {
  pageSize.value = Number(val)
  console.log(pageSize.value)
  getList()
}
const handleCurrentChange = (val: number) => {
  pageNum.value = Number(val)
  console.log(pageNum.value)
  getList()
}
const centerDialogVisible = ref(false)
const handleEdit = (index: number, row: any) => {
  centerDialogVisible.value = true
  form.value = JSON.parse(JSON.stringify(row))
}

//选择删除
const multipleSelection = ref([])
const handleSelectionChange = (val: any) => {
  multipleSelection.value = val
}
function exp(){
  window.open("http://localhost:9090/monthlyActiveUser/export")
}
function handleExcelImportSuccess() {
  ElMessage({
    message: '导入成功！',
    type: 'success',
  })
  getList()
}
async function cancel(){
  centerDialogVisible.value = false
}
const formatDate = (dateString: string) => {
  return dayjs(dateString).format('YYYY-MM-DD').substring(0, 10);
};

</script>

<style scoped>

</style>