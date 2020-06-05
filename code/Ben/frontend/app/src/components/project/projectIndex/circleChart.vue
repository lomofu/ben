<template>
  <div>
    <v-row justify="center mb-3">
      <v-col cols="12" xs="12" sm="12" md="5" lg="5" xl="5">
        <v-card-subtitle>
          任务总数:
        </v-card-subtitle>
        <v-card-title
          class="d-flex justify-center mt-2"
          style="font-size: 50px"
        >
          {{ total }}
        </v-card-title>
      </v-col>
      <v-divider vertical class="ma-3"></v-divider>
      <v-col cols="12" xs="12" sm="12" md="5" lg="5" xl="5">
        <div class="d-flex justify-center" style="position: relative">
          <div
            v-if="chartData.rows[0].数量 <= 0 && chartData.rows[1].数量 <= 0"
          >
            未发布任务:
          </div>
          <div
            class="data-empty mt-4"
            v-if="chartData.rows[0].数量 <= 0 && chartData.rows[1].数量 <= 0"
          >
            暂无数据绘制图像
          </div>
          <div v-else>
            <ve-ring
              height="140px"
              :data="chartData"
              :judge-width="true"
              :change-delay="200"
              :width-change-delay="200"
              :extend="extend"
              :toolbox="toolbox"
              :loading="loading"
              :afterConfig="afterConfig"
              :colors="colors"
            ></ve-ring>
          </div>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { REQUEST } from "../../../common/view/Constant";

export default {
  name: "circleChart",
  data() {
    this.colors = [
      "#b13c5b",
      "#2f4554",
      "#61a0a8",
      "#d48265",
      "#91c7ae",
      "#749f83",
      "#ca8622",
      "#bda29a",
      "#6e7074",
      "#546570",
      "#c4ccd3"
    ];
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
      grid: {
        top: 100
      },
      series: {
        radius: ["50%", "80%"],
        center: ["50%", "60%"],
        labelLine: {
          show: false
        },
        label: {
          show: true
        }
      }
    };
    return {
      chartData: {
        columns: ["状态", "数量"],
        rows: [
          { 状态: "已发布", 数量: 0 },
          { 状态: "未发布", 数量: 0 }
        ]
      },
      dataEmpty: true,
      loading: true,
      total: 0
    };
  },
  methods: {
    afterConfig(option) {
      this.loading = false;
      return option;
    },
    getRenderData(data) {
      let rows = this.chartData.rows;
      rows[0].数量 = data.pubCount;
      rows[1].数量 = data.unPubCount;
      this.total = data.total;
    },
    async fetchData() {
      const { id } = this.$route.params;
      const {
        data: { code, data }
      } = await this.$api.job.getTotalJobCount(id);
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
  width: 99%;
}
.data-empty {
  z-index: 2;
  min-height: 90px;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.7);
}
</style>
