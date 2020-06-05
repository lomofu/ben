<template>
  <div>
    <v-row justify="center">
      <v-dialog
        v-model="dialog"
        max-width="600"
        persistent
        no-click-animation
        scrollable
      >
        <v-card min-height="550" max-height="550">
          <v-card-title>
            权限列表
          </v-card-title>
          <v-card-subtitle>
            选择对应角色的权限
          </v-card-subtitle>
          <v-card-text>
            <v-text-field
              class="mt-2 mb-2"
              label="搜索权限"
              v-model="search"
              outlined
              hide-details
              clearable
              dense
              :disabled="isDisable"
              clear-icon="mdi-close-circle-outline"
            ></v-text-field>
            <v-treeview
              v-model="selected"
              :search="search"
              hoverable
              selectable
              :items="all"
              :disabled="isDisable"
            ></v-treeview>
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
              color="blue darken-1"
              text
              @click="handleUpdatePermission"
              :loading="isDisable"
            >
              更改权限
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
</template>

<script>
import { eventBus } from "../../../bus";
import { REQUEST } from "../../../common/view/Constant";
import { mapActions, mapGetters, mapMutations } from "vuex";
import _ from "lodash";

export default {
  name: "permEditDialog",
  data: () => ({
    isDisable: false,
    dialog: false,
    item: null,
    search: "",
    selected: []
  }),
  methods: {
    ...mapMutations({
      updateRoleList: "updateRoleList"
    }),
    ...mapActions({
      fetchAllPerms: "fetchAllPerms"
    }),
    handleClose() {
      this.item = null;
      this.search = null;
      this.selected = [];
      this.dialog = false;
    },
    async fetchRoleData() {
      const {
        data: { code, data }
      } = await this.$api.perm.getPermListByRoleId(this.item.id);
      if (REQUEST.SUCCESS.code === code) {
        const list = data.map(e => e.id);
        this.selected = JSON.parse(JSON.stringify(list));
        this.updateRoleList(JSON.parse(JSON.stringify(list)));
      }
    },
    async handleUpdatePermission() {
      this.isDisable = true;
      if (_.isEqual(this.roleList, this.selected)) {
        this.isDisable = false;
        this.$notification.warn("Ops", "你似乎没做什么修改");
        return;
      }
      try {
        const {
          data: { code, msg }
        } = await this.$api.role.updateRolePerm(this.item.id, this.selected);
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
    ...mapGetters({
      allPerms: "getAllPerms",
      roleList: "getRoleList"
    }),
    all() {
      return this.allPerms.map(e => {
        return {
          id: e.id,
          name: `${e.name} : ${e.description} ${e.perm}`
        };
      });
    }
  },
  created() {
    this.fetchAllPerms();
    eventBus.$on("showPermList", item => {
      this.item = item;
      this.fetchRoleData();
      this.dialog = true;
    });
  },
  beforeDestroy() {
    eventBus.$off("showPermList");
  }
};
</script>

<style scoped></style>
