<template>
  <div class="animated fadeInDown pa-1">
    <p class="text-center" style="font-size: smaller;color: grey">
      *头像大小 150x150
    </p>
    <br />
    <div class="d-flex justify-center mb-5">
      <div class="show-preview">
        <div :style="previews.div" class="preview">
          <img :src="previews.url" :style="previews.img" />
        </div>
      </div>
    </div>
    <div style="width:100%;height:250px">
      <vueCropper
        :img="preSave"
        :autoCrop="options.autoCrop"
        :info="options.info"
        :outputType="options.outputType"
        :canScale="options.canScale"
        :autoCropWidth="options.autoCropWidth"
        :autoCropHeight="options.autoCropHeight"
        :fixedNumber="options.fixedNumber"
        :outputSize="options.outputSize"
        :fixedBox="options.fixedBox"
        :centerBox="options.centerBox"
        ref="cropper"
        @realTime="realTime"
      />
    </div>
    <p class="text-center mt-3">
      <v-btn icon color="red" @click="$refs.cropper.rotateLeft()">
        <v-icon>
          mdi-rotate-left
        </v-icon>
      </v-btn>
      <v-btn icon color="red" @click="$refs.cropper.rotateLeft()">
        <v-icon>
          mdi-rotate-right
        </v-icon>
      </v-btn>
    </p>
    <p class="d-flex justify-space-around">
      <v-btn class="mt-3" @click="handleUpload" color="info" :loading="loading">
        上传头像
      </v-btn>
    </p>
  </div>
</template>
<script>
import { VueCropper } from "vue-cropper";
import { REQUEST } from "../../../common/view/Constant";
export default {
  props: ["preSave", "fileName"],
  components: {
    VueCropper
  },
  inject: ["reload"],
  data: () => ({
    previews: {},
    options: {
      autoCrop: true,
      info: true,
      autoCropWidth: 150,
      autoCropHeight: 150,
      canScale: true,
      fixedNumber: [1, 1],
      outputSize: 0.9,
      outputType: "png",
      fixedBox: true,
      centerBox: true
    },
    loading: false
  }),
  methods: {
    handleUpload() {
      this.$refs.cropper.getCropData(async data => {
        this.loading = true;
        this.$emit("upload");
        try {
          let file = this.dataURLtoFile(data, this.fileName);
          const {
            data: { code, msg }
          } = await this.$api.account.upload(file);
          if (REQUEST.SUCCESS.code === code) {
            this.$notification.success(code, msg);
            setTimeout(() => {
              window.location.reload();
            }, 500);
          }
        } finally {
          this.loading = false;
          this.$emit("final");
        }
      });
    },
    realTime(data) {
      this.previews = data;
    },
    dataURLtoFile(dataurl, filename) {
      //将base64转换为文件
      let arr = dataurl.split(","),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], filename, { type: mime });
    }
  }
};
</script>

<style scoped>
.show-preview {
  overflow: hidden;
  margin: 5px;
  border-radius: 80px;
  box-shadow: 0 2px 5px 0 #00000024;
}
</style>
