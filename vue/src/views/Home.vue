<template>
  <div>
<!--    统计组件-->
    <div>
      <el-row :gutter="16">
        <el-col :span="8">
          <el-card>
            <div class="statistic-card">
              <el-statistic :value="RegisterTotal">
                <template #title>
                  <div style="display: inline-flex; align-items: center">
                    <b>新注册用户</b>
                    <el-tooltip
                        effect="dark"
                        content="新注册用户"
                        placement="top"
                    >
                      <el-icon style="margin-left: 4px" :size="12">
                        <Warning />
                      </el-icon>
                    </el-tooltip>
                  </div>
                </template>
              </el-statistic>
              <div class="statistic-footer">
                <div class="footer-item">
                  <span>相比昨日</span>
                  <span class="green">

            </span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="statistic-card">
              <el-statistic :value="693700">
                <template #title>
                  <div style="display: inline-flex; align-items: center">
                    Monthly Active Users
                    <el-tooltip
                        effect="dark"
                        content="Number of users who logged into the product in one month"
                        placement="top"
                    >
                      <el-icon style="margin-left: 4px" :size="12">
                        <Warning />
                      </el-icon>
                    </el-tooltip>
                  </div>
                </template>
              </el-statistic>
              <div class="statistic-footer">
                <div class="footer-item">
                  <span>month on month</span>
                  <span class="red">
              12%
              <el-icon>
                <CaretBottom />
              </el-icon>
            </span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="statistic-card">
              <el-statistic :value="72000" title="New transactions today">
                <template #title>
                  <div style="display: inline-flex; align-items: center">
                    New transactions today
                  </div>
                </template>
              </el-statistic>
              <div class="statistic-footer">
                <div class="footer-item">
                  <span>than yesterday</span>
                  <span class="green">
              16%
              <el-icon>
                <CaretTop />
              </el-icon>
            </span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
<!--    时间选择器-->
    <div>
      <div class="demo-date-picker">
        <div class="block">
          <span class="demonstration">选择你想查看的时间</span>
          <el-date-picker
              v-model="value2"
              type="daterange"
              unlink-panels
              range-separator="到"
              start-placeholder="起始时间"
              end-placeholder="结束时间"
              :shortcuts="shortcuts"
              size="large"
          />
          <el-button @click="getTime" class="ml-10" type="primary">获取数据</el-button>
        </div>
      </div>
    </div>
<!--    数据图表-->
    <div>
      <el-carousel :interval="16000" width="1600px" height="550px">
        <el-carousel-item>
          <div id="Register" style="width: 1600px;height: 500px"></div>
        </el-carousel-item>
        <el-carousel-item>
          <div id="AUD" style="width: 1600px; height: 500px"></div>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script lang="ts" setup>
import * as echarts from 'echarts';
import request from "@/utils/request";
import {onMounted, ref, watch} from "vue";
import type { SpaceInstance } from 'element-plus'

const date = ref('')
const RegisterTotal = ref()

async function getRegisterList() {
  const res = await request.get("/registerRecords")
  if(res.code === '200'){
    RegisterTotal.value = res.data.length
  }
}
getRegisterList()


// 获取当前时间
const now = new Date()
// 将当前时间作为value2的初始值
const value2 = ref([now,now])
function getTime() {
  const formattedDates = value2.value.map(date => formatDate(date))
  const startDate = formattedDates[0]
  const endDate = formattedDates[1]
  getTimeRData(startDate, endDate)
  getTimeAUData(startDate, endDate)
}
// 格式化时间函数
function formatDate(dateString) {
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = (date.getMonth() + 1).toString().padStart(2, '0')
  const day = date.getDate().toString().padStart(2, '0')
  return `${year}-${month}-${day}`
}
const RegisterOption = {
  toolbox: {
    feature: {
      dataView: {show: true, readOnly: false},
      magicType: {show: true, type: ['line', 'bar']},
      restore: {show: true},
      saveAsImage: {show: true}
    }
  },
  title: {
    text: '新注册用户数据',
    x: 'center',
    y: 'bottom',
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross',
      crossStyle: {
        color: '#999'
      }
    }
  },
  legend: {},
  xAxis: [
    {
      type: 'category',
      data: [],
      axisPointer: {
        type: 'shadow'
      }
    }
  ],
  yAxis: [
    {
      type: 'value',
      name: '人数',
      min: 0,
      max: 30,
      interval: 6,
      axisLabel: {
        formatter: '{value} 人',
      }
    }
  ],
  series: []
}

const AUDOption = {
  toolbox: {
    feature: {
      dataView: {show: true, readOnly: false},
      magicType: {show: true, type: ['line', 'bar']},
      restore: {show: true},
      saveAsImage: {show: true}
    }
  },
  title: {
    text: '活跃用户的社区日活行为数据',
    x: 'center',
    y: 'bottom',
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross',
      crossStyle: {
        color: '#999'
      }
    }
  },
  legend: {},
  xAxis: [
    {
      type: 'category',
      data: [],
      axisPointer: {
        type: 'shadow'
      }
    }
  ],
  yAxis: [
    {
      type: 'value',
      name: '人数',
      min: 0,
      max: 24000,
      interval: 6000,
      axisLabel: {
        formatter: '{value} 人',
      }
    }
  ],
  series: []
}
async function getTimeRData(startDate, endDate){
  const res = await request.get("/eChart/registerRecords/"+startDate+"/"+endDate)
  for (let date in res.data) {
    if (res.data.hasOwnProperty(date)) {
      // 将日期添加到 x 轴数据中
      RegisterOption.xAxis[0].data.push(date);
      // 获取对应日期的渠道注册数据
      let channelsData = res.data[date];
      // 遍历渠道注册数据，将注册人数填充到对应的系列中
      channelsData.forEach(item => {
        // 根据渠道的名称找到对应的系列
        let seriesIndex = RegisterOption.series.findIndex(series => series.name === item.channels);
        // 如果找到对应的系列，则将注册人数添加到该系列中
        if (seriesIndex !== -1) {
          RegisterOption.series[seriesIndex].data.push(item.count);
        } else {
          // 如果没有找到匹配的系列，创建一个新的系列
          RegisterOption.series.push({
            name: item.channels,
            type: 'bar',
            tooltip: {
              valueFormatter: function (channels) {
                return channels as number + ' 人';
              }
            },
            label:{
              show: true,
              position:'outside',
              formatter: function (channels) {
                return channels.value as number;
              }
            },
            data: [item.count]
          })
        }
      })
    }
    // 更新图表数据
    RegisterChart.setOption(RegisterOption);
  }

}

async function getTimeAUData(startDate, endDate) {
  try {
    const res = await request.get(`/eChart/activeUsersData/${startDate}/${endDate}`); // 假设 request 是您用于发起 HTTP 请求的对象，可能是 axios 或者其他库
    console.log(res.data);

    // 清空之前的数据，避免重复添加
    AUDOption.xAxis[0].data = [];
    AUDOption.series = [];

    // 映射英文系列名称为中文名称
    const seriesMap = {
      activeUser: '日活跃用户',
      attendUser: '日上进活动报名人数',
      cardUser: '日上进习惯打卡人数',
      commentUser: '日圈子帖子评论人数',
      likeUser: '日圈子帖子点赞人数',
      otherUser: '日圈子总分享人数_含上进故事等',
      payUser: '日商城下单人数',
      rewardUser: '日打赏人数',
      sendUser: '日发帖用户数',
      shareUser: '日圈子帖子分享人数',
      total: '总计'
    };

    // 填充新数据
    for (let item of res.data) {
      AUDOption.xAxis[0].data.push(item.date)
      for (let key in item) {
        if (key !== 'date') {
          let seriesName = seriesMap[key];
          let seriesIndex = AUDOption.series.findIndex(series => series.name === seriesName)
          if (seriesIndex === -1) {
            AUDOption.series.push({
              name: seriesName,
              type: 'line', // 或者其他图表类型，根据需求调整
              data: [item[key]],
              tooltip: {
                valueFormatter: function (channels) {
                  return channels as number + ' 人';
                }
              },
              label:{
                show: true,
                position:'outside',
                formatter: function (channels) {
                  return channels.value as number;
                }
              },
            })
          } else {
            AUDOption.series[seriesIndex].data.push(item[key])
          }
        }
      }
    }

    // 更新图表
    AUDChart.setOption(AUDOption);
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

// 在适当的位置定义 myChart
let RegisterChart
let AUDChart
onMounted(() => {
  //柱状图(新注册用户记录)
  let RegisterChartDom = document.getElementById('Register')
  RegisterChart = echarts.init(RegisterChartDom)
  RegisterOption && RegisterChart.setOption(RegisterOption)
  //柱状折线图(活跃用户的社区日活行为数据)
  let AUDChartDom = document.getElementById('AUD')
  AUDChart = echarts.init(AUDChartDom)
  AUDOption && AUDChart.setOption(AUDOption)
})

// 当时间选择器的值发生变化时，可以在这里获取选择的时间
const shortcuts = [
  {
    text: 'Last week',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: 'Last month',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: 'Last 3 months',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
]




</script>

<style scoped>
.el-col {
  text-align: center;
}

.countdown-footer {
  margin-top: 8px;
}
.demo-date-picker {
  display: flex;
  width: 100%;
  padding: 0;
  flex-wrap: wrap;
}

.demo-date-picker .block {
  padding: 30px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  flex: 1;
}

.demo-date-picker .block:last-child {
  border-right: none;
}

.demo-date-picker .demonstration {
  display: block;
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-bottom: 20px;
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

</style>
