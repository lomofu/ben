<template>
  <v-sheet>
    <v-navigation-drawer
      v-model="drawer"
      temporary
      absolute
      right
      :width="width"
      mobile-break-point="1260"
      class="hidden-sm-and-down"
      style="position: fixed"
    >
      <v-btn icon class="ma-2">
        <v-icon size="30" class="hvr-grow" @click="clickSize">{{
          width === "45vw" ? "mdi-chevron-left" : "mdi-chevron-right"
        }}</v-icon>
      </v-btn>
      <div
        class="userinfo-box d-flex align-center justify-center"
        :style="{ background: dark ? '#141414' : '' }"
      >
        <div class="mt-10 ">
          <v-hover v-slot:default="{ hover }">
            <div class="d-flex justify-center">
              <v-avatar :title="user.username" size="90">
                <v-img
                  :lazy-src="getAccount.avatarUrl"
                  :src="getAccount.avatarUrl"
                >
                  <v-expand-transition>
                    <div
                      v-if="hover"
                      class="d-flex transition-fast-in-fast-out grey darken-2 white--text edit"
                      style="height: 100%;"
                    >
                      <v-btn
                        icon
                        text
                        fab
                        @click="handleEditPicture"
                        color="white"
                      >
                        <v-icon size="50" color="white"
                          >mdi-account-box-outline</v-icon
                        >
                      </v-btn>
                    </div>
                  </v-expand-transition>
                </v-img>
              </v-avatar>
            </div>
          </v-hover>
          <Permission :perm="getAccountEdit">
            <v-dialog
              v-model="dialog2"
              max-width="500px"
              persistent
              no-click-animation
            >
              <v-card class="pa-8">
                <vueCrapper
                  :preSave="imageSave"
                  :fileName="fileName"
                  v-if="preSave"
                  @upload="upload"
                  @final="final"
                ></vueCrapper>
                <br />
                <v-file-input
                  class="mt-3"
                  :rules="rules"
                  :disabled="isDisable"
                  accept="image/png, image/jpeg, image/jpg, image/bmp"
                  placeholder="上传你的头像"
                  prepend-icon="mdi-camera"
                  label="头像"
                  @change="reviewFile"
                  show-size
                  small-chips
                  ref="imgPicture"
                />
                <v-card-actions class="mt-3">
                  <v-btn
                    color="red"
                    text
                    @click="handleCloseDialog"
                    :disabled="isDisable"
                  >
                    关闭窗口
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </Permission>
          <p class="text-center userinfo-username">
            {{ getAccount.name }}
          </p>
          <div class="d-flex justify-center">
            <v-chip color="orange" dark>
              {{ getAccount.admin ? "管理员" : getAccount.role[0] }}
              <v-icon right>mdi-account-outline</v-icon>
            </v-chip>
          </div>
        </div>
      </div>
      <user-tabs
        :style="{
          background: dark ? '#141414' : ''
        }"
      ></user-tabs>
    </v-navigation-drawer>
  </v-sheet>
</template>
<script>
import { eventBus } from "../../../bus";
import { ACCOUNT_PERM } from "../../../utils/permission";
export default {
  name: "userinfo",
  props: ["user"],
  components: {
    userTabs: () => import("./userTab.vue"),
    vueCrapper: () => import("../../public/right/vueCropper.vue")
  },
  data() {
    return {
      isDisable: false,
      drawer: false,
      width: "45vw",
      showSize: true,
      dialog2: false,
      rules: [value => !value || value.size < 2000000 || "头像大小需要小于2MB"],
      file: null,
      preSave: "",
      imageSave: "",
      fileName: null
    };
  },
  methods: {
    clickSize() {
      this.width === "45vw" ? (this.width = "80vw") : (this.width = "45vw");
    },
    handleEditPicture() {
      this.dialog2 = true;
    },
    reviewFile(e) {
      if (e && e.size <= 2000000) {
        let file = e;
        this.fileName = e.name;
        this.imageSave = e;
        let reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = data => {
          let res = data.target;
          this.preSave = res.result;
          this.imageSave = res.result;
        };
      } else {
        this.preSave = null;
        this.imageSave = null;
      }
    },
    handleCloseDialog() {
      this.dialog2 = false;
      this.preSave = null;
      this.imageSave = null;
      this.$refs.imgPicture.value = null;
    },
    upload() {
      this.isDisable = true;
    },
    final() {
      this.isDisable = false;
    }
  },
  computed: {
    getAccount() {
      return this.$store.getters.getAccount;
    },
    getAccountEdit() {
      return ACCOUNT_PERM.EDIT_ACCOUNT;
    },
    dark() {
      return this.$store.getters.getDark;
    }
  },
  created() {
    eventBus.$on("openUserInfo", message => {
      this.drawer = message;
    });
  },
  beforeDestroy() {
    eventBus.$off("openUserInfo");
  }
};
</script>
<style scoped lang="scss">
@include div;
.userinfo {
  &-box {
    @include color;
    height: 30vh;
    min-width: 45vw;
    max-width: 80vw;
    display: flex;
    margin-top: -6vh;
  }

  &-username {
    @include font-bold;
    font-size: 22px;
    color: white;
    text-shadow: $text-shadow;
  }
  &-subinfo {
    @include font;
    font-size: 16px;
    color: #4d4d4d;
  }

  p {
    width: 45vw;
  }
}

.edit {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: 0.7;
  position: absolute;
  width: 100%;
  border-radius: 100px;
}

img {
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 100%;
}
</style>
