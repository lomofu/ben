<template>
  <v-row>
    <v-dialog v-model="dialog" max-width="450">
      <v-card>
        <v-card-title class="headline mb-3">
          <v-icon size="35" class="mr-3" color="red"
            >mdi-alert-circle-outline
          </v-icon>
          <span v-if="type === 'team'">删除这个团队?</span>
          <span v-else>删除这个项目?</span>
        </v-card-title>
        <v-card-text>
          <span v-if="type === 'team'">
            这意味着您将删除这个团队的所有信息,包含该团队下的项目,雇员等重要信息,您确定要删除吗？️
          </span>
          <span v-else>
            这意味着您将删除这个项目的所有信息,包含该项目下的任务等全部信息,您确定要删除吗？️
          </span>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="red darken-1"
            :loading="isDisable"
            text
            @click="handleClose"
          >
            取消
          </v-btn>
          <v-btn
            color="green darken-1"
            :disabled="isDisable"
            text
            @click="handleSure"
          >
            确定
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import { eventBus } from "../../../bus";
import { REQUEST } from "../../../common/view/Constant";
import { mapActions, mapGetters, mapMutations } from "vuex";

export default {
  name: "deleteDialog",
  inject: ["reloadContent"],
  data: () => ({
    isDisable: false,
    type: null,
    dialog: false,
    teamId: null,
    projectId: null
  }),
  methods: {
    ...mapMutations({
      decreaseTeamCount: "decreaseTeamCount",
      decreaseProjectCount: "decreaseProjectCount"
    }),
    ...mapActions({
      fetchMenuData: "fetchMenuData",
      fetchTotalCount: "fetchTotalCount",
      fetchSimpleTeamList: "fetchSimpleTeamList"
    }),
    handleClose() {
      this.dialog = false;
      this.teamId = null;
      this.projectId = null;
    },
    async handleSure() {
      this.isDisable = true;
      try {
        if (this.type === "team") {
          const {
            data: { code, msg }
          } = await this.$api.team.deleteTeam(this.teamId);
          if (code === REQUEST.SUCCESS.code) {
            this.fetchMenuData(this.account.id);
            this.fetchTotalCount({
              accountId: this.account.id,
              companyId: this.company.id
            });
            if (this.$route.path === "/teamList") {
              eventBus.$emit("fetchTeamList");
            }
            if (this.$route.path === "/projectList") {
              this.fetchSimpleTeamList(this.account.id);
            }
            if (this.$route.meta.name === "project") {
              this.reloadContent();
            }
            this.handleClose();
            this.$notification.success(code, msg);
          }
        } else {
          const {
            data: { code, msg }
          } = await this.$api.project.deleteProject(this.projectId);
          if (code === REQUEST.SUCCESS.code) {
            this.fetchMenuData(this.account.id);
            if (this.$route.path === "/projectList") {
              eventBus.$emit("fetchProjectList");
            }
            if (this.$route.meta.name === "project") {
              this.reloadContent();
            }
            this.decreaseProjectCount();
            this.handleClose();
            this.$notification.success(code, msg);
          }
        }
      } finally {
        this.isDisable = false;
      }
    }
  },
  computed: {
    ...mapGetters({
      company: "getCompany",
      account: "getAccount"
    })
  },
  created() {
    eventBus.$on("showDeleteTeamDialog", message => {
      this.type = "team";
      this.dialog = message.index;
      this.teamId = message.data;
    });
    eventBus.$on("showDeleteProjectDialog", message => {
      this.type = "project";
      this.dialog = message.index;
      this.projectId = message.data;
    });
  }
};
</script>
