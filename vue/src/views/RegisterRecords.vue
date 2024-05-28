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
      <el-upload action="http://localhost:9090/registerRecords/import" accept="xlsx" :show-file-list="false" :on-success="handleExcelImportSuccess" style="display: inline-block">
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
          <el-table-column prop="yzId" label="远智编码"/>
          <el-table-column prop="name" label="姓名" />
          <el-table-column prop="channels" label="注册渠道"/>
          <el-table-column prop="source" label="注册来源"/>
          <el-table-column prop="registerTime" label="注册时间">
            <template #default="{ row }">
              {{ row ? formatDate(row.registerTime) : '' }}
            </template>
          </el-table-column>
          <el-table-column prop="loginTime" label="登录时间">
            <template #default="{ row }">
              {{ row ? formatDate(row.loginTime) : '' }}
            </template>
          </el-table-column>


          <el-table-column label="操作">
            <template #default="scope">

<!--              <el-button @click="handleEdit(scope.$index, scope.row)" type="success">编辑</el-button>-->
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
    <div>
      <el-dialog v-model="centerDialogVisible" title="用户信息" width="500" center>
        <el-form label-width="80px">
          <el-form-item label="远智编码">
            <el-input v-model="form.yzId" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="注册渠道">
            <el-input v-model="form.channels" type="password" show-password autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="注册来源">
            <el-input v-model="form.sources" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="注册时间">
            <el-input v-model="form.registerTime" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="登录时间">
            <el-input v-model="form.loginTime" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="cancel">取消</el-button>
            <el-button type="primary" @click="saveUserList">
              保存
            </el-button>
          </div>
        </template>
      </el-dialog>
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
    const res = await request.get("/registerRecords/page",{
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      name: name.value
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
  const res = await request.post("/registerRecords",form.value)

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
  const res = await request.delete("/registerRecords/"+id)
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
  const res = await request.post("/registerRecords/del/batch",ids)
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
  window.open("http://localhost:9090/registerRecords/export")
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