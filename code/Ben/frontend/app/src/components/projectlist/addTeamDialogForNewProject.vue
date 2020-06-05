<template>
  <v-dialog v-model="dialog" max-width="600">
    <v-card>
      <v-img
        height="200"
        src="../../../public/img/projectlist/teamlist.png"
        lazy-src="../../../public/img/projectlist/teamlist.png"
      ></v-img>
      <v-card-title>
        选择创建项目的团队
      </v-card-title>
      <v-card-subtitle>
        在创建项目前先选择一个团队吧
      </v-card-subtitle>
      <v-card-text>
        <v-container class="mt-3">
          <v-autocomplete
            v-model="formData.team"
            label="选择你的团队"
            :items="teamList"
            :error-messages="selectError"
            auto-select-first
            hide-no-data
            outlined
            required
            @change="$v.formData.team.$touch()"
            @blur="$v.formData.team.$touch()"
            dense
          >
          </v-autocomplete>
        </v-container>
      </v-card-text>
      <v-card-actions class="pa-5">
        <v-spacer></v-spacer>
        <v-btn
          color="red darken-1"
          text
          @click="handleClose"
          :disabled="isDisable"
          >关闭
        </v-btn>
        <v-btn
          color="blue darken-1"
          text
          :loading="isDisable"
          @click="handleNext"
          >下一步
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { eventBus } from "../../bus";
import { ADD_TEAM_DIALOG_FOR_NEW_PROJECT } from "../../common/components/Constant";
import { required } from "vuelidate/lib/validators";
import { validationMixin } from "vuelidate";
export default {
  name: "addTeamDialogForNewProject",
  mixins: [validationMixin],
  validations: {
    formData: {
      team: {
        required
      }
    }
  },
  data: () => ({
    dialog: false,
    isDisable: false,
    teamList: [],
    formData: {
      team: null
    }
  }),
  methods: {
    fetchData() {
      this.teamList = this.$store.getters.getSimpleTeamList.map(e => e.name);
    },
    handleClose() {
      this.formData.team = null;
      this.$v.$reset();
      this.dialog = false;
    },
    handleNext() {
      this.$v.formData.team.$touch();
      if (this.$v.formData.team.$invalid) {
        return false;
      }
      const teamId = this.simpleTeamList.find(
        e => e.name === this.formData.team
      ).id;
      this.handleClose();
      eventBus.$emit("showProjectDialog", {
        type: "create",
        index: true,
        teamId: teamId
      });
    }
  },
  computed: {
    selectError() {
      const errors = [];
      if (!this.$v.formData.team.$dirty) return errors;
      !this.$v.formData.team.required &&
        errors.push(ADD_TEAM_DIALOG_FOR_NEW_PROJECT.NOT_SELECT_TEAM_NAME);
      return errors;
    },
    simpleTeamList() {
      return this.$store.getters.getSimpleTeamList;
    }
  },
  watch: {
    simpleTeamList() {
      this.fetchData();
    }
  },
  created() {
    this.fetchData();
    eventBus.$on("showAddTeamDialogForNewProjectDialog", message => {
      this.dialog = message;
    });
  },
  beforeDestroy() {
    eventBus.$off("showAddTeamDialogForNewProjectDialog");
  }
};
</script>

<style scoped></style>
