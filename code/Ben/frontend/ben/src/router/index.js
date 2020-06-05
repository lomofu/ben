import Vue from "vue";
import VueRouter from "vue-router";
import api from "../utils/api";

Vue.use(VueRouter);

function loadView(view) {
  return () =>
    import(/* webpackChunkName: "view-[request]" */ `@/views/${view}.vue`);
}

const routes = [
  {
    path: "/",
    name: "home",
    meta: {
      title: "首页"
    },
    component: loadView("Home")
  },
  {
    path: "/price",
    name: "price",
    meta: {
      title: "价格"
    },
    component: loadView("Price")
  },
  {
    path: "/support",
    name: "support",
    meta: {
      title: "支持"
    },
    component: loadView("Support")
  },
  {
    path: "/login",
    name: "login",
    meta: {
      title: "登录"
    },
    component: loadView("Login")
  },
  {
    path: "/signup",
    name: "signUp",
    meta: {
      title: "注册"
    },
    component: loadView("SignUp")
  },
  {
    path: "/register",
    name: "register",
    meta: {
      title: "填写注册信息"
    },
    component: loadView("Register")
  },
  {
    path: "/employee",
    name: "employee",
    meta: {
      title: "完善个人信息"
    },
    component: loadView("NewEmployee")
  },
  { path: "*", name: "404", component: loadView("404") }
];

const router = new VueRouter({
  mode: "history",
  scrollBehavior() {
    return { x: 0, y: 0 };
  },
  routes
});

router.beforeEach(async (to, from, next) => {
  const token = Vue.$cookies.get("ben-user");
  if (token) {
    await api.login.isLogin(token);
    next();
  }
  next();
});

export default router;
