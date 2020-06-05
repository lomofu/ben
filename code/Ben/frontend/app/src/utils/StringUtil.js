const StringUtil = {
  toLine(name) {
    return name.replace(/([A-Z])/g, "_$1").toLowerCase();
  }
};

export default StringUtil;
