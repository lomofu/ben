import "../../jest-setup";
import { shallowMount } from "@vue/test-utils";
import load from "@/components/public/load.vue";
import Vuex from "vuex";

describe("load.vue test case", () => {
  let getters;
  let store;
  let wrapper;

  beforeEach(() => {
    getters = {
      getVersion: () => "1.0.0"
    };

    store = new Vuex.Store({
      getters
    });

    wrapper = shallowMount(load, {
      store,
      sync: false
    });
  });

  it("should render the component when passed", () => {
    expect(wrapper.isVueInstance()).toBeTruthy();
  });

  it("should be 1.0.0 when render the component success", function() {
    const vm = wrapper.vm;
    expect(vm.$data.version).toBe("1.0.0");
  });
});
