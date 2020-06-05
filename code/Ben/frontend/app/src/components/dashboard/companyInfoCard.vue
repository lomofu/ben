<template>
  <div>
    <v-subheader class="ma-3" style="font-weight: bold;font-size: 22px;">
      公司概览
    </v-subheader>
    <v-row justify="center">
      <v-card
        class="ma-8"
        min-width="340"
        width="460"
        max-width="500"
        :elevation="6"
      >
        <v-row class="mb-1">
          <v-img
            contain
            :lazy-src="
              getCompany.photo === undefined
                ? 'https://ben-k8s.oss-cn-shanghai.aliyuncs.com/ben/common/icon-color.png?x-oss-process=style/mystyle'
                : getCompany.photo
            "
            :src="
              getCompany.photo === undefined
                ? 'https://ben-k8s.oss-cn-shanghai.aliyuncs.com/ben/common/icon-color.png?x-oss-process=style/mystyle'
                : getCompany.photo
            "
            width="100"
            aspect-ratio="3"
          >
            <template v-slot:placeholder>
              <v-row class="fill-height ma-0" align="center" justify="center">
                <v-progress-circular
                  indeterminate
                  color="amber"
                ></v-progress-circular>
              </v-row>
            </template>
          </v-img>
        </v-row>
        <v-divider></v-divider>
        <v-card-title>
          {{ getCompany.name }}
        </v-card-title>

        <v-card-subtitle>
          <p v-if="getCompany.type === 1">
            <v-icon class="mr-3 mb-1 mt-2">
              mdi-domain
            </v-icon>
            类型: 公司
          </p>
          <p v-else>
            <v-icon color="purple" class="mr-3 mt-2">
              mdi-account-group
            </v-icon>
            类型: 团队
          </p>
          <p>
            <v-icon color="red" class="mr-3">
              mdi-map-marker
            </v-icon>
            地址:
            {{ getCompany.address === "" ? "暂无地址" : getCompany.address }}
          </p>
        </v-card-subtitle>

        <v-card-actions>
          <Permission :perm="getCompanyEdit">
            <v-btn text @click="handleOpenCompanyEditDialog">
              <v-icon left>mdi-pencil</v-icon>修改信息
            </v-btn>
          </Permission>
          <v-spacer></v-spacer>
          <v-btn @click="show = !show" class="ma-2" tile text color="purple">
            <v-icon left>
              {{ show ? "mdi-chevron-up" : "mdi-chevron-down" }}
            </v-icon>
            更多信息
          </v-btn>
        </v-card-actions>

        <v-expand-transition>
          <div v-show="show">
            <v-divider></v-divider>
            <v-card-text>
              <p class="mb-3">
                Id: <span class="font-italic">{{ getCompany.id }}</span>
              </p>
              <p class="mb-3">
                简介:
                <span class="font-italic">{{
                  getCompany.description === ""
                    ? "暂无简介"
                    : getCompany.description
                }}</span>
              </p>
              <p class="mb-3">
                创建时间:
                <span class="font-italic">
                  {{ $moment(getCompany.createTime).format("lll") }}
                </span>
              </p>
              <p class="mb-3">
                修改时间:
                <span class="font-italic">
                  {{ $moment(getCompany.updateTime).format("lll") }}
                </span>
              </p>
            </v-card-text>
          </div>
        </v-expand-transition>
      </v-card>
    </v-row>
  </div>
</template>

<script>
import { eventBus } from "../../bus";
import { COMPANY_PERM } from "../../utils/permission";
export default {
  name: "companyInfoCard",
  data: () => ({
    show: false
  }),
  methods: {
    handleOpenCompanyEditDialog() {
      eventBus.$emit("showCompanyEditDialog", {
        index: true
      });
    }
  },
  computed: {
    getCompany() {
      return this.$store.getters.getCompany;
    },
    getCompanyEdit() {
      return COMPANY_PERM.EDIT_COMPANY;
    }
  }
};
</script>

<style scoped></style>
