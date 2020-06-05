import moment from "moment";

const fomatter = "YYYY-MM-DD HH:mm";
const time = {
  converseToStanderString(time) {
    return moment(time).format(fomatter);
  },
  converseToFullDay(time, isBegin) {
    let date = moment(time);
    if (isBegin) {
      date.set("hour", 0);
      date.set("minute", 0);
    } else {
      date.set("hour", 23);
      date.set("minute", 59);
    }
    return date.format(fomatter);
  }
};

export default time;
