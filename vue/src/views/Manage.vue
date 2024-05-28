<script setup lang="ts">
import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";
import {createRouter} from "vue-router";
const user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}

</script>

<template>
  <div>
    <el-container>
      <el-aside width="288px" style="background-color: rgb(18,65,86)"><Aside/></el-aside>
      <el-container>
        <el-header style="border-bottom: 1px solid #ccc" :user="user"><Header/></el-header>
        <el-main><RouterView/></el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style>

</style>
