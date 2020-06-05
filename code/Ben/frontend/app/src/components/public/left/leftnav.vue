<template>
  <v-card>
    <v-navigation-drawer
      app
      mobile-break-point="1240"
      :expand-on-hover="getMini"
      class="user-slice"
      :mini-variant.sync="getMini"
      :dark="dark"
    >
      <slot></slot>
      <template v-slot:prepend>
        <v-row align="center" class="app-nav-header">
          <v-col class="col-12">
            <v-list-item class="justify-center ma-1">
              <div class="app-word mt-5 ">
                Ben.
              </div>
            </v-list-item>
          </v-col>
        </v-row>
      </template>

      <v-row style="cursor: pointer" class="pa-6 mb-2">
        <v-col class="col-12">
          <v-tooltip right>
            <template v-slot:activator="{ on }">
              <v-list-item class="d-flex justify-center" v-on="on">
                <v-list-item-avatar size="50" @click="showOpen">
                  <v-img
                    class="hvr-grow-shadow"
                    :lazy-src="account.avatarUrl"
                    :src="account.avatarUrl"
                  >
                    <template v-slot:placeholder>
                      <v-row
                        class="fill-height ma-0"
                        align="center"
                        justify="center"
                      >
                        <v-progress-circular
                          indeterminate
                          color="amber"
                        ></v-progress-circular>
                      </v-row>
                    </template>
                  </v-img>
                </v-list-item-avatar>
                <v-list-item-content @click="showOpen">
                  <v-list-item-title>{{ account.name }}</v-list-item-title>
                  <v-list-item-subtitle
                    >{{ account.email }}
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </template>

            <v-card class="mx-auto pa-5" max-width="375">
              <div class="d-flex justify-center">
                <v-img
                  class="hvr-grow-shadow d-flex"
                  height="150"
                  :lazy-src="account.avatarUrl"
                  :src="account.avatarUrl"
                >
                  <template v-slot:placeholder>
                    <v-row
                      class="fill-height ma-0"
                      align="center"
                      justify="center"
                    >
                      <v-progress-circular
                        indeterminate
                        color="amber"
                      ></v-progress-circular>
                    </v-row>
                  </template>

                  <v-row
                    class="d-flex justify-center pa-2"
                    style="background: rgba(32,32,32,0.48)"
                  >
                    <v-card-title dark class="white--text">
                      信息概况
                    </v-card-title>
                  </v-row>
                  <v-spacer></v-spacer>
                  <v-row
                    class="fill-height d-flex justify-center"
                    style="background: rgba(162,162,162,0.4)"
                  >
                    <v-card-title class="white--text">
                      <div class="">
                        {{ account.name }}
                      </div>
                    </v-card-title>
                  </v-row>
                </v-img>
              </div>

              <v-list two-line>
                <v-list-item class="d-flex justify-center">
                  <v-chip class="ma-2" color="primary" label>
                    <v-icon left>mdi-account-circle-outline</v-icon>
                    {{ account.admin ? "管理员" : account.role[0] }}
                  </v-chip>
                </v-list-item>
                <v-list-item>
                  <v-list-item-icon>
                    <v-icon color="indigo">mdi-cellphone</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title
                      >{{ account.phoneNumber }}
                    </v-list-item-title>
                    <v-list-item-subtitle>手机</v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
                <v-divider inset></v-divider>
                <v-list-item>
                  <v-list-item-icon>
                    <v-icon color="indigo">mdi-email</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title>{{ account.email }}</v-list-item-title>
                    <v-list-item-subtitle>邮箱</v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
                <v-divider inset></v-divider>
                <v-list-item>
                  <v-list-item-icon>
                    <v-icon color="indigo">mdi-clock-outline</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title
                      >{{ $moment(account.createTime).format("lll") }}
                    </v-list-item-title>
                    <v-list-item-subtitle>创建时间</v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </v-card>
          </v-tooltip>
        </v-col>
      </v-row>

      <v-list-item
        class="pa-3"
        style="cursor: pointer"
        @click="$router.push({ path: '/' })"
      >
        <v-list-item>
          <v-icon size="30">mdi-apple-safari</v-icon>
          <v-list-item-title class="ma-5">个人面板</v-list-item-title>
        </v-list-item>
      </v-list-item>
      <v-list-item
        class="pa-3"
        style="cursor: pointer"
        @click="$router.push({ path: '/myjob' })"
      >
        <v-list-item>
          <v-icon size="30">mdi-clipboard-list-outline</v-icon>
          <v-list-item-title class="ma-5">我的排班</v-list-item-title>
        </v-list-item>
      </v-list-item>
      <br />
      <v-list-item class="mb-2">
        <v-list-item-title class="ma-4">
          <span style="font-size: 16px">我的团队</span>
        </v-list-item-title>
        <Permission :perm="getTeamAdd">
          <v-list-item-action>
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn v-on="on" class="mr-10" icon>
                  <v-icon color="grey lighten-1" @click="handleCreateNewTeam"
                    >mdi-account-multiple-plus
                  </v-icon>
                </v-btn>
              </template>
              <span class="pa-3">创建团队</span>
            </v-tooltip>
          </v-list-item-action>
        </Permission>
      </v-list-item>
      <div v-if="menu">
        <v-list v-for="(item, index) in menu" :key="index">
          <v-list-group prepend-icon="mdi-account-group">
            <template v-slot:activator>
              <v-list-item class="edit-title">
                <v-list-item-title class="mr-3"
                  ><p>{{ item.teamName }}</p>
                </v-list-item-title>
                <Permission :perm="getTeamEdit">
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                      <v-btn v-on="on" icon class="edit-btn mr-1">
                        <v-icon
                          class="animated fadeIn"
                          color="blue lighten-1"
                          @click="handleEditTeam(item)"
                          small
                          >mdi-pencil
                        </v-icon>
                      </v-btn>
                    </template>
                    <span class="pa-3">编辑团队</span>
                  </v-tooltip>
                </Permission>
                <Permission :perm="getTeamDel">
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                      <v-btn icon class="edit-btn" v-on="on">
                        <v-icon
                          class="animated fadeIn"
                          color="red lighten-1"
                          @click="handleDeleteTeam(item.teamId)"
                          small
                          >mdi-trash-can
                        </v-icon>
                      </v-btn>
                    </template>
                    <span class="pa-3">删除团队</span>
                  </v-tooltip>
                </Permission>
              </v-list-item>
            </template>
            <v-list-group
              sub-group
              no-action
              v-for="(el, i) in item.children"
              :key="i"
            >
              <template v-slot:activator>
                <v-list-item-content>
                  <v-list-item v-if="el.link === undefined">
                    <v-list-item-title>
                      {{ el.name }}
                    </v-list-item-title>
                    <Permission :perm="getProjectAdd">
                      <v-list-item-action>
                        <v-tooltip bottom>
                          <template v-slot:activator="{ on }">
                            <v-btn class="mr-3" v-on="on" icon>
                              <v-icon
                                color="grey lighten-1"
                                @click="handleCreateNewProject(item.teamId)"
                                small
                                >mdi-shape-square-plus
                              </v-icon>
                            </v-btn>
                          </template>
                          <span class="pa-3">创建项目</span>
                        </v-tooltip>
                      </v-list-item-action>
                    </Permission>
                  </v-list-item>
                  <div v-else>
                    <v-list-item-title
                      @click="
                        $router.push({ path: `${el.link}/${item.teamId}` })
                      "
                    >
                      <span>{{ el.name }}</span>
                    </v-list-item-title>
                  </div>
                </v-list-item-content>
              </template>
              <v-list-item
                link
                v-for="(child, j) in el.children"
                :key="j"
                class="edit-title"
                @click="
                  $router.push({
                    path: `${child.link}/${child.projectId}`
                  })
                "
              >
                <v-list-item-title>
                  <p>{{ child.projectName }}</p>
                </v-list-item-title>
                <Permission :perm="getProjectEdit">
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                      <v-btn
                        icon
                        class="edit-btn"
                        @click="handleEditProject(child, item.teamId)"
                      >
                        <v-icon
                          v-on="on"
                          class="animated fadeIn"
                          color="blue lighten-1"
                          small
                          >mdi-pencil
                        </v-icon>
                      </v-btn>
                    </template>
                    <span class="pa-3">编辑项目</span>
                  </v-tooltip>
                </Permission>
                <Permission :perm="getProjectDel">
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                      <v-btn
                        v-on="on"
                        icon
                        class="edit-btn"
                        @click="handleDeleteProject(child.projectId)"
                      >
                        <v-icon
                          class="animated fadeIn"
                          color="red lighten-1"
                          small
                          >mdi-trash-can
                        </v-icon>
                      </v-btn>
                    </template>
                    <span class="pa-3">删除项目</span>
                  </v-tooltip>
                </Permission>
              </v-list-item>
            </v-list-group>
          </v-list-group>
        </v-list>
      </div>

      <v-row style="position: absolute;bottom: 5px">
        <v-menu top absolute>
          <template v-slot:activator="{ on }">
            <v-btn text>
              <v-icon size="30" v-on="on">mdi-settings </v-icon>
            </v-btn>
          </template>
          <v-list class="pa-5">
            <v-list-item @click="handleShowSettingDialog">
              <v-list-item-title>
                <v-icon class="mr-3">mdi-logout</v-icon>
                <span>设置</span>
              </v-list-item-title>
            </v-list-item>
            <v-list-item @click="handleLogout">
              <v-list-item-title>
                <v-icon class="mr-3">mdi-logout</v-icon>
                <span>退出登录</span>
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-row>
    </v-navigation-drawer>
  </v-card>
</template>

<script>
import { eventBus } from "../../../bus";
import { mapGetters, mapMutations } from "vuex";
import {
  ACCOUNT_PERM,
  PROJECT_PERM,
  TEAM_PERM
} from "../../../utils/permission";
import { checkPermission } from "../../../utils/auth/auth";

export default {
  name: "leftnav",
  data: () => ({
    userinfo: true
  }),
  methods: {
    ...mapMutations({
      successMsg: "updateSuccessMsg"
    }),
    showOpen() {
      if (checkPermission(ACCOUNT_PERM.VIEW_ACCOUNT)) {
        eventBus.$emit("openUserInfo", true);
      }
    },
    handleCreateNewTeam() {
      eventBus.$emit("showTeamDialog", { type: "create", index: true });
    },
    handleEditTeam(data) {
      eventBus.$emit("showTeamDialog", {
        type: "edit",
        index: true,
        data: {
          id: data.teamId,
          name: data.teamName,
          description: data.description,
          createTime: data.createTime,
          updateTime: data.updateTime
        }
      });
    },
    handleDeleteTeam(teamId) {
      eventBus.$emit("showDeleteTeamDialog", { data: teamId, index: true });
    },
    handleCreateNewProject(teamId) {
      eventBus.$emit("showProjectDialog", {
        type: "create",
        index: true,
        teamId: teamId
      });
    },
    handleEditProject(data, teamId) {
      eventBus.$emit("showProjectDialog", {
        type: "edit",
        index: true,
        teamId: teamId,
        data: {
          id: data.projectId,
          name: data.projectName,
          description: data.description,
          createTime: data.createTime,
          updateTime: data.updateTime
        }
      });
    },
    handleDeleteProject(projectId) {
      eventBus.$emit("showDeleteProjectDialog", {
        data: projectId,
        index: true
      });
    },
    handleShowSettingDialog() {
      this.$router.push("/setting");
    },
    handleLogout() {
      this.$notification.success("退出成功");
      this.$cookies.remove("ben-user");
      setTimeout(() => window.location.replace(process.env.VUE_APP_URL), 500);
    }
  },
  computed: {
    ...mapGetters({
      mini: "getMiniIndex",
      account: "getAccount",
      menu: "getMenu",
      company: "getCompany",
      dark: "getDark"
    }),
    getMini: {
      get() {
        return this.mini;
      },
      set() {}
    },
    getTeamAdd() {
      return TEAM_PERM.ADD_TEAM;
    },
    getTeamEdit() {
      return TEAM_PERM.EDIT_TEAM;
    },
    getTeamDel() {
      return TEAM_PERM.DEL_TEAM;
    },
    getProjectAdd() {
      return PROJECT_PERM.ADD_PROJECT;
    },
    getProjectEdit() {
      return PROJECT_PERM.EDIT_PROJECT;
    },
    getProjectDel() {
      return PROJECT_PERM.DEL_PROJECT;
    }
  }
};
</script>

<style lang="scss" scoped>
a {
  color: black;
}

p {
  width: 88px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

@include div;
.user-slice {
  min-height: 100vh;
  padding-bottom: 45px;
}

.app-nav-header {
  &::after {
    @include before-after(30px);
  }
}

.app-img {
  margin: 10px 0 0 25px;
}

.app-nav {
  &-avatar {
    margin: {
      left: 20px;
    }
    cursor: pointer;
  }

  &-name {
    overflow: hidden;
    word-break: break-all;
  }
}

.app-content {
  &-title1 {
    @include font-bold;
    font-size: 16px;
  }
}

.app-word {
  @include font-bold;
  font-size: 24px;
}

.p-team {
  @include font-bold;
  font-size: 25px;
  padding: 20px;

  &::before {
    @include before-after(5px);
  }

  &::after {
    @include before-after(5px);
  }
}

.edit {
  &-btn {
    display: none;
  }

  &-title:hover {
    .edit-btn {
      display: inline;
    }
  }
}
</style>
