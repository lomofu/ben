<template>
  <v-card class="mx-auto card-box" outlined>
    <v-card-text>
      <v-form>
        <v-row>
          <v-col class="col-7">
            <div>id:</div>
            <v-text-field
              :value="formData.id"
              solo
              flat
              readonly
              dense
            ></v-text-field>
          </v-col>
          <v-spacer></v-spacer>
        </v-row>
        <v-row>
          <v-col class="col-6">
            <v-row>
              <div class="mt-2 mr-2">名称:</div>
              <v-text-field
                v-model.trim="formData.name"
                :error-messages="nameErrors"
                :counter="canEdit ? 20 : false"
                :outlined="canEdit"
                :solo="!canEdit"
                :flat="!canEdit"
                :readonly="!canEdit"
                dense
                clear-icon="mdi-close"
                :clearable="canEdit"
                @input="handleValidateName"
                @blur="handleValidateName"
                :disable="isDisable"
              ></v-text-field>
            </v-row>
          </v-col>
          <v-col class="col-6 d-flex">
            <div class="mt-2">
              性别: <span v-if="!canEdit">{{ formData.gender }}</span>
            </div>
            <v-row v-if="canEdit">
              <v-radio-group
                class="ml-4 mt-1"
                v-model="formData.gender"
                @change="isChangeSex = true"
                :disable="isDisable"
                row
              >
                <v-radio
                  class="mr-3"
                  v-for="n in 3"
                  :key="n"
                  :label="n === 3 ? '保密' : n === 1 ? '男' : '女'"
                  :value="n"
                ></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>

        <v-row>
          <v-col class="col-6">
            <v-row>
              <div class="mt-2 mr-2">手机:</div>
              <v-text-field
                v-model.trim="formData.phoneNumber"
                :counter="canEdit ? 11 : false"
                :error-messages="phoneErrors"
                :outlined="canEdit"
                :solo="!canEdit"
                :flat="!canEdit"
                :readonly="!canEdit"
                clear-icon="mdi-close"
                :clearable="canEdit"
                @input="handleValidatePhone"
                @blur="handleValidatePhone"
                :disable="isDisable"
                dense
              ></v-text-field>
            </v-row>
          </v-col>
          <v-col class="col-6">
            <v-row>
              <div class="mt-2 mr-2">邮箱:</div>
              <v-text-field
                v-model.trim="formData.email"
                :error-messages="emailErrors"
                :outlined="canEdit"
                :solo="!canEdit"
                :flat="!canEdit"
                :readonly="!canEdit"
                clear-icon="mdi-close"
                :clearable="canEdit"
                @input="handleValidateEmail"
                @blur="handleValidateEmail"
                :disable="isDisable"
                dense
              ></v-text-field>
            </v-row>
          </v-col>
        </v-row>
      </v-form>
      <p class="text-right">
        <span>创建时间: {{ $moment(account.createTime).format("lll") }}</span>
      </p>
    </v-card-text>
    <Permission :perm="getAccountEdit">
      <v-card-actions>
        <v-btn
          v-if="!canEdit"
          text
          color="deep-purple accent-4"
          @click="handleEdit"
        >
          修改资料
        </v-btn>
        <v-btn
          v-else
          text
          color="blue"
          :loading="isDisable"
          @click="handleUpdate"
        >
          提交
        </v-btn>
        <v-btn
          v-if="canEdit"
          :disable="isDisable"
          text
          color="red"
          @click="handleEdit"
        >
          取消
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
          v-if="canEdit"
          :disable="isDisable"
          text
          color="red"
          @click="handleRecover"
        >
          还原
        </v-btn>
      </v-card-actions>
    </Permission>
  </v-card>
</template>

<script>
import { validationMixin } from "vuelidate";
import { email, maxLength, required } from "vuelidate/lib/validators";
import { fullName, phone } from "../../../common/validator/validator";
import { EMAIL, FULLNAME, PHONE } from "../../../common/validator/Constant";
import { REQUEST } from "../../../common/view/Constant";
import { ACCOUNT_CARD } from "../../../common/components/Constant";
import debounce from "lodash/debounce";
import { mapActions, mapGetters } from "vuex";
import { ACCOUNT_PERM } from "../../../utils/permission";

export default {
  name: "accountCard",
  mixins: [validationMixin],
  validations: {
    formData: {
      email: {
        required,
        email,
        async isUnique(value) {
          if (
            value === "" ||
            value === null ||
            !/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)
          ) {
            return true;
          } else {
            const response = await this.$api.account.isUnique("email", value);
            return response.data.data;
          }
        }
      },
      name: {
        required,
        illegalChar: fullName.illegalChar(),
        underline: fullName.underline(),
        allNumbers: fullName.allNumbers(),
        maxLength: maxLength(20),
        async isUnique(value) {
          if (
            value === "" ||
            value === null ||
            /^\d*\d\d*$/.test(value) ||
            /(^_)|(__)|(_+$)/.test(value)
          ) {
            return true;
          } else {
            const response = await this.$api.account.isUnique("name", value);
            return response.data.data;
          }
        }
      },
      phoneNumber: {
        required,
        phone: phone,
        async isUnique(value) {
          if (value === "" || value === null || value.length !== 11) {
            return true;
          } else {
            const response = await this.$api.account.isUnique("phone", value);
            return response.data.data;
          }
        }
      }
    }
  },
  data: () => ({
    canEdit: false,
    isDisable: false,
    formData: {
      id: "",
      name: "",
      gender: null,
      phoneNumber: "",
      email: ""
    }
  }),
  methods: {
    initialInfo() {
      const formData = this.formData;
      const account = this.account;
      formData.id = account.id;
      formData.name = account.name;
      formData.gender = account.gender;
      formData.phoneNumber = account.phoneNumber;
      formData.email = account.email;
    },
    validateNameData() {
      if (this.formData.name !== this.account.name) {
        const validate = this.$v.formData;
        validate.name.$touch();
        return (
          !validate.name.required ||
          !validate.name.illegalChar ||
          validate.name.underline ||
          validate.name.allNumbers ||
          !validate.name.maxLength ||
          !validate.name.isUnique
        );
      } else {
        return false;
      }
    },
    validatePhoneData() {
      if (this.formData.phoneNumber !== this.account.phoneNumber) {
        const validate = this.$v.formData;
        validate.phoneNumber.$touch();
        return validate.phoneNumber.$invalid;
      } else {
        return false;
      }
    },
    validateEmailData() {
      if (this.formData.email !== this.account.email) {
        const validate = this.$v.formData;
        validate.email.$touch();
        return validate.email.$invalid || !validate.email.isUnique;
      } else {
        return false;
      }
    },
    handleValidateName: debounce(function() {
      if (this.canEdit && this.formData.name) this.$v.formData.name.$touch();
    }, 300),
    handleValidateEmail: debounce(function() {
      if (this.canEdit && this.formData.email) this.$v.formData.email.$touch();
    }, 300),
    handleValidatePhone: debounce(function() {
      if (this.canEdit && this.formData.phoneNumber)
        this.$v.formData.phoneNumber.$touch();
    }, 300),
    handleEdit() {
      this.handleRecover();
      this.canEdit = !this.canEdit;
    },
    handleRecover() {
      this.$v.$reset();
      this.initialInfo();
    },
    async handleUpdate() {
      if (
        this.formData.name !== this.account.name ||
        this.formData.phoneNumber !== this.account.phoneNumber ||
        this.formData.email !== this.account.email ||
        this.formData.gender !== this.account.gender
      ) {
        if (
          this.validateEmailData() ||
          this.validateNameData() ||
          this.validatePhoneData()
        ) {
          return false;
        } else {
          this.isDisable = true;
          try {
            const {
              data: { code, msg }
            } = await this.$api.account.updateAccount(this.formData);
            if (code === REQUEST.SUCCESS.code) {
              this.fetchAccount(this.formData.id);
              this.canEdit = !this.canEdit;
              this.$notification.success(code, msg);
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
    ...mapActions({
      fetchAccount: "fetchAccount"
    })
  },
  computed: {
    ...mapGetters({
      account: "getAccount"
    }),
    nameErrors() {
      const errors = [];
      if (this.canEdit) {
        if (!this.$v.formData.name.$dirty) return errors;
        !this.$v.formData.name.required && errors.push(FULLNAME.NAME_IS_EMPTY);
        !this.$v.formData.name.maxLength &&
          errors.push(FULLNAME.NAME_IS_OUT_LIMIT);
        !this.$v.formData.name.illegalChar &&
          errors.push(FULLNAME.NAME_WITH_ILLEGAL_CHAR);
        this.$v.formData.name.allNumbers &&
          errors.push(FULLNAME.NAME_WITH_ALL_NUMBERS);
        this.$v.formData.name.underline &&
          errors.push(FULLNAME.NAME_WITH_UNDERLINE);
        !this.$v.formData.name.isUnique && errors.push(FULLNAME.NAME_IS_EXIST);
      }
      return errors;
    },
    phoneErrors() {
      const errors = [];
      if (this.canEdit) {
        if (!this.$v.formData.phoneNumber.$dirty) return errors;
        !this.$v.formData.phoneNumber.required &&
          errors.push(PHONE.PHONE_IS_EMPTY);
        !this.$v.formData.phoneNumber.phone &&
          errors.push(PHONE.PHONE_NUMBER_IS_ILLEGAL);
        !this.$v.formData.phoneNumber.isUnique &&
          errors.push(PHONE.PHONE_IS_EXIST);
      }
      return errors;
    },
    emailErrors() {
      const errors = [];
      const validate = this.$v;
      if (this.canEdit) {
        if (!validate.formData.email.$dirty) return errors;
        !validate.formData.email.isUnique &&
          errors.push(EMAIL.EMAIL_IS_EXISTED);
        !validate.formData.email.email && errors.push(EMAIL.EMAIL_ILLEGAL);
        !validate.formData.email.required && errors.push(EMAIL.EMAIL_IS_EMPTY);
      }
      return errors;
    },
    getAccountEdit() {
      return ACCOUNT_PERM.EDIT_ACCOUNT;
    }
  },
  created() {
    this.initialInfo();
  }
};
</script>

<style scoped>
div {
  margin: 0;
}

.card-box {
  position: relative;
  overflow: hidden;
  z-index: 10;
}

.card-box::after {
  content: "";
  display: block;
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 100px;
  background-repeat: no-repeat;
  background-image: url("../../../../public/img/icon/icon-color.png");
  background-position: right;
  background-position-y: -10px;
  background-size: 250px;
  opacity: 45%;
  transform: rotate(-20deg);
  z-index: -1;
}
</style>
