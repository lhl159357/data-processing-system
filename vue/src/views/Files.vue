<template>
  <div>
    <div class="flex gap-4 mb-4 items-center">
      <el-input
          v-model="name"
          style="width: 240px"
          size="large"
          placeholder="请输入文件名"
          :suffix-icon="Search"
          @input="handleInput"
      />
    </div>
    <div style="margin-top: 20px">
      <el-button class="mt-5" type="primary" plain @click="handleAdd" icon="Plus">新增</el-button>
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
          <el-button class="mt-5" type="danger" plain icon="Delete">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <div>
      <div style="margin-top: 20px">
        <el-table :data="tableData" border height="350" style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID"/>
          <el-table-column prop="name" label="文件名" />
          <el-table-column prop="type" label="类型"/>
          <el-table-column prop="size" label="文件大小"/>
          <el-table-column prop="url" label="文件路径"/>
          <el-table-column prop="md5" label="MD5"/>
          <el-table-column label="操作">
            <template #default="scope">

              <el-button @click="handleEdit(scope.$index, scope.row)" type="success" icon="Edit">编辑</el-button>
              <el-popconfirm
                  width="220"
                  confirm-button-text="确认删除！"
                  cancel-button-text="我再想想"
                  :icon="InfoFilled"
                  icon-color="red"
                  title="你确定要删除吗？"
                  @confirm="delFileList(scope.row.id)"
              >
                <template #reference>
                  <el-button type="danger" icon="Delete">删除</el-button>
                </template>
              </el-popconfirm>

            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="demo-pagination-block">
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
          <el-form-item label="ID">
            <el-input v-model="form.id" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="文件名">
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="文件路径">
            <el-input v-model="form.password" type="password" show-password autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="cancel">取消</el-button>
            <el-button type="primary" @click="saveFileList">
              保存
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {reactive, ref} from 'vue'
import request from "@/utils/request";
import {ElMessage, ElTable} from "element-plus";
import {InfoFilled, Search} from '@element-plus/icons-vue'
import router from "@/router";

const name = ref('')
async function handleInput(){
  await getFileList()
}

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const roles = ref([])

//获取用户信息
async function getFileList(){
  try {
    const res = await request.get("/file/page",{
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

async function handleAdd(){
  centerDialogVisible.value = true
  form.value = {}
}

//更新用户信息
const form = ref({})
async function saveFileList(){
  const res = await request.post("/file",form.value)

  if (res.code === '200'){
    ElMessage({
      message: '保存成功！',
      type: 'success',
    })
    await getFileList()
    centerDialogVisible.value = false
  }else {
    ElMessage({
      message: '保存失败！',
      type: 'warning',
    })
  }
}
//删除用户信息
async function delFileList(id){
  const res = await request.delete("/file/"+id)
  if (res.code === '200'){
    ElMessage({
      message: '删除成功！',
      type: 'success',

    })
    centerDialogVisible.value = false
    await getFileList()
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
  const res = await request.post("/file/del/batch",ids)
  if(res.code === '200'){
    ElMessage({
      message: '删除成功！',
      type: 'success',
    })
    await getFileList()
  }else {
    ElMessage({
      message: '删除失败！',
      type: 'warning',
    })
  }
}

// 在组件加载时获取用户列表数据
getFileList()

const small = ref(false)
const background = ref(false)
const disabled = ref(false)

const handleSizeChange = (val: number) => {
  pageSize.value = Number(val)
  console.log(pageSize.value)
  getFileList()
}
const handleCurrentChange = (val: number) => {
  pageNum.value = Number(val)
  console.log(pageNum.value)
  getFileList()
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

async function cancel(){
  centerDialogVisible.value = false
}

</script>

<style scoped>

</style>