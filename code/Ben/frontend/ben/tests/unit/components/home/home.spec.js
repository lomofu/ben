import "../../jest-setup";
import { shallowMount } from "@vue/test-utils";
import speed from "@/components/home/speed.vue";

describe("speed.vue test case", () => {
  let wrapper;
  beforeEach(() => {
    wrapper = shallowMount(speed);
  });

  it("should render the component when passed", () => {
    expect(wrapper.isVueInstance()).toBeTruthy();
  });
});
