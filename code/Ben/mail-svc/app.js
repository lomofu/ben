const Koa = require('koa');
const app = new Koa();
const json = require('koa-json');
const onerror = require('koa-onerror');
const bodyparser = require('koa-bodyparser');
const logger = require('koa-logger');
const router = require('./routes/router');
const Moment = require('moment');

onerror(app);

app.use(bodyparser({
    enableTypes: ['json', 'form', 'text']
}));

const loggers = logger((str) => {
    console.log(Moment().format('YYYY-MM-DD HH:mm:ss') + str);
});

app.use(json());
app.use(loggers);

// logger
app.use(async (ctx, next) => {
    const start = new Date();
    const ms = new Date() - start;
    console.log(` -> 方法: ${ctx.method} \n -> 路径: ${ctx.path} \n -> 耗时: ${ms}ms`);
    await next();
});

// routes
app.use(router.routes()).use(router.allowedMethods());

// error-handling
app.on('error', (err, ctx) => {
    console.error('Error\n', err, ctx);
});


module.exports = app;
