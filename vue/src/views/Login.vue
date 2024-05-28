<script lang="ts" setup>
import { ref } from 'vue'
import {Lock, User} from "@element-plus/icons-vue";
import request from "@/utils/request";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";


const input = ref('')
const password = ref('')
const router = useRouter()
const user = [{}]
async function login() {
  // 使用 input.value 和 password.value 作为登录凭据
  const loginData = {
    username: input.value,
    password: password.value
  }


    const res = await request.post('/user/login', loginData)
    if (res.code === '200') {
      localStorage.setItem("user", JSON.stringify(res.data))
      localStorage.setItem("menus", JSON.stringify(res.data.menus))
      ElMessage({
        message: '登录成功！',
        type: 'success',
      })
      // 登录成功，跳转到首页
      await router.push('/home')
    } else {
      ElMessage({
        message: res.msg,
        type: 'error',
      })
    }
}
</script>

<template>
  <div class="background">
    <el-card style="margin:300px auto;width: 350px">
      <div style="text-align: center">
        <img src="@/images/logo.jpg" alt="" style="display: inline-block; width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
        <b>远智数据处理系统</b>
      </div>
      <div style="margin-top: 50px">
        <div style="margin: 20px 40px">
          <el-input v-model="input" style="width: 240px" placeholder="请输入账号" :prefix-icon="User"/>
        </div>
        <div style="margin: 20px 40px">
          <el-input
              v-model="password"
              style="width: 240px"
              type="password"
              placeholder="请输入密码"
              show-password
              :prefix-icon="Lock"
          />
        </div>
      </div>
      <div style="margin-left: 90px">
        <el-button type="primary" round @click="login">登录</el-button>
        <el-button type="warning" round>注册</el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.background {
    height: 100vh;
    background-image: linear-gradient(to bottom right, #FC466B, #e23ffb, #3F5EFB, #3ffbc3);
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>