<template>
  <v-dialog
    v-model="dialog"
    color="white"
    persistent
    no-click-animation
    max-width="600px"
  >
    <v-card>
      <v-img
        height="200"
        src="../../../../public/img/push/push.png"
        lazy-src="../../../../public/img/push/push.png"
      ></v-img>
      <v-card-title>
        <v-icon color="primary">mdi-cloud-upload</v-icon>
        <span class="headline ml-3">推送排班</span>
      </v-card-title>
      <v-card-subtitle class="mt-1">
        排班将通知本周有排班的成员
      </v-card-subtitle>
      <v-card-text>
        <div>选择推送方式:</div>
        <a-popover placement="topLeft" title="推送方式: 邮件">
          <v-checkbox
            dense
            v-model="formData.selected"
            label="邮件"
            value="mail"
            :disabled="isDisable"
            :error-messages="selectErrors"
          ></v-checkbox>
          <template slot="content">
            <p>系统会通过账户绑定的邮箱向指定用户发送排班邮件</p>
          </template>
        </a-popover>
        <a-popover placement="topLeft" title="推送方式: 短信">
          <template slot="content">
            <p>系统会通过账户绑定的手机向指定用户发送简易的排班信息</p>
          </template>
          <v-checkbox
            color="orange"
            dense
            v-model="formData.selected"
            label="短信"
            value="phone"
            :disabled="isDisable"
            :error-messages="selectErrors"
          ></v-checkbox>
        </a-popover>
        <v-alert v-show="showAlert" dismissible type="warning">
          由于短信运营商限制,目前排班信息不会显示详情,<br />只会显示排班任务名称(限制20字）
        </v-alert>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="red" text @click="handleClose" :disabled="isDisable"
          >关闭</v-btn
        >
        <v-btn
          color="blue darken-1"
          :loading="isDisable"
          text
          @click="handlePush"
        >
          推送
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { Popover } from "ant-design-vue";
import { validationMixin } from "vuelidate";
import { required } from "vuelidate/lib/validators";
import { eventBus } from "../../../bus";
import { REQUEST } from "../../../common/view/Constant";
export default {
  name: "pushSchedule",
  mixins: [validationMixin],
  validations: {
    formData: {
      projectId: { required },
      selected: { required }
    }
  },
  components: {
    aPopover: Popover
  },
  data: () => ({
    dialog: false,
    isDisable: false,
    formData: {
      projectId: null,
      selected: []
    },
    showAlert: false
  }),
  methods: {
    validateFormData() {
      const validate = this.$v.formData;
      validate.$touch();
      return validate.projectId.$invalid || validate.selected.$invalid;
    },
    handleClose() {
      this.formData.projectId = null;
      this.formData.selected = [];
      this.$v.formData.$reset();
      this.dialog = false;
    },
    async handlePush() {
      this.isDisable = true;
      if (this.validateFormData()) {
        this.isDisable = false;
        return false;
      }
      eventBus.$emit("pushing", true);
      this.dialog = false;
      try {
        this.formData.projectId = this.$route.params.id;
        const {
          data: { code, msg }
        } = await this.$api.push.pushSchedule(this.formData);
        eventBus.$emit("pushing", false);
        if (REQUEST.SUCCESS.code === code) {
          this.$notification.success(code, msg);
          this.handleClose();
        }
      } finally {
        this.isDisable = false;
      }
    }
  },
  computed: {
    selectErrors() {
      const errors = [];
      if (!this.$v.formData.selected.$dirty) return errors;
      !this.$v.formData.selected.required &&
        errors.push("请至少选择一种推送方式");
      return errors;
    }
  },
  watch: {
    "formData.selected"(val) {
      if (val.some(e => e === "phone")) {
        this.showAlert = true;
      } else {
        this.showAlert = false;
      }
    }
  },
  created() {
    this.formData.projectId = this.$route.params;
  }
};
</script>

<style scoped></style>
