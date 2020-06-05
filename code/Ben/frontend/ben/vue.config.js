const CompressionPlugin = require("compression-webpack-plugin");
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");

const cdn = {
  externals: {
    vue: "Vue",
    vuex: "Vuex"
  },
  css: [
    "https://cdn.bootcss.com/vuetify/2.1.0/vuetify.min.css",
    "https://cdn.bootcss.com/material-design-icons/3.0.1/iconfont/material-icons.css"
  ],
  js: [
    "https://cdn.bootcss.com/vue/2.6.11/vue.min.js",
    "https://cdn.bootcss.com/vuex/3.1.2/vuex.min.js",
    "https://cdn.bootcss.com/vuetify/2.2.15/vuetify.min.js"
  ]
};

module.exports = {
  publicPath: "/",
  outputDir: "dist",
  assetsDir: "",
  crossorigin: "anonymous",
  lintOnSave: true,
  runtimeCompiler: process.env.VUE_APP_NAME === "LOCAL",
  productionSourceMap: process.env.VUE_APP_NAME === "LOCAL",
  parallel: true,
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
          minRatio: 0.8,
          deleteOriginalAssets: false
        })
      ];
      config.plugins.push(
        new UglifyJsPlugin({
          uglifyOptions: {
            warnings: false,
            drop_debugger: true,
            drop_console: true,
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
    }
  },
  pluginOptions: {},
  transpileDependencies: ["vuetify"]
};
