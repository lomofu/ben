<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" max-width="600" persistent no-click-animation>
      <v-card>
        <v-img
          height="200"
          src="../../../../public/img/left/teams.png"
          lazy-src="../../../../public/img/left/teams.png"
        ></v-img>
        <v-card-title>
          <span class="headline">
            {{ type === "create" ? "创建团队!" : "修改团队信息" }}
          </span>
          <v-list-item-subtitle class="blue--text">
            {{
              type === "create"
                ? "创建属于你的管理团队吧!"
                : "仔细修改一下团队的信息"
            }}
          </v-list-item-subtitle>
          <v-list-item-subtitle>
            <small class="grey--text">
              团队是一个或者多个项目的集合,如果您的类型是公司,请创建一个团队来管理您的项目
            </small>
          </v-list-item-subtitle>
        </v-card-title>
        <v-card-text>
          <v-container class="mt-3">
            <v-form max-width="25vw" ref="form">
              <v-text-field
                label="* 团队名称"
                v-model.trim="formData.name"
                :counter="formData.name === null ? false : 20"
                :error-messages="nameErrors"
                @input="$v.formData.name.$touch()"
                @blur="$v.formData.name.$touch()"
                clear-icon="mdi-close"
                clearable
                outlined
                required
              >
              </v-text-field>
              <v-textarea
                label="团队描述"
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
              >
              </v-textarea>
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
  name: "teamDialog",
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
    type: null,
    formData: {
      name: "",
      description: "",
      createTime: null,
      updateTime: null,
      id: "",
      accountId: ""
    }
  }),
  methods: {
    ...mapMutations({
      increaseTeamCount: "increaseTeamCount"
    }),
    ...mapActions({
      fetchMenuData: "fetchMenuData",
      fetchSimpleTeamList: "fetchSimpleTeamList"
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
          formData.companyId = this.company.id;
          formData.accountId = this.account.id;
          const {
            data: { code, msg }
          } = await this.$api.team.createNewTeam(formData);
          if (code === REQUEST.SUCCESS.code) {
            this.fetchMenuData(formData.accountId);
            this.increaseTeamCount();
            eventBus.$emit("fetchTeamList");
            this.fetchSimpleTeamList(this.account.id);
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
          formData.companyId = this.company.id;
          const {
            data: { code, msg }
          } = await this.$api.team.updateTeam(formData);
          if (code === REQUEST.SUCCESS.code) {
            this.fetchMenuData(this.account.id);
            if (this.$route.path === "/teamList") {
              eventBus.$emit("fetchTeamList");
            }
            if (this.$route.path === "/projectList") {
              this.fetchSimpleTeamList(this.account.id);
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
    this.formData.accountId = this.account.id;
    eventBus.$on("showTeamDialog", message => {
      this.dialog = message.index;
      this.type = message.type;
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
    eventBus.$off("showTeamDialog");
  }
};
</script>
