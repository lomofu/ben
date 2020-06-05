const Koa = require("koa");
const app = new Koa();
const json = require("koa-json");
const onerror = require("koa-onerror");
const bodyparser = require("koa-bodyparser");
const logger = require("koa-logger");
const mongoose = require("mongoose");

const config = require(`./config/env/${process.env.NODE_ENV}`);
const router = require("./routes/www");

// error handler
onerror(app);

// middlewares
app.use(
  bodyparser({
    enableTypes: ["json", "form", "text"]
  })
);
app.use(json());
app.use(logger());

//mongdb
mongoose.Promise = global.Promise;
mongoose.set("useCreateIndex", true);
mongoose.set("useFindAndModify", false);
mongoose
  .connect(config.mongodb_config.connect, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
    poolSize: 10,
    socketTimeoutMS: 0,
    keepAlive: true,
    bufferMaxEntries: 0
  })
  .then(
    () => {
      console.log("ben connect success");
    },
    err => console.error("error!" + err)
  );

// logger
app.use(async (ctx, next) => {
  const start = new Date();
  const ms = new Date() - start;
  console.log(
    ` -> 方法: ${ctx.method} \n -> 路径: ${ctx.path} \n -> 耗时: ${ms}ms`
  );
  await next();
});

// routes
app.use(router.routes()).use(router.allowedMethods());

// error-handling
app.on("error", (err, ctx) => {
  console.error("server error\n", err, ctx);
});

module.exports = app;
