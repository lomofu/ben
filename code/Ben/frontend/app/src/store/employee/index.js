const employee = {
  state: {
    formData: null
  },
  getters: {
    getFormData({ formData }) {
      return formData;
    }
  },
  mutations: {
    updateFormData(state, formData) {
      state.formData = formData;
    }
  }
};

export default employee;
