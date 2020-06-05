<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" max-width="600" persistent no-click-animation>
      <v-card>
        <v-card-title>
          <span class="headline">
            添加团队新成员
          </span>
          <v-list-item-subtitle>
            <small class="grey--text">
              请选择团队成员的来源
            </small>
          </v-list-item-subtitle>
        </v-card-title>
        <v-card-text>
          <v-container class="mt-10 d-flex justify-space-around">
            <div
              class="choose d-flex  justify-center hvr-grow-shadow pa-2"
              @click="handleAddExist"
              style=" border: #1976d2 2px solid;"
              v-ripple
            >
              <div class="flex-column text-center align-self-center">
                <v-icon size="60" color="primary">mdi-playlist-plus </v-icon>
                <div style="color: #1976d2">选择已经存在的雇员</div>
              </div>
            </div>

            <div
              class="choose d-flex justify-center hvr-grow-shadow pa-2"
              style=" border: purple 2px solid;"
              v-ripple
              @click="handleAddNew"
            >
              <div class="flex-column text-center align-self-center">
                <v-icon size="60" color="purple">mdi-account-plus </v-icon>
                <div style="color: purple">添加新的成员</div>
              </div>
            </div>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="dialog = false">关闭 </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <addExistEmployeeDialog
      ref="addExitEmployeeDialog"
    ></addExistEmployeeDialog>
  </v-row>
</template>

<script>
export default {
  name: "choseMemberDialog",
  components: {
    addExistEmployeeDialog: () => import("./addExistEmployeeDialog.vue")
  },
  data: () => ({
    dialog: false
  }),
  methods: {
    handleAddExist() {
      this.$refs.addExitEmployeeDialog.dialog = true;
      this.dialog = false;
    },
    handleAddNew() {
      this.$emit("fuc");
    }
  }
};
</script>

<style scoped>
.choose {
  height: 150px;
  width: 150px;
  cursor: pointer;
  border-radius: 15px;
}
</style>
