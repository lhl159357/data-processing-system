<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router';

const route = useRoute()
const router = useRouter()
const user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
const menus = localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : {}
function loginOut(){
  router.replace('/login')
}
</script>

<template>
  <div>
    <div>
    </div>
    <div >
      <div style="margin-top: 10px">
        <div style="text-align: right">
          <el-dropdown style="cursor: pointer">
            <img :src="user.icon" alt="" style="width: 50px ; height: 40px; border-radius: 100%; position: relative;right: 5px">
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人信息</el-dropdown-item>
                <el-dropdown-item @click="loginOut">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div style="display: inline-block">


        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
