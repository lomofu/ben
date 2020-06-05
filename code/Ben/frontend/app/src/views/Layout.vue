<template>
  <div class="user">
    <Permission :perm="getEditEmployeePerm">
      <changeEmployeeRoleDialog></changeEmployeeRoleDialog>
    </Permission>
    <left-nav>
      <Permission :perm="getTeamEdit">
        <teamDialog></teamDialog>
      </Permission>
      <deleteDialog></deleteDialog>
      <Permission :perm="getProjectEdit">
        <projectDialog></projectDialog>
      </Permission>
    </left-nav>
    <v-content>
      <v-container fluid>
        <transition
          enter-active-class="animated fadeIn"
          leave-active-class="animated fadeOut"
          mode="out-in"
        >
          <router-view v-if="success"></router-view>
        </transition>
      </v-container>
      <benFooter></benFooter>
    </v-content>
    <Permission :perm="getAccountView">
      <user-info
        :user="user"
        :style="{ background: dark ? '#141414' : '' }"
      ></user-info>
    </Permission>
    <Permission :perm="getCompanyEdit">
      <company-dialog></company-dialog>
    </Permission>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import {
  ACCOUNT_PERM,
  COMPANY_PERM,
  EMPLOYEE_PERM,
  PROJECT_PERM,
  TEAM_PERM
} from "../utils/permission";
import { eventBus } from "../bus";

export default {
  name: "UserCenter",
  components: {
    leftNav: () => import("../components/public/left/leftnav.vue"),
    userInfo: () => import("../components/public/right/userInfo.vue"),
    teamDialog: () => import("../components/public/left/teamDialog.vue"),
    projectDialog: () => import("../components/public/left/projectDialog.vue"),
    deleteDialog: () => import("../components/public/left/deleteDialog.vue"),
    companyDialog: () => import("../components/dashboard/companyDialog.vue"),
    benFooter: () => import("../components/public/footer.vue"),
    changeEmployeeRoleDialog: () =>
      import("../components/public/employee/changeEmployeeRoleDialog.vue")
  },
  provide() {
    return {
      reloadContent: this.reloadContent
    };
  },
  data: () => ({
    user: {},
    menu: [],
    success: true
  }),
  methods: {
    ...mapActions({
      fetchMenuData: "fetchMenuData",
      fetchSimpleTeamList: "fetchSimpleTeamList"
    }),
    reloadContent() {
      this.success = false;
      this.$nextTick(function() {
        this.success = true;
      });
    }
  },
  computed: {
    key() {
      return this.$route.path + Math.random();
    },
    getCompany() {
      return this.$store.getters.getCompany;
    },
    getProjectEdit() {
      return PROJECT_PERM.EDIT_PROJECT;
    },
    getCompanyEdit() {
      return COMPANY_PERM.EDIT_COMPANY;
    },
    getTeamEdit() {
      return TEAM_PERM.EDIT_TEAM;
    },
    getAccountView() {
      return ACCOUNT_PERM.VIEW_ACCOUNT;
    },
    getEditEmployeePerm() {
      return EMPLOYEE_PERM.EDIT_EMPLOYEE;
    },
    dark() {
      return this.$store.getters.getDark;
    }
  },
  created() {
    this.fetchSimpleTeamList(this.$store.getters.getAccount.id);
    this.fetchMenuData(this.$store.getters.getAccount.id);
  },
  mounted() {
    const me = this;
    me.$goEasy.subscribe({
      channel: me.getCompany.id,
      onMessage: function(message) {
        const result = JSON.parse(message.content);
        eventBus.$emit("fetchMessage");
        setTimeout(() => {
          me.$notification.success(
            "[推送: 新公告] " + result.title,
            result.content
          );
        }, 1000);
      }
    });
  },
  beforeDestroy() {
    this.$goEasy.disconnect();
  }
};
</script>

<style lang="scss">
@include div;

.user {
  min-width: 100vw;
  min-height: 100vh;
  height: 100%;
}
.container {
  padding: 1px;
}
</style>
