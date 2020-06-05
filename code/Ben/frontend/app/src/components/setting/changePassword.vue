<template>
  <div>
    <v-card flat class="ma-5">
      <v-stepper v-model="step" vertical>
        <v-stepper-step :complete="step > 1" step="1">
          选择验证方式
        </v-stepper-step>
        <v-stepper-content step="1">
          <v-card flat class="d-flex justify-center" height="200px">
            <v-btn
              x-large
              outlined
              class="ma-5"
              color="indigo"
              :disabled="disable.isDisable1"
              :loading="disable.isDisable1"
              @click="handleValidateByEmail"
            >
              <v-icon size="30" class="mr-3">mdi-email-send-outline</v-icon>
              邮箱验证
            </v-btn>
            <v-btn
              x-large
              outlined
              class="ma-5"
              color="orange  darken-4"
              :disabled="disable.isDisable1"
              :loading="disable.isDisable1"
              @click="handleValidateBySms"
            >
              <v-icon size="30" class="mr-3">mdi-cellphone-key</v-icon>
              短信验证
            </v-btn>
          </v-card>
          <v-btn color="primary" @click="step = 2" v-if="success.success1"
            >下一步
          </v-btn>
        </v-stepper-content>

        <v-stepper-step :complete="step > 2" step="2"
          >输入验证码</v-stepper-step
        >

        <v-stepper-content step="2">
          <v-card color="grey lighten-1" class="mb-12" height="200px"></v-card>
          <v-btn color="primary" @click="step = 3">下一步</v-btn>
          <v-btn text color="red" @click="handleCancel">取消</v-btn>
        </v-stepper-content>

        <v-stepper-step :complete="step > 3" step="3">修改密码</v-stepper-step>

        <v-stepper-content step="3">
          <v-card color="grey lighten-1" class="mb-12" height="200px"></v-card>
          <v-btn color="primary" @click="null">完成</v-btn>
          <v-btn text color="red" @click="handleCancel">取消</v-btn>
        </v-stepper-content>
      </v-stepper>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "changePassword",
  data: () => ({
    step: 1,
    disable: {
      isDisable1: false
    },
    success: {
      success1: true
    }
  }),
  methods: {
    handleCancel() {
      this.step = 1;
    },
    handleValidateByEmail() {},
    handleValidateBySms() {}
  }
};
</script>

<style scoped></style>
