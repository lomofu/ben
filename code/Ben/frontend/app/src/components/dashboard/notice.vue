<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" no-click-animation persistent max-width="600px">
      <v-card>
        <v-img
          height="140"
          src="../../../public/img/notice/notice.png"
          lazy-src="../../../public/img/notice/notice.png"
        >
        </v-img>
        <v-card-title>
          <v-icon color="orange">mdi-bell-ring</v-icon>
          <span class="headline ml-3">公告</span>
        </v-card-title>
        <v-card-subtitle class="mt-1">
          公告可以通知所有团队的所有成员,实现一键群发
        </v-card-subtitle>
        <v-card-text>
          <v-form ref="form">
            <v-text-field
              :disabled="isDisable"
              label="请输入公告的标题"
              dense
              outlined
              :counter="formData.title === null ? false : 20"
              clearable
              required
              :error-messages="titleErrors"
              clearable-icon="mdi-close"
              v-model.trim="formData.title"
              @input="$v.formData.title.$touch()"
              @blur="$v.formData.title.$touch()"
            ></v-text-field>
            <v-textarea
              :disabled="isDisable"
              label="请输入你想发布的公告"
              auto-grow
              outlined
              :counter="formData.content === null ? false : 200"
              clearable
              required
              :error-messages="contentErrors"
              clearable-icon="mdi-close"
              v-model.trim="formData.content"
              @input="$v.formData.content.$touch()"
              @blur="$v.formData.content.$touch()"
            ></v-textarea>
            <div>选择推送方式:</div>
            <a-popover placement="topLeft" title="推送方式: App">
              <template slot="content">
                <p>App仅在本应用中通知生效,并且用户可以在消息中心查看</p>
              </template>
              <v-checkbox
                color="green"
                dense
                v-model="formData.selected"
                label="应用"
                value="app"
                :disabled="isDisable"
                :error-messages="selectErrors"
              ></v-checkbox>
            </a-popover>
            <a-popover placement="topLeft" title="推送方式: 邮件">
              <template slot="content">
                <p>系统会通过账户绑定的邮箱向指定用户发送公告邮件</p>
              </template>
              <v-checkbox
                dense
                v-model="formData.selected"
                label="邮件"
                value="mail"
                :disabled="isDisable"
                :error-messages="selectErrors"
              ></v-checkbox>
            </a-popover>
            <a-popover placement="topLeft" title="推送方式: 短信">
              <template slot="content">
                <p>系统会通过账户绑定的手机向指定用户发送公告信息标题</p>
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
          </v-form>
          <small class="mt-1">*公告内容不能超过200字</small>
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
            @click="handleSendNotice"
          >
            推送
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>
<script>
import { eventBus } from "../../bus";
import { validationMixin } from "vuelidate";
import { required, maxLength } from "vuelidate/lib/validators";
import { Popover } from "ant-design-vue";
import { REQUEST } from "../../common/view/Constant";
export default {
  name: "notice",
  mixins: [validationMixin],
  validations: {
    formData: {
      title: { required, maxLength: maxLength(20) },
      content: { required, maxLength: maxLength(200) },
      createBy: { required },
      companyId: { required },
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
      title: null,
      content: null,
      createBy: null,
      companyId: null,
      selected: []
    }
  }),
  methods: {
    validateData(type) {
      const errors = [];
      switch (type) {
        case "title": {
          if (!this.$v.formData.title.$dirty) return errors;
          !this.$v.formData.title.maxLength && errors.push("标题不能超过20字");
          !this.$v.formData.title.required && errors.push("标题不能不能为空");
          break;
        }
        case "content": {
          if (!this.$v.formData.content.$dirty) return errors;
          !this.$v.formData.content.maxLength &&
            errors.push("内容最多只能含有200字");
          !this.$v.formData.content.required && errors.push("公告内容不能为空");
          break;
        }
        case "select": {
          if (!this.$v.formData.selected.$dirty) return errors;
          !this.$v.formData.selected.required &&
            errors.push("请至少选择一种推送方式");
          break;
        }
      }
      return errors;
    },
    validateFormData() {
      const validate = this.$v.formData;
      validate.$touch();
      return (
        validate.title.$invalid ||
        validate.content.$invalid ||
        validate.createBy.$invalid ||
        validate.companyId.$invalid ||
        validate.selected.$invalid
      );
    },
    async handleSendNotice() {
      this.isDisable = true;
      if (this.validateFormData()) {
        this.isDisable = false;
        return false;
      }
      eventBus.$emit("pushing", true);
      this.dialog = false;
      try {
        const {
          data: { code, msg }
        } = await this.$api.push.pushNotification(this.formData);
        eventBus.$emit("pushing", false);
        if (REQUEST.SUCCESS.code === code) {
          this.$notification.success(code, msg);
          this.handleClose();
        }
      } finally {
        this.isDisable = false;
      }
    },
    handleClose() {
      this.$refs.form.reset();
      this.$v.formData.$reset();
      this.dialog = false;
    }
  },
  computed: {
    titleErrors() {
      return this.validateData("title");
    },
    contentErrors() {
      return this.validateData("content");
    },
    selectErrors() {
      return this.validateData("select");
    }
  },
  created() {
    eventBus.$on("showNoticeDialog", message => {
      this.formData.createBy = this.$store.getters.getAccount.id;
      this.formData.companyId = this.$store.getters.getCompany.id;
      this.dialog = message;
    });
  }
};
</script>
