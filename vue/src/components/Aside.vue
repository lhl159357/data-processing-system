
<template>
    <el-menu :default-openeds="opens"
             style="min-height: 100vh; overflow-x: hidden"
             background-color="rgb(18,65,86)"
             text-color="#fff"
             active-text-color="#ffd04b"
             router
    >
      <div style="height: 60px; line-height: 60px; text-align: center">
        <img src="@/images/logo.jpg" alt="" style="display: inline-block; width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
        <b style="color: white">数据处理系统</b>
      </div>
      <div v-for="item in menus" :key="item.id">
        <div v-if="item.path">
          <el-menu-item :index="item.path">
            <el-icon>
              <component :is="item.icon" class="icon" size="small"></component>
            </el-icon>
            <span slot="title">{{ item.name }}</span>
          </el-menu-item>
        </div>
        <div v-else>
          <el-sub-menu :index="item.id + ''">
            <template #title>
              <el-icon>
                <component :is="item.icon" class="icon" size="small"></component>
              </el-icon>
              <span>{{ item.name }}</span>
            </template>
            <div v-for="subItem in item.children" :key="subItem.id">
              <el-menu-item :index="subItem.path">
                <el-icon>
                  <component :is="subItem.icon" class="icon" size="small"></component>
                </el-icon>
                <span slot="title">{{ subItem.name }}</span>
              </el-menu-item>
            </div>
          </el-sub-menu>
        </div>
      </div>
    </el-menu>
</template>

<script lang="ts" setup>

const menus = localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : []
const opens = localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
</script>

<style scoped>

</style>