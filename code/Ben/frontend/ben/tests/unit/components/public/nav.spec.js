import { shallowMount, createLocalVue } from "@vue/test-utils";
import nav from "@/components/public/nav.vue";
import Vuetify from "vuetify";
import Vue from "vue";

Vue.use(Vuetify);
const localVue = createLocalVue();

describe("nav.vue test case", () => {
  it("should render the component when route is /", () => {
    const wrapper = shallowMount(nav, {
      localVue,
      stubs: ["router-link", "router-view"],
      mocks: {
        $route: {
          path: "/"
        }
      }
    });
    expect(wrapper.isVueInstance()).toBeTruthy();
  });

  it("should render the component when route is /price", () => {
    const wrapper = shallowMount(nav, {
      localVue,
      stubs: ["router-link", "router-view"],
      mocks: {
        $route: {
          path: "/price"
        }
      }
    });
    expect(wrapper.isVueInstance()).toBeTruthy();
  });

  it("should render the component when route is /support", () => {
    const wrapper = shallowMount(nav, {
      localVue,
      stubs: ["router-link", "router-view"],
      mocks: {
        $route: {
          path: "/support"
        }
      }
    });
    expect(wrapper.isVueInstance()).toBeTruthy();
  });

  it("should be true when call handleLeave methods", async () => {
    const wrapper = shallowMount(nav, {
      localVue,
      stubs: ["router-link", "router-view"],
      mocks: {
        $route: {
          path: "/"
        }
      }
    });
    const vm = wrapper.vm;
    expect(vm.$data.showMenu).toBeFalsy();
    vm.handleLeave();
    await new Promise(done =>
      setTimeout(() => {
        expect(vm.$data.showMenu).toBeTruthy();
        done();
      }, 2000)
    );
  });
});
