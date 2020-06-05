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
  }
};

export const HOME = {
  TIPS: {
    EMAIL_ILLEGAL: "请输入合法的邮箱地址!",
    EMAIL_IS_EMPTY: "邮箱地址不能为空!"
  },
  VIEW: {
    TYPECONTENT: ["欢迎来到 Ben  ", "花费很少的时间分享日程安排  "],
    BTN: {
      icon: "mdi-at",
      word: "免 费 注 册",
      height: "70",
      placeholder: "请输入您的邮箱"
    },
    CONTENT: {
      p1: [
        "什么是Ben?",
        "Ben 是个Saas多租户的项目管理平台, 您只需要快速注册自己的管理员账号就可以轻松安排Team人员之间的任务排班流程。"
      ],
      p2: [
        "为什么选择Ben?",
        "Ben 可以在您发布任务,排班的同时一键精确推送给员工,这样极大的提升了工作安排效率。"
      ],
      p3: [
        "Ben能做什么？",
        "许多企业都需要项目管理的支持, Ben可以帮助企业管理者管理雇员和排班，并以短信或者邮件等方式，将排班信息及时通知到雇员。"
      ],
      p4: [
        "使用简单！",
        "想要使用Ben十分简单, Ben是一款基于云原生的SAAS应用，所以您只需要一款能上网的设备就可以体验Ben带给您的服务。"
      ],
      view: ["看看哪个计划适合您的团队或者企业:", "计划 & 价格"]
    }
  }
};

export const PRICE = {
  VIEW: {
    TITLE: "计划和价格",
    CARD: [
      {
        icon: {
          colors: "green",
          data: "mdi-cloud-check"
        },
        title: "免费使用",
        price: "Free",
        content: [
          "创建您的项目的时间表,单账户可管理最多5个Team",
          "分享时间表给Team中的成员",
          "单个Team最多可以有10人"
        ],
        button: "免 费 注 册",
        className: "d-table-cell"
      },
      {
        icon: {
          color: "black",
          data: "mdi-star-circle"
        },
        title: "Plus +",
        price: "¥ 6.66/月",
        content: [
          "我们目前正在开发的一些更强大的功能,和一些更激动人心的功能",
          "不限Team的数量",
          "单个Team上限1000人"
        ],
        button: "Coming Soon",
        className: "d-table-cell",
        isDisabled: true
      }
    ],
    QUESTIONS: {
      title: "常见问题",
      question: [
        {
          title: "免费使用是否有限制？",
          answer:
            "当然没有！我们希望您能满意地使用Ben来安排工作调度，所以我们提供永久免费的服务。当然，目前在团队数量和团队成员上有些许限制，详情可以查看上方“免费使用”卡片中的详细介绍。"
        },
        {
          title: "当我购买会员服务后有什么新的功能？",
          answer:
            "Ben目前提供免费使用，我们的团队也在极力开发更强大的会员服务，初步设想可参考上方“Plus +”卡片中的详细介绍。当然，如果您有意向与我们合作，可以通过邮件联系我们，我们会热心为您服务。"
        }
      ]
    }
  }
};

export const SUPPORT = {
  VIEW: {
    btn: {
      icon: "mdi-arrow-left",
      word: "返 回"
    },
    title: "欢迎使用Ben.支持",
    content: "mdi-chevron-double-down",
    contents: [
      {
        title: "当前版本"
      },
      {
        icon: "mdi-monitor-cellphone",
        title: "更新内容",
        content: "前往查看"
      }
    ],
    content1: ["忘记密码了?", "忘记密码了, 没关系"],
    content2: [
      "为什么页面样式视图混乱? 显示功能无法正常使用?",
      "我们目前对Chrome,Firefox支持良好,我们也建议您用这些浏览器打开以确保正常使用, 详情请参考下列浏览器支持表"
    ],
    content3: [
      "联系我们",
      "如果有特殊疑问,请发送邮件到 <code>ben_k8s@163.com</code> 邮箱,我们会尽快与您取得联系"
    ],
    supp_list: [
      {
        icon: "mdi-google-chrome",
        color: "red",
        icon2: "mdi-check-circle",
        color2: "green",
        supp: "Chromium (Chrome, Edge Insider)"
      },
      {
        icon: "mdi-firefox",
        color: "orange",
        icon2: "mdi-check-circle",
        color2: "green",
        supp: "Firefox"
      },
      {
        icon: "mdi-edge",
        color: "cyan darken-2",
        icon2: "mdi-check-circle",
        color2: "green",
        supp: "Edge"
      },
      {
        icon: "mdi-apple-safari",
        color: "blue",

        icon2: "mdi-check-circle",
        color2: "green",
        supp: "Safari 10 +"
      },
      {
        icon: " mdi-edge-legacy",
        color: "blue",

        icon2: "mdi-check-circle",
        color2: "green",
        supp: "IE11 +"
      },
      {
        icon: " mdi-internet-explorer",
        color: "",
        icon2: "mdi-close-circle",
        color2: "red",
        supp: "IE9 / IE10"
      }
    ]
  }
};

export const LOGIN = {
  VIEW: {
    btn: {
      icon: "mdi-arrow-left",
      word: "返 回"
    },
    subtitle: " 花费很少的时间分享日程安排"
  }
};

export const SIGNUP = {
  VIEW: {
    subtitle: "创建您的线上账号来分享您的时间安排给您的团队",
    word1: "已经有账号了? ",
    btn: {
      icon: "mdi-arrow-left",
      word: "返 回"
    }
  }
};

export const REGISTER = {
  VIEW: {
    subtitle: "就差一步! 填写您的用户信息",
    word1: "已经有账号了? "
  }
};

export const EMPLOYEE = {
  VIEW: {
    subtitle: "Hi! 在加入公司之前先核对并完善您的个人信息!"
  }
};
