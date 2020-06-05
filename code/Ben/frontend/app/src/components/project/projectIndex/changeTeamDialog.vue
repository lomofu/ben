<template>
  <v-row>
    <v-dialog v-model="dialog" max-width="600">
      <v-card>
        <v-card-title>
          更换团队
        </v-card-title>
        <v-card-text>
          <v-card :color="dark ? 'orange darken-5' : '#FFF59D'" outlined>
            <v-card-title>
              源团队:
            </v-card-title>
            <v-card-text>
              <p class="mb-2">团队ID: {{ project.teamId }}</p>
              <p>团队名称: {{ project.teamName }}</p>
            </v-card-text>
          </v-card>

          <p class="text-center ma-5">
            <v-icon size="50" color="green">mdi-arrow-down-bold</v-icon>
          </p>

          <v-card outlined>
            <v-card-title>
              目标团队:
            </v-card-title>
            <v-card-text>
              团队ID:
              <v-text-field
                v-model="formData.teamId"
                readonly
                solo
                flat
              ></v-text-field>
              <v-autocomplete
                label="选择你的团队"
                v-model="teamName"
                :items="teamList"
                :error-messages="selectError"
                :placeholder="teamList.length <= 0 && '当前无团队可更换'"
                auto-select-first
                cache-items
                hide-no-data
                outlined
                required
                @change="handleChange($event)"
                @blur="$v.formData.teamId.$touch()"
                dense
              >
              </v-autocomplete>
            </v-card-text>
          </v-card>
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
          <v-btn color="primary" :disabled="isDisable" text @click="handleSure">
            更换
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import { ADD_TEAM_DIALOG_FOR_NEW_PROJECT } from "../../../common/components/Constant";
import { required } from "vuelidate/lib/validators";
import { validationMixin } from "vuelidate";
import { eventBus } from "../../../bus";
import { REQUEST } from "../../../common/view/Constant";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "changeTeamDialog",
  mixins: [validationMixin],
  props: {
    data: {
      type: Object,
      required: true
    }
  },
  validations: {
    formData: {
      id: {
        required
      },
      teamId: {
        required
      }
    }
  },
  data: () => ({
    isDisable: false,
    dialog: false,
    project: null,
    teamList: [],
    teamName: null,
    formData: {
      id: null,
      teamId: null
    }
  }),
  methods: {
    ...mapActions({
      fetchMenuData: "fetchMenuData"
    }),
    fetchData() {
      let list = JSON.parse(JSON.stringify(this.simpleTeamList));
      list.splice(
        list.findIndex(e => e.id === this.project.teamId),
        1
      );
      this.teamList = list.map(e => e.name);
    },
    filterItem(val) {
      const list = this.simpleTeamList;
      return list.find(e => e.name === val);
    },
    handleChange($event) {
      this.formData.teamId = this.filterItem($event).id;
    },
    async handleSure() {
      this.formData.id = this.project.id;
      this.$v.formData.$touch();
      if (this.$v.formData.teamId.$invalid) {
        return false;
      }
      try {
        this.isDisable = true;
        const {
          data: { code, msg }
        } = await this.$api.project.updateProjectToOtherTeam(this.formData);
        if (code === REQUEST.SUCCESS.code) {
          eventBus.$emit("reloadProjectInfo");
          this.$notification.success(code, msg);
          this.handleClose();
          this.fetchMenuData(this.account.id);
        }
      } finally {
        this.isDisable = false;
      }
    },
    handleClose() {
      this.formData.id = null;
      this.formData.teamId = null;
      this.teamName = null;
      this.$v.formData.$reset();
      this.dialog = false;
    }
  },
  computed: {
    ...mapGetters({
      account: "getAccount",
      simpleTeamList: "getSimpleTeamList",
      dark: "getDark"
    }),
    selectError() {
      const errors = [];
      if (!this.$v.formData.teamId.$dirty) return errors;
      !this.$v.formData.teamId.required &&
        errors.push(ADD_TEAM_DIALOG_FOR_NEW_PROJECT.NOT_SELECT_TEAM_NAME);
      return errors;
    }
  },
  watch: {
    simpleTeamList() {
      this.fetchData();
    }
  },
  created() {
    this.project = this.data;
    this.fetchData();
    eventBus.$on("showChangeTeamDialog", () => {
      this.dialog = true;
    });
  },
  beforeDestroy() {
    eventBus.$off("showChangeTeamDialog");
  }
};
</script>

<style scoped></style>
