<template>
  <div>
    <roleDialog></roleDialog>
    <permEditDialog></permEditDialog>
    <v-card class="ma-5" min-height="400">
      <Permission :perm="getRoleView">
        <roleTable></roleTable>
      </Permission>
      <noPermission v-if="!getCheck"></noPermission>
    </v-card>
  </div>
</template>

<script>
import { ROLE_PERM } from "../../utils/permission";
import { checkPermission } from "../../utils/auth/auth";
export default {
  name: "role",
  components: {
    roleDialog: () => import("./role/roleDialog.vue"),
    roleTable: () => import("./role/roleTable.vue"),
    permEditDialog: () => import("./role/permEditDialog.vue"),
    noPermission: () => import("./noPermission.vue")
  },
  computed: {
    getRoleView() {
      return ROLE_PERM.VIEW_ROLE;
    },
    getCheck() {
      return checkPermission(ROLE_PERM.VIEW_ROLE);
    }
  }
};
</script>

<style scoped></style>
