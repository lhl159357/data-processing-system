<template>
  <div>
    <div class="flex gap-4 mb-4 items-center">
      <el-input
          v-model="date"
          style="width: 240px"
          size="large"
          placeholder="请输入姓名"
          :suffix-icon="Search"
          @input="handleInput"
      />
    </div>
    <div style="margin-top: 20px">
      <el-upload action="http://localhost:9090/activeUsersData/import" accept="xlsx" :show-file-list="false" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" plain icon="Upload">导入数据</el-button>
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
          <el-table-column type="selection" width="55" />
          <el-table-column prop="date" label="日期"/>
          <el-table-column prop="activeUser" label="日活跃用户" />
          <el-table-column prop="sendUser" label="日发帖用户数"/>
          <el-table-column prop="attendUser" label="日上进活动报名人数"/>
          <el-table-column prop="cardUser" label="日上进习惯打卡人数"/>
          <el-table-column prop="likeUser" label="日圈子帖子点赞人数"/>
          <el-table-column prop="commentUser" label="日圈子帖子评论人数"/>
          <el-table-column prop="shareUser" label="日圈子帖子分享人数"/>
          <el-table-column prop="payUser" label="日商城下单人数"/>
          <el-table-column prop="rewardUser" label="日打赏人数"/>
          <el-table-column prop="otherUser" label="日圈子总分享人数_含上进故事等"/>
          <el-table-column prop="total" label="合计"/>
          <el-table-column label="操作">
            <template #default="scope">
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
            :total="totals"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {reactive, ref} from 'vue'
import request from "@/utils/request";
import {ElMessage, ElTable} from "element-plus";
import {InfoFilled, Search} from '@element-plus/icons-vue'
import router from "@/router";

const date = ref('')
async function handleInput(){
  await getList()
}

const pageNum = ref(1)
const pageSize = ref(10)
const totals = ref(0)
const tableData = ref([])

//获取用户信息
async function getList(){
  try {
    const res = await request.get("/activeUsersData/page",{
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      date: date.value
    })
    if (res.code === '200'){
      tableData.value = res.data.records
      totals.value = res.data.total
    }
  } catch (error) {
    console.error('获取用户数据出错：', error)
  }
}


//更新用户信息
const form = ref({})
async function saveUserList(){
  const res = await request.post("/activeUsersData",form.value)

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
  const res = await request.delete("/activeUsersData/"+id)
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
  const res = await request.post("/activeUsersData/del/batch",ids)
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
  window.open("http://localhost:9090/activeUsersData/export")
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

</script>

<style scoped>

</style>