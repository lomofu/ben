<template>
  <v-row style="min-height: 450px">
    <v-card-title class="d-flex justify-center" style="font-weight: bold">
      本周项目数
    </v-card-title>
    <v-card-subtitle class="d-flex justify-center">
      该图展示本周每日任务总数
    </v-card-subtitle>
    <div class="data-empty" v-if="chartData.rows.length < 1">
      没有数据,无法绘制图形 😂
    </div>
    <div class="ma-2" v-else>
      <ve-histogram
        :data="chartData"
        :judge-width="true"
        :change-delay="200"
        :yAxis="yAxis"
        :width-change-delay="200"
        :toolbox="toolbox"
        :extend="extend"
      ></ve-histogram>
    </div>
  </v-row>
</template>

<script>
import "v-charts/lib/style.css";
import echarts from "echarts";
import { REQUEST } from "../../../common/view/Constant";
export default {
  name: "projectChart",
  data() {
    this.yAxis = {
      type: "value",
      minInterval: 1
    };
    this.toolbox = {
      feature: {
        saveAsImage: { show: true }
      }
    };
    this.extend = {
      legend: {
        orient: "vertical",
        left: "left"
      },
      dataZoom: [
        {
          type: "inside"
        },
        {
          type: "slider"
        }
      ],
      series: {
        top: "50%",
        showBackground: true,
        backgroundStyle: {
          color: "rgba(220, 220, 220, 0.8)"
        },
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "#ffe06f" },
            { offset: 1, color: "#30E8BF" }
          ])
        }
      }
    };
    return {
      chartData: {
        columns: ["日期", "任务数量"],
        rows: []
      }
    };
  },
  methods: {
    getRenderData(data) {
      let rows = this.chartData.rows;
      if (rows.length > 0) {
        rows = [];
      }
      data.forEach(e => rows.push({ 日期: e.date, 任务数量: e.count }));
    },
    async fetchData() {
      const { id } = this.$route.params;
      const {
        data: { code, data }
      } = await this.$api.search.company.getThisWeekJobsChartData(id);
      if (REQUEST.SUCCESS.code === code) {
        this.getRenderData(data);
      }
    }
  },
  watch: {
    $route() {
      this.fetchData();
    }
  },
  created() {
    this.fetchData();
  }
};
</script>

<style scoped>
div {
  margin: 0;
  position: relative;
  width: 100%;
}
.data-empty {
  min-height: 400px;
  z-index: 2;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.7);
  color: #888;
  font-size: 16px;
}
</style>
