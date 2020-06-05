<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" max-width="600" persistent no-click-animation>
      <v-card>
        <v-img
          height="200"
          src="../../../../public/img/left/projects.png"
          lazy-src="../../../../public/img/left/projects.png"
        ></v-img>
        <v-card-title>
          <span class="headline">
            {{ type === "create" ? "创建项目!" : "修改项目信息" }}
          </span>
          <v-list-item-subtitle class="blue--text">
            {{
              type === "create"
                ? "创建属于你的项目吧!"
                : "仔细修改一下项目的信息"
            }}
          </v-list-item-subtitle>
          <v-list-item-subtitle>
            <small class="grey--text">
              项目隶属于团队之下,项目是企业管理的虽小单位,通过项目创建和管理排班任务
            </small>
          </v-list-item-subtitle>
        </v-card-title>
        <v-card-text>
          <v-container class="mt-3">
            <v-form max-width="25vw" ref="form">
              <v-text-field
                label="* 项目名称"
                v-model.trim="formData.name"
                :counter="formData.name === null ? false : 20"
                :error-messages="nameErrors"
                @input="$v.formData.name.$touch()"
                @blur="$v.formData.name.$touch()"
                clear-icon="mdi-close"
                clearable
                outlined
                required
              ></v-text-field>
              <v-textarea
                label="项目描述"
                v-model.trim="formData.description"
                :counter="formData.description === null ? false : 100"
                :error-message="descriptionErrors"
                @input="$v.formData.description.$touch()"
                @blur="$v.formData.description.$touch()"
                :background-color="dark ? '' : 'yellow lighten-5'"
                clear-icon="mdi-close"
                clearable
                outlined
                auto-grow
              ></v-textarea>
            </v-form>
          </v-container>
          <v-list-item-subtitle class="grey--text">
            <p>
              <small v-if="type !== 'create'">
                创建时间:{{ $moment(formData.createTime).format("lll") }}
              </small>
            </p>
            <p>
              <small v-if="type !== 'create'">
                修改时间:{{ $moment(formData.updateTime).format("lll") }}
              </small>
            </p>
            <br />
          </v-list-item-subtitle>
          <small class="red--text">* 表示内容必填</small>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="red darken-1"
            text
            @click="handleClose"
            :disabled="isDisable"
            >关闭
          </v-btn>
          <v-btn
            v-if="type === 'create'"
            color="blue darken-1"
            text
            :loading="isDisable"
            @click="handleSave"
            >创建
          </v-btn>
          <v-btn v-else color="blue darken-1" text @click="handleUpdate"
            >修改
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>
<script>
import { eventBus } from "../../../bus";
import { LEFT_NAV } from "../../../common/components/Constant";
import { validationMixin } from "vuelidate";
import { maxLength, required } from "vuelidate/lib/validators";
import { REQUEST } from "../../../common/view/Constant";
import { mapActions, mapGetters, mapMutations } from "vuex";

export default {
  name: "projectDialog",
  mixins: [validationMixin],
  validations: {
    formData: {
      name: {
        required,
        maxLength: maxLength(20)
      },
      description: {
        maxLength: maxLength(100)
      }
    }
  },
  data: () => ({
    isDisable: false,
    dialog: false,
    type: "create",
    formData: {
      name: "",
      description: "",
      createTime: null,
      updateTime: null,
      teamId: "",
      id: ""
    }
  }),
  methods: {
    ...mapMutations({
      increaseProjectCount: "increaseProjectCount"
    }),
    ...mapActions({
      fetchMenuData: "fetchMenuData"
    }),
    validateData() {
      const validate = this.$v.formData;
      validate.$touch();
      return validate.name.$invalid || validate.description.$invalid;
    },
    handleClose() {
      this.dialog = false;
      this.$refs.form.reset();
      this.$v.$reset();
    },
    async handleSave() {
      this.isDisable = true;
      if (this.validateData()) {
        this.isDisable = false;
        return false;
      } else {
        try {
          const formData = JSON.parse(JSON.stringify(this.formData));
          formData.accountId = this.account.id;
          const {
            data: { code, msg }
          } = await this.$api.project.createNewProject(formData);
          if (code === REQUEST.SUCCESS.code) {
            this.fetchMenuData(formData.accountId);
            this.increaseProjectCount();
            if (this.$route.path === "/projectList") {
              eventBus.$emit("fetchProjectList");
            }
            this.handleClose();
            this.$notification.success(code, msg);
          }
        } finally {
          this.isDisable = false;
        }
      }
    },
    async handleUpdate() {
      if (this.validateData()) {
        this.isDisable = false;
        return false;
      } else {
        try {
          this.colverNullToString();
          const formData = JSON.parse(JSON.stringify(this.formData));
          const {
            data: { code, msg }
          } = await this.$api.project.updateProject(formData);
          if (code === REQUEST.SUCCESS.code) {
            this.fetchMenuData(this.account.id);
            if (this.$route.path === "/projectList") {
              eventBus.$emit("fetchProjectList");
            }
            if (
              // eslint-disable-next-line no-useless-escape
              this.$route.path.match(/^\/projects\/((?:[^\/]+?))(?:\/(?=$))?$/i)
            ) {
              eventBus.$emit("reloadProjectInfo");
            }
            this.handleClose();
            this.$notification.success(code, msg);
          }
        } finally {
          this.isDisable = false;
        }
      }
    },
    colverNullToString() {
      const formData = this.formData;
      if (formData.description === null) formData.description = "";
    }
  },
  computed: {
    nameErrors() {
      const errors = [];
      if (!this.$v.formData.name.$dirty) return errors;
      !this.$v.formData.name.required &&
        errors.push(LEFT_NAV.NAME.NAME_IS_EMPTY);
      !this.$v.formData.name.maxLength &&
        errors.push(LEFT_NAV.NAME.NAME_IS_OUT_LIMIT);
      return errors;
    },
    descriptionErrors() {
      const errors = [];
      if (!this.$v.formData.description.$dirty) return errors;
      !this.$v.formData.description.maxLength &&
        errors.push(LEFT_NAV.DESCRIPTION.DES_IS_OUT_LIMIT);
      return errors;
    },
    ...mapGetters({
      menu: "getMenu",
      company: "getCompany",
      account: "getAccount",
      dark: "getDark"
    })
  },
  created() {
    eventBus.$on("showProjectDialog", message => {
      this.dialog = message.index;
      this.type = message.type;
      this.formData.teamId = message.teamId;
      if (message.data) {
        this.formData.id = message.data.id;
        this.formData.name = message.data.name;
        this.formData.description = message.data.description;
        this.formData.createTime = message.data.createTime;
        this.formData.updateTime = message.data.updateTime;
      }
    });
  },
  beforeDestroy() {
    eventBus.$off("showProjectDialog");
  }
};
</script>
