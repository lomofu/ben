<template>
  <v-row style="min-height: 450px;">
    <v-subheader class="ma-3" style="font-weight: bold;font-size: 22px;">
      团队/项目
    </v-subheader>
    <v-card-title class="d-flex justify-center" style="font-weight: bold">
      团队/项目数
    </v-card-title>
    <v-card-subtitle class="d-flex justify-center">
      该图显示每个团队下的项目总数与其他团队的项目数的比较
    </v-card-subtitle>
    <div class="data-empty" v-if="getRenderData.rows.length === 0">
      没有数据,无法绘制图形 😂
    </div>
    <div class="ma-10" v-else>
      <v-subheader class="d-flex justify-end ml-5">
        <v-btn @click="changeType" text color="#1e73a8">切换图表类型</v-btn>
      </v-subheader>
      <div class="ma-5">
        <ve-chart
          :data="getRenderData"
          :settings="chartSettings"
          :change-delay="200"
          :width-change-delay="200"
          :judge-width="true"
          :xAxis="index === 0 ? null : xAxis"
          :toolbox="toolbox"
          :loading="loading"
          :extend="extend"
          :afterConfig="afterConfig"
          :colors="colors"
        >
        </ve-chart>
      </div>
    </div>
  </v-row>
</template>
<script>
import "v-charts/lib/style.css";
export default {
  name: "overviewChart",
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
    this.xAxis = {
      type: "value",
      minInterval: 1
    };
    this.typeArr = ["pie", "bar"];
    this.index = 0;
    this.toolbox = {
      feature: {
        saveAsImage: { show: true },
        restore: { show: true },
        dataView: { show: true, readOnly: false }
      }
    };
    this.extend = {
      legend: {
        orient: "vertical",
        left: "left"
      },
      series: {
        top: "10%",
        animationType: "scale",
        animationEasing: "elasticOut",
        animationDelay: function(idx) {
          return idx * Math.random() * 200;
        }
      }
    };
    return {
      chartData: {
        columns: ["团队", "项目数"],
        rows: []
      },
      chartSettings: {
        selectedMode: "single",
        type: this.typeArr[this.index],
        radius: 120,
        label:
          this.index === 0
            ? {
                normal: {
                  formatter:
                    "团队: {b}" +
                    "\n\n" +
                    "项目数: {c}" +
                    "\n\n" +
                    "占比:({d}%)"
                }
              }
            : null
      },
      dataEmpty: true,
      loading: true
    };
  },
  methods: {
    changeType() {
      this.index++;
      if (this.index >= this.typeArr.length) {
        this.index = 0;
      }
      this.chartSettings = { type: this.typeArr[this.index], radius: 120 };
    },
    afterConfig(option) {
      this.loading = false;
      return option;
    },
    transferData(data) {
      let charData = this.chartData;
      charData.rows = [];
      data &&
        data.forEach(item => {
          charData.rows.push({
            团队: item.teamName,
            项目数: item.children[0].children.length
          });
        });
    }
  },
  computed: {
    getRenderData() {
      let data = this.$store.getters.getMenu;
      this.transferData(data);
      return this.chartData;
    }
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
