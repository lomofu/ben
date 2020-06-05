<template>
  <v-row
    :style="{
      'min-height': '450px',
      background: dark ? '#0a0a0a' : ''
    }"
  >
    <v-subheader class="ma-3" style="font-weight: bold;font-size: 22px;">
      结构分布
    </v-subheader>
    <v-card-title class="d-flex justify-center" style="font-weight: bold">
      团队项目树形分布
    </v-card-title>
    <v-card-subtitle class="d-flex justify-center">
      该图展示公司或者团队下的各个团队或者项目的结构分布
    </v-card-subtitle>
    <div
      class="data-empty"
      v-if="getRenderData.rows[0].value[0].children.length <= 0"
    >
      没有数据,无法绘制图形 😂
    </div>
    <div class="ma-10" v-else>
      <div class="ma-5">
        <ve-tree
          :change-delay="200"
          :toolbox="toolbox"
          :data="getRenderData"
          :settings="chartSettings"
          :loading="loading"
          :judge-width="true"
          :width-change-delay="200"
          :afterConfig="afterConfig"
          :extend="extend"
        ></ve-tree>
      </div>
    </div>
  </v-row>
</template>

<script>
import "v-charts/lib/style.css";
export default {
  name: "tree",
  props: {
    dark: {
      type: Boolean,
      default: false
    }
  },
  data() {
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
        top: "20%"
      }
    };
    return {
      chartData: {
        columns: ["name", "value"],
        rows: [
          {
            name: "Tree树",
            value: [
              {
                name: null,
                children: []
              }
            ]
          }
        ]
      },
      chartSettings: {
        seriesMap: {
          Tree树: {
            orient: "vertical",

            label: {
              normal: {
                position: "left",
                verticalAlign: "middle",
                align: "right",
                fontSize: 20
              }
            }
          }
        }
      },
      loading: true
    };
  },
  methods: {
    afterConfig(option) {
      this.loading = false;
      return option;
    },
    transferData(data) {
      this.chartData.rows[0].value[0].children = [];
      let chartData = this.chartData;
      let treeData = chartData.rows[0].value[0].children;
      if (data && data.length && data.length > 0) {
        data.forEach(e => {
          let temp = { name: `团队: ${e.teamName}`, children: [] };
          e.children[0].children.forEach(i => {
            let name = i.projectName;
            temp.children.push({ name: `项目: ${name}` });
          });
          treeData.push(temp);
        });
      }
    }
  },
  computed: {
    getRenderData() {
      let data = this.$store.getters.getMenu;
      this.transferData(data);
      return this.chartData;
    }
  },
  created() {
    this.chartData.rows[0].value[0].name = this.$store.getters.getCompany.name;
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
  min-height: 390px;
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
