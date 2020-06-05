const myjob = {
  state: {
    myJobThisWeekCount: 0,
    myJobTotalCount: 0
  },
  getters: {
    getMyJobThisWeekCount({ myJobThisWeekCount }) {
      return myJobThisWeekCount;
    },
    getMyJobTotalCount: ({ myJobTotalCount }) => {
      return myJobTotalCount;
    }
  },
  mutations: {
    updateMyJobThisWeekCount(state, data) {
      state.myJobThisWeekCount = data;
    },
    updateMyJobTotalCount(state, data) {
      state.myJobTotalCount = data;
    }
  }
};

export default myjob;
