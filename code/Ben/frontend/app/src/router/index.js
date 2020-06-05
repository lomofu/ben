import Vue from "vue";
import VueRouter from "vue-router";
import { EMPLOYEE_PERM, PROJECT_PERM, TEAM_PERM } from "../utils/permission";

Vue.use(VueRouter);

function loadView(view) {
  return () =>
    import(/* webpackChunkName: "view-[request]" */ `@/views/${view}.vue`);
}

function loadSettingComponents(view) {
  return () =>
    import(
      /* webpackChunkName: "setting-[request]" */ `@/components/setting/${view}.vue`
    );
}

const routes = [
  {
    path: "/",
    name: "layout",
    component: loadView("Layout"),
    children: [
      {
        meta: {
          title: "个人看板"
        },
        path: "/",
        component: loadView("Dashboard")
      },
      {
        meta: {
          title: "团队列表",
          perm: TEAM_PERM.VIEW_TEAM
        },
        path: "teamList",
        component: loadView("TeamList")
      },
      {
        meta: {
          title: "项目列表",
          perm: PROJECT_PERM.VIEW_PROJECT
        },
        path: "projectList",
        component: loadView("ProjectList")
      },
      {
        meta: {
          title: "雇员列表",
          perm: EMPLOYEE_PERM.VIEW_EMPLOYEE
        },
        path: "members",
        component: loadView("Member")
      },
      {
        meta: {
          title: "团队成员",
          perm: TEAM_PERM.VIEW_TEAM
        },
        path: "employees/:id",
        component: loadView("Employee")
      },
      {
        meta: {
          title: "项目详情",
          name: "project",
          perm: PROJECT_PERM.VIEW_PROJECT
        },
        path: "projects/:id",
        component: loadView("Project")
      },
      {
        meta: {
          title: "我的排班",
          name: "myjob"
        },
        path: "myjob",
        component: loadView("MyJob")
      }
    ]
  },
  {
    meta: {
      title: "设置"
    },
    path: "/setting",
    component: loadView("Setting"),
    children: [
      {
        meta: {
          base: "设置",
          title: "设置中心",
          disabled: false
        },
        path: "/",
        component: loadSettingComponents("base")
      },
      {
        meta: {
          base: "设置",
          title: "修改密码",
          disabled: false
        },
        path: "password",
        component: loadSettingComponents("changePassword")
      },
      {
        meta: {
          base: "设置",
          title: "角色管理",
          disabled: false
        },
        path: "permission",
        component: loadSettingComponents("role")
      },
      {
        meta: {
          base: "设置",
          title: "主题选择",
          disabled: false
        },
        path: "theme",
        component: loadSettingComponents("themeSelector")
      }
    ]
  },
  {
    path: "/403",
    meta: {
      title: "没有权限"
    },
    name: "403",
    component: loadView("403")
  },
  {
    path: "*",
    meta: {
      title: "页面消失"
    },
    name: "404",
    component: loadView("404")
  }
];

const router = new VueRouter({
  mode: "history",
  scrollBehavior() {
    return { x: 0, y: 0 };
  },
  routes
});

router.beforeEach((to, from, next) => {
  next();
});

router.onError(error => {
  const pattern = /Loading chunk (\d)+ failed/g;
  const isChunkLoadFailed = error.message.match(pattern);
  const targetPath = router.history.pending.fullPath;
  if (isChunkLoadFailed) {
    router.replace(targetPath);
  }
});

export default router;
