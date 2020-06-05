<template>
  <v-row jusitify="center">
    <v-col class="col-12" align="center">
      <v-card
        class="ma-8 pa-2"
        min-width="350px"
        width="500px"
        max-height="800px"
        :elevation="6"
      >
        <v-card-text :class="changeColor()">
          <v-list-item>
            <v-list-item-content>
              <v-tooltip bottom>
                <template v-slot:activator="{ on }">
                  <v-list-item-title
                    v-on="on"
                    class="d-flex justify-start projectName"
                    style="cursor: pointer"
                    v-clipboard:copy="project.name"
                    v-clipboard:success="onCopy"
                    v-clipboard:error="onError"
                  >
                    {{ project.name }}
                  </v-list-item-title>
                </template>
                <span>点击 复制项目名</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on }">
                  <v-list-item-title
                    v-on="on"
                    class="projectId mt-2"
                    style="cursor: pointer"
                    v-clipboard:copy="project.id"
                    v-clipboard:success="onCopy"
                    v-clipboard:error="onError"
                  >
                    ID: {{ project.id }}
                  </v-list-item-title>
                </template>
                <span>点击 复制项目ID</span>
              </v-tooltip>
            </v-list-item-content>
            <v-list-item-avatar tile size="140">
              <v-icon size="140">mdi-book</v-icon>
            </v-list-item-avatar>
          </v-list-item>
          <v-list-item>
            <p>描述: {{ getDescription }}</p>
          </v-list-item>
        </v-card-text>
        <v-card-text class="d-flex flex-column  justify-start">
          <v-list-item> 团队: {{ project.teamName }} </v-list-item>
          <v-list-item> 总人数: {{ project.total }} </v-list-item>
          <v-list-item> 创建时间: {{ getCreateTime }} </v-list-item>
          <v-list-item> 创建时间: {{ getUpdateTime }} </v-list-item>
        </v-card-text>
        <v-card-actions class="pa-5">
          <Permission :perm="getProjectEdit">
            <v-btn outlined @click="handleEdit">
              <v-icon class="ma-1">mdi-pencil</v-icon>
              修改信息
            </v-btn>
          </Permission>
          <v-spacer></v-spacer>
          <v-btn
            v-if="showMore"
            @click="show = !show"
            class="ma-2"
            tile
            text
            color="purple"
          >
            <v-icon left>
              {{ show ? "mdi-chevron-up" : "mdi-chevron-down" }}
            </v-icon>
            更多操作
          </v-btn>
        </v-card-actions>
        <v-expand-transition>
          <div v-show="show">
            <v-divider></v-divider>
            <v-card-text
              class="d-flex justify-space-between"
              @click.stop="handleChange"
            >
              <Permission :perm="getProjectChangeTeam">
                <v-btn outlined color="orange">
                  <v-icon class="ma-1">
                    mdi-account-switch
                  </v-icon>
                  更换团队
                </v-btn>
              </Permission>
              <Permission :perm="getProjectDel">
                <v-btn outlined color="red" @click.stop="handleDelete">
                  <v-icon class="ma-1">
                    mdi-delete
                  </v-icon>
                  删除项目
                </v-btn>
              </Permission>
            </v-card-text>
          </div>
        </v-expand-transition>
      </v-card>
    </v-col>
    <change-team-dialog :data="project"></change-team-dialog>
  </v-row>
</template>

<script>
import { TIME } from "../../../common/view/Constant";
import { eventBus } from "../../../bus";
import { EMPLOYEE_EDIT_DIALOG } from "../../../common/components/Constant";
import { PROJECT_PERM } from "../../../utils/permission";
import { checkPermission } from "../../../utils/auth/auth";

const color = [
  "card-up-1",
  "card-up-5",
  "card-up-2",
  "card-up-4",
  "card-up-3",
  "card-up-1",
  "card-up-4",
  "card-up-2",
  "card-up-5",
  "card-up-1"
];

export default {
  name: "projectInfoCard",
  props: {
    data: {
      type: Object,
      required: true
    }
  },
  components: {
    changeTeamDialog: () => import("./changeTeamDialog")
  },
  data: () => ({
    project: {},
    show: false
  }),
  methods: {
    changeColor() {
      if (this.getDark) {
        return "dark";
      }
      let index = Math.round(Math.random() * 9);
      return color[index > 9 ? Math.round(Math.random() * 9) : index];
    },
    handleEdit() {
      const data = this.project;
      eventBus.$emit("showProjectDialog", {
        type: "edit",
        index: true,
        teamId: data.teamId,
        data: {
          id: data.id,
          name: data.name,
          description: data.description,
          createTime: data.createTime,
          updateTime: data.updateTime
        }
      });
    },
    handleDelete() {
      const data = this.project;
      eventBus.$emit("showDeleteProjectDialog", {
        data: data.id,
        index: true
      });
    },
    handleChange() {
      eventBus.$emit("showChangeTeamDialog");
    },
    onCopy() {
      this.$notification.success(
        EMPLOYEE_EDIT_DIALOG.CLIPBOARD,
        EMPLOYEE_EDIT_DIALOG.COPY_SUCCESS
      );
    },
    onError() {
      this.$notification.error(
        EMPLOYEE_EDIT_DIALOG.CLIPBOARD,
        EMPLOYEE_EDIT_DIALOG.COPY_FAIL
      );
    }
  },
  computed: {
    getDescription() {
      return this.project.description === null ||
        this.project.description === "" ||
        this.project.description === undefined
        ? "暂无描述"
        : this.project.description;
    },
    getCreateTime() {
      return this.$moment(this.project.createTime).format(TIME.LOCAL_TIME);
    },
    getUpdateTime() {
      return this.$moment(this.project.updateTime).format(TIME.LOCAL_TIME);
    },
    getDark() {
      return this.$store.getters.getDark;
    },
    getProjectEdit() {
      return PROJECT_PERM.EDIT_PROJECT;
    },
    getProjectDel() {
      return PROJECT_PERM.DEL_PROJECT;
    },
    getProjectChangeTeam() {
      return PROJECT_PERM.CHANGE_PROJECT;
    },
    showMore() {
      return (
        checkPermission(PROJECT_PERM.DEL_PROJECT) ||
        checkPermission(PROJECT_PERM.CHANGE_PROJECT)
      );
    }
  },
  watch: {
    data: {
      handler() {
        this.changeColor();
        this.project = this.data;
      },
      deep: true
    }
  },
  created() {
    this.project = this.data;
  }
};
</script>

<style scoped>
p {
  text-align: left;
  overflow: hidden;
}
.projectId {
  font-weight: lighter;
  font-size: 14px;
}
.projectName {
  font-weight: bolder;
  font-size: 30px;
}
.dark {
  background: #141414;
}
.card-up-1 {
  background: #c9d6ff;
  background: -webkit-linear-gradient(to right, #e2e2e2, #c9d6ff);
  background: linear-gradient(to right, #e2e2e2, #c9d6ff);
}
.card-up-2 {
  background: #bdc3c7;
  background: -webkit-linear-gradient(to right, #fffcdc, #d9a7c7);
  background: linear-gradient(to right, #fffcdc, #d9a7c7);
}
.card-up-3 {
  background: #fcb173;
  background: -webkit-linear-gradient(to right, #f7f1ba, #fcb173);
  background: linear-gradient(to right, #f7f1ba, #fcb173);
}
.card-up-4 {
  background: #b9ebe2;
  background: -webkit-linear-gradient(to left, #bcc3e5, #b9ebe2);
  background: linear-gradient(to left, #bcc3e5, #b9ebe2);
}
.card-up-5 {
  background: #ffbea5;
  background: -webkit-linear-gradient(to left, #ffbea5, #bce8d2);
  background: linear-gradient(to left, #ffbea5, #bce8d2);
}
</style>
