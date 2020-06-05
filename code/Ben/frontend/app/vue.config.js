const main = require("./public/manifest.json");
const CompressionPlugin = require("compression-webpack-plugin");
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");
// const { InjectManifest } = require("workbox-webpack-plugin");
const cdn = {
  externals: {
    vue: "Vue",
    vuex: "Vuex",
    moment: "moment",
    echarts: "echarts"
  },
  css: [
    "https://app.lomofu.com/cdn/css/vuetify.min.css",
    "https://app.lomofu.com/cdn/css/material-icons.css",
    "https://app.lomofu.com/cdn/css/v-charts/style.min.css"
  ],
  js: [
    "https://app.lomofu.com/cdn/js/vue.min.js",
    "https://app.lomofu.com/cdn/js/vuex.min.js",
    "https://app.lomofu.com/cdn/js/vuetify.min.js",
    "https://app.lomofu.com/cdn/js/moment.min.js",
    "https://app.lomofu.com/cdn/js/zh-cn.js",
    "https://app.lomofu.com/cdn/js/echarts.min.js"
  ]
};

module.exports = {
  publicPath: "/",
  outputDir: "dist",
  assetsDir: "",
  lintOnSave: true,
  runtimeCompiler: process.env.VUE_APP_NAME === "LOCAL",
  productionSourceMap: process.env.VUE_APP_NAME === "LOCAL",
  parallel: true,
  pwa:
    process.env.VUE_APP_NAME !== "LOCAL"
      ? {
          name: main.name,
          themeColor: main.theme_color,
          msTileColor: "#000000",
          appleMobileWebAppCapable: "yes",
          appleMobileWebAppStatusBarStyle: "black"
        }
      : {},
  devServer: {
    port: process.env.VUE_APP_PORT
  },
  chainWebpack: config => {
    if (process.env.VUE_APP_NAME !== "LOCAL") {
      config.plugins.delete("prefetch");
      config.plugins.delete("preload");
      config.plugin("html").tap(args => {
        if (process.env.VUE_APP_NAME !== "LOCAL") args[0].cdn = cdn;
        return args;
      });
      const imagesRule = config.module.rule("images");
      imagesRule
        .use("url-loader")
        .loader("url-loader")
        .tap(options => Object.assign(options, { limit: 6144 }));
      config.optimization.minimize(true);
      config.optimization.splitChunks({
        cacheGroups: {
          vendor: {
            chunks: "all",
            test: /node_modules/,
            name: "vendor",
            minChunks: 1,
            maxInitialRequests: 5,
            minSize: 0,
            priority: 100
          },
          common: {
            chunks: "all",
            test: /[\\/]src[\\/]js[\\/]/,
            name: "common",
            minChunks: 2,
            maxInitialRequests: 5,
            minSize: 0,
            priority: 60
          },
          styles: {
            name: "styles",
            test: /\.(sa|sc|c)ss$/,
            chunks: "all",
            enforce: true
          }
        }
      });
    }
  },
  configureWebpack: config => {
    let obj = {};
    obj.resolve = {
      alias: {
        svg: "@/assets/svg"
      }
    };
    if (process.env.VUE_APP_NAME !== "LOCAL") {
      obj.plugins = [
        new CompressionPlugin({
          filename: "[path].gz[query]",
          algorithm: "gzip",
          test: /\.js$|\.html$|\.css/,
          threshold: 10240,
          deleteOriginalAssets: false
        })
      ];
      config.plugins.push(
        new UglifyJsPlugin({
          uglifyOptions: {
            warnings: false,
            drop_debugger: true,
            drop_console: true,
            reduce_vars: true,
            pure_funcs: ["console.log"]
          },
          sourceMap: false,
          parallel: true
        })
      );
      config.externals = cdn.externals;
    }
    return obj;
  },
  css: {
    extract: false,
    sourceMap: false,
    loaderOptions: {
      scss: {
        prependData: '@import "~@/assets/css/global/global.scss";'
      }
    },
    requireModuleExtension: true
  },
  pluginOptions: {},
  transpileDependencies: ["vuetify"]
};
