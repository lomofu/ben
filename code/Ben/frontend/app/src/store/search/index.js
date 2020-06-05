const search = {
  state: {
    employeeTable: {
      searchAll: true
    },
    teamTable: {
      searchAll: true
    },
    projectTable: {
      searchAll: true
    }
  },
  getters: {
    getEmployeeSearchAll({ employeeTable }) {
      return employeeTable.searchAll;
    },
    getTeamSearchAll({ teamTable }) {
      return teamTable.searchAll;
    },
    getProjectSearchAll({ projectTable }) {
      return projectTable.searchAll;
    }
  },
  mutations: {
    updateEmployeeSearchAll(state, data) {
      state.employeeTable.searchAll = data;
    },
    updateTeamSearchAll(state, data) {
      state.teamTable.searchAll = data;
    },
    updateProjectSearchAll(state, data) {
      state.projectTable.searchAll = data;
    }
  }
};

export default search;
