const project = {
  state: {
    showSchedule: false,
    calendar: {
      start: null,
      end: null
    }
  },
  getters: {
    getShowSchedule({ showSchedule }) {
      return showSchedule;
    },
    getStart: ({ calendar }) => {
      return calendar.start;
    },
    getEnd: ({ calendar }) => {
      return calendar.end;
    }
  },
  mutations: {
    updateShowSchedule(state, index) {
      state.showSchedule = index;
    },
    updateStart(state, start) {
      state.calendar.start = start;
    },
    updateEnd(state, end) {
      state.calendar.end = end;
    }
  }
};

export default project;
