<template>
  <div>
    <div class="flex gap-4 mb-4 items-center">
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
          <el-table-column prop="id" label="ID" />
          <el-table-column prop="name" label="名称"/>
          <el-table-column prop="description" label="描述"/>
          <el-table-column prop="flag" label="唯一标识"/>
          <el-table-column label="操作" width="400">
            <template #default="scope">
              <el-button class="ml-5" type="warning" slot="reference" @click="selectMenu(scope.row)" icon="Menu">分配菜单<i class="el-icon-menu"></i> </el-button>
              <el-button @click="handleEdit(scope.$index, scope.row)" type="success" icon="Edit">编辑</el-button>
              <el-popconfirm
                  width="220"
                  confirm-button-text="确认删除！"
                  cancel-button-text="我再想想"
                  :icon="InfoFilled"
                  icon-color="red"
                  title="你确定要删除吗？"
                  @confirm="delRoleList(scope.row.id)"
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
      <el-dialog v-model="centerDialogVisible" title="角色信息" width="500" center>
        <el-form label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="cancel">取消</el-button>
            <el-button type="primary" @click="saveRoleList">
              保存
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog title="菜单信息" v-model="dialogMenuVisible" width="20%">
        <div style="margin: 10px 50px">
          <el-tree
              ref="treeRef"
              style="max-width: 600px"
              :data="menuData"
              show-checkbox
              :default-expanded-keys="expends"
              :default-checked-keys="checks"
              node-key="id"
              :props="props"
          >
            <template #default="{ data }">
        <span class="custom-tree-node">
          <span>
            <el-icon>
                <component :is="data.icon" class="icon" size="small"></component>
              </el-icon>
            {{ data.name }}
          </span>
        </span>
            </template>
          </el-tree>
          <div slot="footer" class="dialog-footer" style="margin: 20px 40px">
            <el-button @click="cancelMenu">取 消</el-button>
            <el-button type="primary" @click= "saveRoleMenu">确 定</el-button>
          </div>
        </div>

      </el-dialog>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import request from "@/utils/request";
import {ElMessage, ElTable, ElTree} from 'element-plus'
import {InfoFilled, Search} from "@element-plus/icons-vue";
import {useRoute} from "vue-router";
import {LoginStore} from "@/stores/LoginStore";
import router from "@/router";

const name = ref('')
async function handleInput(){
  await getRoleList()
}

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])


//获取权限信息
async function getRoleList(){
  try {
    const res = await request.get("/role/page",{
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
// 在组件加载时获取用户列表数据
getRoleList()

async function handleAdd(){
  centerDialogVisible.value = true
  form.value = {}
}

//更新权限信息
const form = ref({})
async function saveRoleList(){
  const res = await request.post("/role",form.value)
  console.log(form)
  if (res.code === '200'){
    ElMessage({
      message: '保存成功！',
      type: 'success',
    })
    await getRoleList()
    centerDialogVisible.value = false
  }else {
    ElMessage({
      message: '保存失败！',
      type: 'warning',
    })
  }
}
//删除权限信息
async function delRoleList(id){
  const res = await request.delete("/role/"+id)
  if (res.code === '200'){
    ElMessage({
      message: '删除成功！',
      type: 'success',

    })
    centerDialogVisible.value = false
    await getRoleList()
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
  const res = await request.post("/role/del/batch",ids)
  if(res.code === '200'){
    ElMessage({
      message: '删除成功！',
      type: 'success',
    })
    await getRoleList()
  }else {
    ElMessage({
      message: '删除失败！',
      type: 'warning',
    })
  }
}
//分配菜单
const treeRef = ref<InstanceType<typeof ElTree>>()
const expends = ref([])
const menuData = ref([])
const checks = ref([])
const dialogMenuVisible = ref(false)
const props = {
  label: 'name',
}
const selectedRoleId = ref(null);
const selectedRoleFlag = ref(null);
const selectMenu = async (role) => {
  selectedRoleId.value = role.id; // 存储选定的 roleId
  console.log(role)
  selectedRoleFlag.value = role.flag
  dialogMenuVisible.value = true
  try {
    const res = await request.get("/menu")
    menuData.value = res.data
    // 把后台返回的菜单数据处理成id数组
    expends.value = menuData.value.map(v => v.id)
    const res1 = await request.get("/role/roleMenu/" + role.id)
    checks.value = res1.data

    const res2 = await request.get("/menu/ids")
    const ids = res2.data
    dialogMenuVisible.value = true
        ids.forEach(id => {
          if (!checks.value.includes(id)) {
            treeRef.value!.setChecked(id,false,false)
          }
        })
  } catch (error) {
    console.error('获取菜单信息失败：', error)
    // 可能需要在这里处理错误信息
  }
}

const roleFlag = ref()
async function saveRoleMenu(){
  const roleId = selectedRoleId.value // 获取存储的 roleId
  const roleFlag = selectedRoleFlag.value
  const res = await request.post("/role/roleMenu/" + roleId, treeRef.value!.getCheckedKeys())
    if(res.code === '200'){
      ElMessage({
        message: '绑定成功！',
        type: 'success',
      })
      dialogMenuVisible.value = false
      if(roleFlag === 'ROLE_ADMIN'){
        localStorage.removeItem("menus")
        localStorage.removeItem("user")
        await router.push("/login")
      }
    }else {
      ElMessage({
        message: '绑定失败！',
        type: 'error',
      })
    }
}

const small = ref(false)
const background = ref(false)
const disabled = ref(false)

const handleSizeChange = (val: number) => {
  pageSize.value = Number(val)
  getRoleList()
}
const handleCurrentChange = (val: number) => {
  pageNum.value = Number(val)
  getRoleList()
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
async function cancelMenu(){
  dialogMenuVisible.value = false
}



</script>

<style scoped>


</style>