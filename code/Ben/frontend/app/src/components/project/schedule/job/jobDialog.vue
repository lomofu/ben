<template>
  <v-dialog
    v-model="dialog"
    max-width="900px"
    color="white"
    persistent
    no-click-animation
  >
    <v-card>
      <v-row>
        <v-col class="col-9">
          <v-card-title>
            <span style="font-size: 24px;font-weight: bold">{{
              getTitle
            }}</span>
            <v-spacer></v-spacer>
            <Permission v-if="type !== 'new'" :perm="getJobDel">
              <v-btn
                color="red"
                outlined
                :loading="isDisable"
                @click="showDeleteDialog"
                >删除此任务</v-btn
              >
            </Permission>
          </v-card-title>
          <v-card-subtitle v-if="canEdit">
            <span>{{ getSubTitle }}</span>
            <br />
            <small class="red--text">* 表示必填</small>
          </v-card-subtitle>
          <v-card-text>
            <v-form class="pa-2" ref="event">
              <v-label v-if="!canEdit">事件名称</v-label>
              <v-text-field
                dense
                :clearable="canEdit"
                clear-icon="mdi-close"
                label="* 事件名称"
                :outlined="canEdit"
                :counter="canEdit && formData.name !== null ? 20 : false"
                required
                :error-messages="nameErrors"
                v-model="formData.name"
                @blur="$v.formData.name.$touch()"
                @input="$v.formData.name.$touch()"
                :readonly="!canEdit"
                :solo="!canEdit"
                :flat="!canEdit"
                :disabled="isDisable"
              ></v-text-field>

              <v-label color="grey">日期</v-label>
              <div class="d-flex justify-sm-space-between mt-2">
                <v-text-field
                  v-if="!canEdit"
                  solo
                  flat
                  readonly
                  :value="getRangeTime"
                ></v-text-field>
                <a-locale-provider v-else :locale="locale">
                  <a-range-picker
                    ref="rangePicker"
                    class="mr-3"
                    size="large"
                    :showTime="getShowTime"
                    :placeholder="getPlaceHolder"
                    :format="getFormat"
                    :value="[timePicker.start, timePicker.end]"
                    @ok="onOk"
                    @change="onChange"
                    :disabled="isDisable"
                  />
                </a-locale-provider>
                <v-checkbox
                  class="ma-1"
                  v-if="canEdit"
                  v-model="isFullDay"
                  label="全天"
                ></v-checkbox>
              </div>
              <a-alert
                class="mb-1"
                v-if="dateErrors.length > 0"
                type="error"
                :message="dateErrors"
                closable
                banner
              />

              <v-label color="grey">属于</v-label>
              <div class="mt-2">
                <v-avatar size="40">
                  <v-img :src="account.avatarUrl" :lazy-src="account.avatarUrl">
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
                </v-avatar>
                <span class="ml-3">{{ account.name }}</span>
              </div>

              <v-divider class="mt-3 mb-4"></v-divider>
              <v-label color="grey">颜色</v-label>
              <div class="mt-1" v-if="!canEdit">
                <v-btn fab small :color="formData.color"></v-btn>
              </div>
              <v-color-picker
                v-else
                class="mt-2"
                hex
                v-on:update:color="updateColor"
                v-model="color"
                hide-canvas
                hide-inputs
                hide-mode-switch
                mode="rgba"
                dot-size="1"
              ></v-color-picker>
              <br />
              <v-textarea
                label="请输入你对此事件的描述"
                auto-grow
                outlined
                :counter="canEdit && formData.name !== null ? 100 : false"
                :clearable="canEdit"
                required
                :error-messages="detailsErrors"
                clearable-icon="mdi-close"
                :readonly="!canEdit"
                v-model="formData.description"
                @input="$v.formData.description.$touch()"
                @blur="$v.formData.description.$touch()"
                :disabled="isDisable"
              ></v-textarea>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="blue darken-1"
              text
              @click="handleCloseDialog"
              :disabled="isDisable"
              >关闭</v-btn
            >
            <Permission v-if="!canEdit" :perm="getJobEdit">
              <v-btn
                color="purple darken-1"
                text
                @click="handelEdit"
                :disabled="isDisable"
              >
                修改
              </v-btn>
            </Permission>
            <Permission v-if="type === 'new'" :perm="getJobAdd">
              <v-btn
                color="red darken-1"
                text
                @click="handleClear"
                :disabled="isDisable"
              >
                清除
              </v-btn>
            </Permission>
            <Permission v-if="canEdit && type !== 'new'" :perm="getJobEdit">
              <v-btn
                color="red darken-1"
                text
                @click="reset"
                :disabled="isDisable"
                >取消</v-btn
              >
            </Permission>
            <v-btn
              v-if="canEdit && checkPerm"
              color="green darken-1"
              text
              @click="handleSubmitEvent"
              :loading="isDisable"
              >保存</v-btn
            >
          </v-card-actions>
        </v-col>

        <v-col class="col-3">
          <v-img
            contain
            src="../../../../../public/img/employee/schedule.png"
          ></v-img>
        </v-col>
      </v-row>
    </v-card>
  </v-dialog>
</template>

<script>
import moment from "moment";
import {
  DatePicker,
  Alert,
  Modal,
  Input,
  LocaleProvider
} from "ant-design-vue";
import { REQUEST, TIME } from "../../../../common/view/Constant";
import { validationMixin } from "vuelidate";
import { required, maxLength } from "vuelidate/lib/validators";
import {
  ACCOUNT_CARD,
  JOB_DIALOG
} from "../../../../common/components/Constant";
import { eventBus } from "../../../../bus";
import time from "../../../../utils/time";
import zhCN from "ant-design-vue/lib/locale-provider/zh_CN";
import cloneDeep from "lodash/cloneDeep";
import { checkPermission } from "../../../../utils/auth/auth";
import { JOB_PERM } from "../../../../utils/permission";

export default {
  name: "jobDialog",
  mixins: [validationMixin],
  validations: {
    formData: {
      name: { required, maxLength: maxLength(20) },
      description: { maxLength: maxLength(100) },
      start: { required },
      end: { required }
    }
  },
  components: {
    aAlert: Alert,
    aRangePicker: DatePicker.RangePicker,
    // eslint-disable-next-line vue/no-unused-components
    antInput: Input,
    aLocaleProvider: LocaleProvider
  },
  data: () => ({
    locale: zhCN,
    type: "new",
    dialog: false,
    canEdit: true,
    isDisable: false,
    isFullDay: false,
    time: null,
    timePicker: {
      start: null,
      end: null
    },
    color: null,
    formData: {
      id: null,
      name: null,
      description: null,
      start: null,
      end: null,
      color: null,
      full: null,
      accountId: null,
      projectId: null
    },
    account: {},
    dateFormat: "YYYY/MM/DD",
    monthFormat: "YYYY/MM"
  }),
  methods: {
    moment,
    updateColor({ hex }) {
      this.formData.color = hex;
    },
    getStartAndEnd(value) {
      this.timePicker = {
        start: value[0],
        end: value[1]
      };
      const start = this.isFullDay
        ? value[0].format(TIME.DEFAULT_DATE)
        : value[0].format(TIME.DEFAULT_TIME);
      const end = this.isFullDay
        ? value[1].format(TIME.DEFAULT_DATE)
        : value[1].format(TIME.DEFAULT_TIME);
      this.formData.start = start;
      this.formData.end = end;
    },
    validateHasChange() {
      const data = this.formData;
      const raw = this.rawJob;
      return (
        data.name !== raw.name ||
        data.color !== raw.color ||
        data.description !== raw.description ||
        data.start !== raw.start ||
        data.end !== raw.end ||
        this.isFullDay !== raw.full
      );
    },
    validateFormData() {
      this.$v.formData.$touch();
      return this.$v.formData.$invalid;
    },
    onChange(value) {
      this.getStartAndEnd(value);
    },
    onOk(value) {
      this.getStartAndEnd(value);
    },
    converseData() {
      const formData = this.formData;
      formData.full = this.isFullDay;
      this.formData.projectId = this.$route.params.id;
      if (this.type === "new") {
        formData.color = this.color.hex;
      }
      formData.projectId = this.$route.params.id;
      if (this.isFullDay) {
        formData.start = time.converseToFullDay(formData.start, true);
        formData.end = time.converseToFullDay(formData.end, false);
      } else {
        formData.start = time.converseToStanderString(formData.start);
        formData.end = time.converseToStanderString(formData.end);
      }
    },
    handleSubmitEvent() {
      this.type === "new"
        ? this.handleSaveNewEvent()
        : this.handleUpdateEvent();
    },
    async handleSaveNewEvent() {
      if (this.validateFormData()) {
        return false;
      } else {
        this.converseData();
        try {
          this.isDisable = true;
          const {
            data: { code, msg }
          } = await this.$api.job.saveJob(this.formData);
          if (code === REQUEST.SUCCESS.code) {
            this.$notification.success(code, msg);
            this.handleCloseDialog();
            eventBus.$emit("refreshEventList");
          }
        } finally {
          this.isDisable = false;
        }
      }
    },
    async handleUpdateEvent() {
      if (this.validateHasChange()) {
        if (this.validateFormData()) {
          return false;
        } else {
          this.converseData();
          try {
            this.isDisable = true;
            const {
              data: { code, msg }
            } = await this.$api.job.updateJob(this.formData);
            if (code === REQUEST.SUCCESS.code) {
              this.$notification.success(code, msg);
              this.handleCloseDialog();
              eventBus.$emit("refreshEventList");
            }
          } finally {
            this.isDisable = false;
          }
        }
      } else {
        this.$notification.error(
          ACCOUNT_CARD.INFO.IS_NOT_CHANGE.TITLE,
          ACCOUNT_CARD.INFO.IS_NOT_CHANGE.CONTENT
        );
      }
    },
    handleClear() {
      const formData = this.formData;
      formData.id = null;
      formData.name = null;
      formData.description = null;
      formData.star = null;
      formData.end = null;
      formData.color = null;
      formData.full = false;
      this.timePicker = {
        start: null,
        end: null
      };
      this.color = null;
      this.$v.$reset();
    },
    reset() {
      if (this.type === "new") {
        this.$refs.event.reset();
      }
      if (this.type === "edit") {
        const raw = cloneDeep(this.rawJob);
        const data = this.formData;
        data.name = raw.name;
        data.color = raw.color;
        data.color = raw.color;
        data.description = raw.description;
        this.timePicker = {
          start: moment(raw.start),
          end: moment(raw.end)
        };
        data.start = raw.start;
        data.end = raw.end;
        this.canEdit = !this.canEdit;
        this.type = "";
      }
      this.$v.$reset();
    },
    handleCloseDialog() {
      this.reset();
      this.formData = {
        id: null,
        name: null,
        description: null,
        start: null,
        end: null,
        color: null,
        full: null,
        accountId: null,
        projectId: null
      };
      this.timePicker = {
        start: null,
        end: null
      };
      this.color = null;
      this.$refs.event.reset();
      this.$v.$reset();
      this.dialog = false;
    },
    handelEdit() {
      this.type = "edit";
      this.canEdit = !this.canEdit;
    },
    showDeleteDialog() {
      let me = this;
      Modal.confirm({
        title: "你确定删除这个任务吗?",
        okType: "danger",
        okText: "确定",
        cancelText: "取消",
        onOk() {
          me.handleDelete();
        },
        onCancel() {}
      });
    },
    async handleDelete() {
      this.isDisable = true;
      try {
        const {
          data: { code, msg }
        } = await this.$api.job.deleteJob(this.formData.id);
        if (code === REQUEST.SUCCESS.code) {
          this.$notification.success(code, msg);
          eventBus.$emit("refreshEventList");
          this.dialog = false;
        }
      } finally {
        this.isDisable = false;
      }
    },
    validateComputed(type) {
      const errors = [];
      switch (type) {
        case "name": {
          if (!this.$v.formData.name.$dirty) break;
          !this.$v.formData.name.maxLength &&
            errors.push(JOB_DIALOG.NAME.NAME_IS_OUT_LIMIT);
          !this.$v.formData.name.required &&
            errors.push(JOB_DIALOG.NAME.NAME_IS_EMPTY);
          break;
        }
        case "detail": {
          if (!this.$v.formData.description.$dirty) break;
          !this.$v.formData.description.maxLength &&
            errors.push(JOB_DIALOG.DESCRIPTION);
          break;
        }
        case "date": {
          if (!this.$v.formData.description.$dirty) break;
          !this.$v.formData.start.required &&
            errors.push(JOB_DIALOG.DATE.START_DATE_EMPTY);
          !this.$v.formData.end.required &&
            errors.push(JOB_DIALOG.DATE.END_DATE_EMPTY);
          break;
        }
      }
      return errors;
    }
  },
  computed: {
    getTitle() {
      if (this.type === "new") {
        return "添加事件";
      }
      if (this.type === "edit") return "修改事件";
      return "当前事件";
    },
    getSubTitle() {
      if (this.type === "new") {
        return "通过事件指定固定时间,相关人员便可以收到通知";
      }
      if (this.type === "edit") return "仔细修改当前事件";
      return this.formData.id;
    },
    getPlaceHolder() {
      return this.isFullDay
        ? ["* 请选择开始日期", "* 请选择结束日期"]
        : ["* 请选择开始时间", "* 请选择结束时间"];
    },
    getFormat() {
      return this.isFullDay ? "YYYY-MM-DD" : "YYYY-MM-DD HH:mm";
    },
    getRangeTime() {
      if (this.timePicker.start && this.timePicker.end) {
        return `${this.timePicker.start.format(
          this.getFormat
        )} - ${this.timePicker.end.format(this.getFormat)}`;
      } else {
        return null;
      }
    },
    getShowTime() {
      return this.isFullDay
        ? false
        : { format: "HH:mm", disabledSeconds: true, clearText: true };
    },
    getJobList() {
      return this.$store.getters.getJobList;
    },
    rawJob() {
      return this.$store.getters.getJob;
    },
    nameErrors() {
      return this.validateComputed("name");
    },
    detailsErrors() {
      return this.validateComputed("detail");
    },
    dateErrors() {
      return this.validateComputed("date");
    },
    checkPerm() {
      return (
        checkPermission(JOB_PERM.EDIT_JOB) || checkPermission(JOB_PERM.ADD_JOB)
      );
    },
    getJobAdd() {
      return JOB_PERM.ADD_JOB;
    },
    getJobEdit() {
      return JOB_PERM.EDIT_JOB;
    },
    getJobDel() {
      return JOB_PERM.DEL_JOB;
    }
  },
  created() {
    eventBus.$on("showEventDialog", message => {
      this.type = message.type;
      this.canEdit = message.edit;
      if (message.type !== "new") {
        this.account = message.data.account;
        this.$store.commit("updateJob", cloneDeep(message.data.formData));
        this.formData = cloneDeep(message.data.formData);
        this.color = this.formData.color;
        this.timePicker.start = moment(this.formData.start);
        this.timePicker.end = moment(this.formData.end);
        this.isFullDay = message.data.formData.full;
      } else {
        this.account = message.data;
        this.formData.start = message.start;
        this.timePicker.start = message.start;
      }
      this.formData.accountId = this.account.id;
      this.dialog = true;
    });
  },
  beforeDestroy() {
    eventBus.$off("showEventDialog");
  }
};
</script>

<style scoped>
div {
  margin: 0;
}
</style>
