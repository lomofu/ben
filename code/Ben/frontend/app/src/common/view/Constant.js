export const REQUEST = {
  SUCCESS: {
    code: 200
  },
  FAIL: {
    code: 400,
    msg: "请求失败"
  },
  ERROR: {
    code: 500,
    msg: "未知错误"
  },
  NETWORK: {
    code: 405,
    msg: "网络忙"
  },
  UN_AUTHORIZATION: {
    code: 401,
    msg: "没有权限"
  }
};

export const PAGE = {
  PAGE_SIZE: 5,
  PAGE_MIN_VAL: 1,
  PAGE_MAX_VAL: 50
};

export const TIME = {
  DEFAULT_TIME: "YYYY-MM-DD H:mm:ss",
  DEFAULT_DATE: "YYYY-MM-DD",
  LOCAL_TIME: "lll",
  DAY: "day",
  SIMPLE_DAY: "Do",
  SIMPLE_LOCAL_TIME: "LT",
  SIMPLE_MONTH_AND_LOCAL_TIME: "DoLT"
};
