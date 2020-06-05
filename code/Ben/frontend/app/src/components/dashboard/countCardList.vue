<template>
  <v-row>
    <Permission :perm="getTeamView">
      <v-col class="col-12" align="center">
        <router-link to="/teamList">
          <v-card
            :class="
              dark
                ? 'ma-2 pa-3 hvr-grow-shadow'
                : 'ma-2 pa-3 hvr-grow-shadow team-box'
            "
            min-width="450px"
            max-width="500px"
            :dark="dark"
          >
            <p>
              <v-icon size="45" dark>mdi-graph</v-icon>
            </p>
            <p class="text">团队数</p>
            <p class="count">
              {{ totalCount.teamCount }}
            </p>
          </v-card>
        </router-link>
      </v-col>
    </Permission>

    <Permission :perm="getProjectView">
      <v-col class="col-12" align="center">
        <router-link to="/projectList">
          <v-card
            :class="
              dark
                ? 'ma-2 pa-3 hvr-grow-shadow'
                : 'ma-2 pa-3 hvr-grow-shadow project-box'
            "
            min-width="450px"
            max-width="500px"
            :dark="dark"
          >
            <p>
              <v-icon size="45" color="white">mdi-book</v-icon>
            </p>
            <p class="text">项目数</p>
            <p class="count">
              {{ totalCount.projectCount }}
            </p>
          </v-card>
        </router-link>
      </v-col>
    </Permission>

    <Permission :perm="getViewEmployeePerm">
      <v-col class="col-12" align="center">
        <router-link to="/members">
          <v-card
            :class="
              dark
                ? 'ma-2 pa-3 hvr-grow-shadow'
                : 'ma-2 pa-3 hvr-grow-shadow member-box'
            "
            min-width="450px"
            max-width="500px"
            :dark="dark"
          >
            <p>
              <v-icon size="45" color="white">
                mdi-account-group-outline
              </v-icon>
            </p>
            <p class="text">成员数</p>
            <p class="count">
              {{ totalCount.memberCount }}
            </p>
          </v-card>
        </router-link>
      </v-col>
    </Permission>
  </v-row>
</template>

<script>
import { mapGetters } from "vuex";
import { EMPLOYEE_PERM, PROJECT_PERM, TEAM_PERM } from "../../utils/permission";
export default {
  name: "countCardList",
  computed: {
    ...mapGetters({
      totalCount: "getTotalCount",
      dark: "getDark"
    }),
    getViewEmployeePerm() {
      return EMPLOYEE_PERM.VIEW_EMPLOYEE;
    },
    getTeamView() {
      return TEAM_PERM.VIEW_TEAM;
    },
    getProjectView() {
      return PROJECT_PERM.VIEW_PROJECT;
    }
  }
};
</script>

<style lang="scss" scoped>
@mixin project {
  background: #005c97;
  background: -webkit-linear-gradient(to right, #5b86e5, #36d1dc);
  background: linear-gradient(to right, #5b86e5, #36d1dc);
}

@mixin team {
  background: #ff8008;
  background: -webkit-linear-gradient(to right, #fc670d, #ff8008);
  background: linear-gradient(to right, #fc670d, #ff8008);
}

@mixin member {
  background: #6a3093;
  background: -webkit-linear-gradient(to right, #834d9b, #d04ed6);
  background: linear-gradient(to right, #834d9b, #d04ed6);
}

div {
  margin: 0;
  padding: 0;
}

a {
  color: white;
}

p {
  color: white;
}

.myteam {
  @include font-bold;
  font-size: 20px;
  color: black;
}

.count {
  @include font-bold;
  font-size: 30px;
}

.project-box {
  @include project;
}

.team-box {
  @include member;
}

.member-box {
  @include team;
  text-shadow: $text-shadow;
}
</style>
