<template>
  <v-row style="min-height: 450px;">
    <v-subheader class="ma-3" style="font-weight: bold;font-size: 22px;">
      {{ new Date().getFullYear() }}年 {{ tip }}
    </v-subheader>
    <div class="data-empty" v-if="chartData.rows.length < 1">
      没有数据,无法绘制图形 😂
    </div>
    <div class="ma-3" v-else>
      <v-card-title class="d-flex justify-center" style="font-weight: bold">
        {{ title }}
      </v-card-title>
      <v-card-subtitle class="d-flex justify-center">
        {{ subTitle }}
      </v-card-subtitle>
      <ve-line
        :data="chartData"
        :judge-width="true"
        :change-delay="300"
        :yAxis="yAxis"
        :width-change-delay="100"
        :settings="chartSettings"
        :toolbox="toolbox"
        :extend="extend"
      ></ve-line>
    </div>
  </v-row>
</template>

<script>
import "v-charts/lib/style.css";
import echarts from "echarts";
export default {
  name: "teamChart",
  props: {
    tip: {
      type: String,
      required: true
    },
    title: {
      type: String
    },
    subTitle: {
      type: String
    },
    yAxisName: {
      type: String,
      required: true
    },
    rows: {
      type: Array,
      required: true
    },
    column: {
      type: String,
      required: true
    },
    color: {
      type: String
    },
    subColor: {
      type: String
    }
  },
  data() {
    this.yAxis = {
      type: "value",
      minInterval: 1
    };
    this.chartSettings = {
      yAxisName: this.yAxisName,
      stack: { 总数: [this.yAxisName] },
      area: true
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
      tooltip: {
        trigger: "axis",
        axisPointer: {
          snap: true,
          label: {
            backgroundColor: "#ccc",
            borderColor: "#aaa",
            borderWidth: 1,
            shadowBlur: 0,
            shadowOffsetX: 0,
            shadowOffsetY: 0,
            color: "#222"
          }
        }
      },
      series: {
        type: "line",
        top: "8%",
        symbolSize: 10,
        itemStyle: {
          color: this.color ? this.color : "#5B86E5"
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: this.subColor ? this.subColor : "#16dbd1"
            },
            {
              offset: 1,
              color: this.color ? this.color : "#5B86E5"
            }
          ])
        }
      }
    };
    return {
      chartData: {}
    };
  },
  created() {
    this.chartData = {
      columns: this.column ? ["月份", this.column] : [],
      rows: this.rows ? this.rows : []
    };
  }
};
</script>

<style scoped>
div {
  margin: 0;
  position: relative;
  width: 99%;
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
