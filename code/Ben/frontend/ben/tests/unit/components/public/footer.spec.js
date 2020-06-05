import "../../jest-setup";
import { shallowMount } from "@vue/test-utils";
import footer from "@/components/public/footer.vue";

describe("footer.vue test case", () => {
  let wrapper;
  beforeEach(() => {
    wrapper = shallowMount(footer);
  });

  it("should render the component when passed", () => {
    expect(wrapper.classes("white")).toBeTruthy();
  });
});
