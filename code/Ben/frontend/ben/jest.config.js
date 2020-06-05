module.exports = {
  preset: "@vue/cli-plugin-unit-jest",
  verbose: true,
  testURL: "http://localhost/",
  moduleFileExtensions: ["js", "json", "vue"],
  moduleNameMapper: {
    "^@/(.*)$": "<rootDir>/src/$1"
  },
  testMatch: [
    //匹配测试用例的文件
    "<rootDir>/tests/unit/components/public/*.spec.js",
    "<rootDir>/tests/unit/components/home/*.spec.js"
  ],
  transform: {
    "^.+\\.js$": "<rootDir>/node_modules/babel-jest",
    ".*\\.(vue)$": "vue-jest"
  },
  testPathIgnorePatterns: ["<rootDir>/test/e2e"],
  collectCoverage: true,
  collectCoverageFrom: [
    "src/**/*.{js,vue}",
    "!src/main.js",
    "!src/router/index.js",
    "!**/node_modules/**"
  ],
  coverageReporters: ["html", "text-summary"]
};
