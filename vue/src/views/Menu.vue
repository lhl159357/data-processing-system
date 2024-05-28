<template>
  <div>
    <div>
      <el-input
          v-model="name"
          style="width: 240px"
          size="large"
          placeholder="请输入名称"
          :suffix-icon="Search"
          @input="handleInput"
      />
    </div>
    <div style="margin-top: 20px">
      <el-button class="mt-5" type="primary" plain @click="handleAdd(null)" icon="Plus">新增</el-button>
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
        <el-table :data="tableData" border height="600" style="width: 100%" @selection-change="handleSelectionChange " default-expand-all row-key="id">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="55"/>
          <el-table-column prop="name" label="名称" width="300"/>
          <el-table-column prop="path" label="路径" width="200"/>
          <el-table-column prop="pagePath" label="页面路径"/>
          <el-table-column label="图标"  width="100">
            <template #default="scope">
              <el-icon>
                <component :is="scope.row.icon" class="icon" size="small"></component>
              </el-icon>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述"/>
          <el-table-column label="操作" width="400">
            <template #default="scope">
              <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path" icon="CirclePlusFilled">新增子菜单<i class="el-icon-plus"></i></el-button>
              <el-button @click="handleEdit(scope.$index, scope.row)" type="success" icon="Edit">
                编辑
              </el-button>
              <el-popconfirm
                  width="220"
                  confirm-button-text="确认删除！"
                  cancel-button-text="我再想想"
                  :icon="InfoFilled"
                  icon-color="red"
                  title="你确定要删除吗？"
                  @confirm="delMenuList(scope.row.id)"
              >
                <template #reference>
                  <el-button type="danger" icon="Delete">删除</el-button>
                </template>
              </el-popconfirm>

            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="padding: 10px 0">
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
      <el-dialog v-model="centerDialogVisible" title="菜单信息" width="500" center>
        <el-form label-width="80px">
          <el-form-item label="名称">
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="路径">
            <el-input v-model="form.path" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="页面路径">
            <el-input v-model="form.pagePath" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="图标">
            <el-select clearable v-model="form.icon" placeholder="请选择图标" style="width: 100%">
              <el-option v-for="item in iconList" :key="item.name" :label="item.name" :value="item.value">
                <el-icon>
                  <component :is="item.value" class="icon" size="small"></component>
                </el-icon>
                {{ item.name }}
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.description" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="cancel">取消</el-button>
            <el-button type="primary" @click="saveMenuList">
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

const name = ref()
async function handleInput(){
  await getMenuList()
}

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])


//获取菜单信息
async function getMenuList(){
  try {
    const res = await request.get("/menu",{
        name: name.value
    })
    if (res.code === '200'){
      tableData.value = res.data
      total.value = res.data.length
    }
  } catch (error) {
    console.error('获取用户数据出错：', error)
  }
}

async function handleAdd(pid){
  centerDialogVisible.value = true
  form.value = {}
  if (pid){
    form.value.pid = pid
  }
  console.log(pid)
  try {
    const res = await request.get("/menu/icons")
    if (res.code === '200') {
      iconList.value = res.data
    }
  } catch (error) {
    // 处理请求错误
    console.error('请求错误:', error)
  }
}

//更新菜单信息
const form = ref({})
async function saveMenuList(){
  const res = await request.post("/menu",form.value)
  console.log(form.value)
  if (res.code === '200'){
    ElMessage({
      message: '保存成功！',
      type: 'success',
    })
    await getMenuList()
    centerDialogVisible.value = false
  }else {
    ElMessage({
      message: '保存失败！',
      type: 'warning',
    })
  }
}
//删除菜单信息
async function delMenuList(id){
  const res = await request.delete("/menu/"+id)
  if (res.code === '200'){
    ElMessage({
      message: '删除成功！',
      type: 'success',

    })
    centerDialogVisible.value = false
    await getMenuList()
  }else {
    ElMessage({
      message: '删除失败！',
      type: 'warning',
    })
  }
}
//批量删除菜单信息
const delBatch = async () => {
  const ids = multipleSelection.value.map((v: any) => Number(v.id))
  // 其他逻辑
  const res = await request.post("/menu/del/batch",ids)
  if(res.code === '200'){
    ElMessage({
      message: '删除成功！',
      type: 'success',
    })
    await getMenuList()
  }else {
    ElMessage({
      message: '删除失败！',
      type: 'warning',
    })
  }
}



// 在组件加载时获取用户列表数据
getMenuList()

const small = ref(false)
const background = ref(false)
const disabled = ref(false)

const handleSizeChange = (val: number) => {
  pageSize.value = Number(val)
  getMenuList()
}
const handleCurrentChange = (val: number) => {
  pageNum.value = Number(val)
  getMenuList()
}
const centerDialogVisible = ref(false)
const iconList = ref([])
const handleEdit = async (index: number, row: any) => {
  form.value = JSON.parse(JSON.stringify(row))
  centerDialogVisible.value = true
  try {
    const res = await request.get("/menu/icons")
    if (res.code === '200') {
      iconList.value = res.data
    }
  } catch (error) {
    // 处理请求错误
    console.error('请求错误:', error)
  }
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