# Ben
*An cloud native application*

*一款云原生应用*





## 简介

这是一个以排班推送为主要功能的管理saas应用，支持公告推送，排班推送，权限管理等。本应用意在探索云原生应用为软件开发带来的全新开发形式。同时对于微服务，devops等也有实践。





## 预览

### 首页

![首页](img/Ben.-首页.png)



### 登录

![Ben.-登录](img/Ben.-登录.png)



### 个人看板

![APP.-个人看板](img/APP.-个人看板.png)



![APP.-个人看板](img/APP.-个人看板1.png)



### 我的排班

![APP.-我的排班](img/APP.-我的排班.png)





### 团队管理

![FireShot Capture 006 - APP. - 团队列表 - app.lomofu.com](img/APP.-团队列表.png)



### 项目详情

![APP.-项目详情](img/APP.-项目详情.png)



### 排班管理

![APP.-排班管理](img/APP.-项目详情1.png)



![APP.-排班管理1](img/APP.-项目详情2.png)



### 设置中心

![FireShot Capture 012 - APP. - 设置中心 - app.lomofu.com](img/APP.-设置中心.png)



### 暗黑模式

![FireShot Capture 013 - APP. - 个人看板 - app.lomofu.com](img/APP.-个人看板(暗黑).png)



## 软件架构



### 微服务架构（前后端分离）

![Ben 微服务架构](img/Ben微服务架构.png)

本应用采用的是微服务架构风格，同时开发模式也是前后端分离。前端由vue构建的，后端是由java和nodejs两种语言构建的服务。



### 云原生应用架构 

![Ben 云原生应用架构](img/Ben云原生应用架构.png)





## 技术清单



### 开发框架

- #### Vue.js

- #### Spring Boot

- #### Spring Cloud中组件（Feign，Spring Cloud Gateway）

- #### Koa2



### 应用服务

- #### MySql

- #### MongoDB

- #### Redis

- #### RocketMQ

- #### Seata

- #### ELK



### 运维服务

- #### Docker

- #### Kubernetes

- #### Istio

- #### Rancher

- #### Jenkins

- #### Sonar Qube





## 环境准备

由于本应用是云原生应用，所以支持一键部署到容器云环境，支持kubernetes环境。可以自行搭建kubernetes环境，或者借助云厂商的kubernetes环境。笔者这里以esc云服务器为基础硬件，同时搭建rancher来创建kubernetes环境进行。



### 硬件准备

这里给出我的硬件服务器配置，根据kubernetes以及rancher官方的推荐。单节点最低配置为 2 Core，运存最少4GB。

| 节点  | 配置 | 操作系统 | Docker |
| ------ | ---- | ---- | ---- |
| Master | 2Core 8GB 40GB | Centos 7.6 | 18.6.3 |
| Node1  | 4Core 8GB 40GB | Centos 7.6 | 18.6.3 |
| Node2  | 4Core 8GB 40GB | Centos 7.6 | 18.6.3 |
| Node3  | 2Core 4GB 40GB | Centos 7.6 | 18.6.3 |



### 部署rancher

**1 准备Linux主机**

准备一台已安装64位Ubuntu 16.04或18.04的Linux主机（或其他[Rancher所支持的Linux发行版](https://rancher.com/support-maintenance-terms/all-supported-versions/rancher-v2.3.4/)），要求至少4GB内存。在该主机上安装[Rancher支持的Docker版本](https://rancher.com/support-maintenance-terms/all-supported-versions/rancher-v2.3.4/)。

**2 运行Server**

在主机上执行以下Docker命令，完成Rancher的安装与运行:

```bash
sudo docker run -d --restart=unless-stopped -p 80:80 -p 443:443 -v <主机路径>:/var/lib/rancher/ rancher/rancher:stable
```

之后，打开浏览器，输入https://<安装容器的主机名或IP地址>，您即可以访问Rancher Server的UI了。跟随用户界面给您的引导，即可设置完成您的第一个Rancher集群。



### 创建Kubernetes集群

rancher已经将创建Kubernetes做的非常简单，只需要点击页面上的创建集群，根据选项最后生成的命令在每个节点的主机上运行即可。详情可以参考[官方文档](https://docs.rancher.cn/rancher2x/#_1-what-s-rancher?)



### 其他环境准备

- #### MySql 数据库

- #### Redis数据库

- #### [RocketMQ](https://github.com/lomofu/documents/blob/2020/kubernetes/pratice/Rancher搭建RocketMQ.md)

- #### [ELK](https://github.com/lomofu/documents/blob/2020/kubernetes/pratice/Rancher搭建ELK.md)

- #### Seata





## 配置文件

因为采用docker容器的方式，所以需要提前构建好docker镜像并上传到镜像仓库。



### Account服务

> ##### 该服务由java编写，采用SpringBoot框架。account服务主要负责应用中账户，角色权限相关服务。对应代码为模块 `account-api` 和 `account-svc`。



**数据库**

- **MySql**

  创建ben_account数据库运行数据库脚本，位于`db/account/ben_account.sql`。

- **MongoDB**

  创建ben文档



**分布式事务Seata**

修改`account-svc/src/main/resources/file.conf`。这里修改的是seata中的事务管理器地址。

```conf
service {
  vgroupMapping.ben-tx-group = "default"
  //这里改成你部署的seata事务管理器
  default.grouplist = "seata.seata:8091"
  enableDegrade = false
  disableGlobalTransaction = false
}
```



**分布式锁**

修改`account-svc/src/main/resources/redisson-对应环境-config.yml`。需要注意的是，这里提供了两个环境的配置文件，如果你使用其中一个需要修改对应的环境的配置文件即可。

```yaml
singleServerConfig:
	# 你的redis服务地址
  address: "redis://redis:6379"
  # 对应的密码
  password: xxxx
```



**Oss存储**

本项目中oss主要用于头像存储功能，采用的是阿里云oss，如果无特殊需求，可以不用开启修改。



**后续**

只需要通过maven构建SpringBoot应用，并通过`account-svc/Dockerfile` 构建docker镜像即可。




<hr>


### Company服务

> ##### 该服务由java编写，采用SpringBoot框架。company服务主要负责公司，团队，项目，消息相关服务。对应代码为模块 `company-api` 和 `company-svc`。



**数据库**

创建ben_company数据库运行数据库脚本，位于`db/company/ben_company.sql`。



**分布式事务Seata**

修改`company-svc/src/main/resources/file.conf`。这里修改的是seata中的事务管理器地址。

```conf
service {
  vgroupMapping.ben-tx-group = "default"
  //这里改成你部署的seata事务管理器
  default.grouplist = "seata.seata:8091"
  enableDegrade = false
  disableGlobalTransaction = false
}
```



**分布式锁**

修改`company-svc/src/main/resources/redisson-对应环境-config.yml`。需要注意的是，这里提供了两个环境的配置文件，如果你使用其中一个需要修改对应的环境的配置文件即可。

```yaml
singleServerConfig:
	#你的redis服务地址
  address: "redis://redis:6379"
  #对应的密码
  password: xxxx
```



**后续**

只需要通过maven构建SpringBoot应用，并通过`company-svc/Dockerfile` 构建docker镜像即可。




<hr>



### Bot服务

> ##### 该服务由java编写，采用SpringBoot框架。bot服务主要负责消息推送相关服务，与消息中间件对接。对应代码为模块 `bot-api` 和 `bot-svc`。



**后续**

只需要通过maven构建SpringBoot应用，并通过`bot-svc/Dockerfile` 构建docker镜像即可。




<hr>



### SMS服务

> ##### 该服务由java编写，采用SpringBoot框架。SMS服务主要负责消息推送中短信发送的实现。对应代码为模块 `sms-api` 和 `sms-svc`。



需要注意的是，由于某些原因，应用在构建的时候采用了两家的短信服务。一家是京东短信，主要负责短信登录验证码的发送，或者登录错误次数过多的发送。另一个负责推送相关的发送。当然修改为统一一家也十分简单，位于`com/ben/sms/service/impl/SmeServiceImpl.java`这个类下，参考即可修改。



**后续**

只需要通过maven构建SpringBoot应用，并通过`account-svc/Dockerfile` 构建docker镜像即可。




<hr>



### www服务

> 该服务由Node.js编写，采用Koa2框架。www服务主要负责新账户的注册相关服务。对应代码为模块 `www-svc`。



**api调用**

修改`www-svc/config/env/对应环境.js`

```javascript
api: {
  get accountSvcBaseApi() {
    return "http://account-svc:8080/api/accounts";
  },
  get botSvcBaseApi() {
    return "http://bot-svc:8080/api/bot";
  }
}
```

如果api无特殊需求不建议修改，这里罗列出来方便查寻。寻址是通过kubernetes的服务发现。



**MongoDB**

修改`www-svc/config/env/对应环境.js`

```javascript
 mongodb_config: {
   //你的mongodb地址
    connect: "mongodb://mongodb.mongodb:27017/ben"
  }
```

这里修改你的mongodb的连接地址



**后续**

只需要通过npm安装相关依赖，并通过`www-svc/Dockerfile` 构建docker镜像即可。




<hr>



### Mail服务

> 该服务由Node.js编写，采用Koa2框架。www服务主要负责邮件发送的具体实现。对应代码为模块 `mail-svc`。



**api调用**

修改`mail-svc/config/环境/EndPoint.js`

```javascript
//这里修改为你前端对应的域名或者ip地址
base: {
  get url() {
    //ex: https://www.lomofu.com
    return "域名/ip";
  }
},
register: {
  get url() {
    //ex: https://www.lomofu.com/register
    return "域名/ip/register";
  }
},
employee: {
  get url() {
     //ex: https://www.lomofu.com/employee
    return "域名/ip/employee";
  }
},
reset: {
  get url() {
     //ex: https://www.lomofu.com/reset
    return "域名/ip/reset";
  }
}
```



**smtp**

修改对应你的smtp服务器用来发送邮件。位于`mail-svc/config/SMTP.js`

```javascript
get server() {
  //ex :163 支持的sevice名单 https://nodemailer.com/smtp/well-known/
  return "xxx";
},
get user() {
  //ex : example@example.com
  return "xxx@xxxx.com";
},
get pass() {
  //ex: 邮箱授权码 这里用base64加密后的
  return "xxxxx";
},
get logo() {
  //ex: 邮箱末尾的logo 如果你有http服务器可以填写相对应的logo地址
  return "xxxxx";
}
```



**token**

这个是用来做服务验证的。位于`mail-svc/config/Token.js`

```javascript
get SECRET() {
    return "YzA0ODM3ZGY2MWYzNmJhNjg0NjYwOWFlNmI4MTQxZDg5NmQ5NmNiOGJkMjJiYzI1OTU3ZDBmY2ViMDYxNjY5NjIwOTkxYjQ5MDdkMzEzZWQ0NTY4MzRkY2YxNzJhZDA2NDMxNzEyMDc5MjUyOWExY2RkZjE4OGVjYTRlMDMzYWM="
}
```

这里的密文对应着account 服务中的secret，当然不改也可以，暂时不启用该校验。



**后续**

只需要通过npm安装相关依赖，并通过`mail-svc/Dockerfile` 构建docker镜像即可。




<hr>

### 前端

#### ben

> 这是未注册或未登录下的单页spa，位于`frontend/ben`下,是一个vue工程。



**部署url**

修改位于`frontend/ben/.env.环境名`

```vue
//环境名
VUE_APP_NAME="DEV"
//app运行端口
VUE_APP_PORT = 8080
//app 未登录域名
VUE_APP_URL = "http://www.lomofu.com"
//app 已登录域名
VUE_APP_CENTER = "http://app.lomofu.com"
```



**https**

上线需要https支持，所以修改`frontend/ben/nginx.conf`

```nginx
//如果需要https 开启
listen 443 ssl;
//服务名 ex: www.lomofu.com
server_name  xxxxx;

ssl_session_timeout 5m;
ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
ssl_ciphers   ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;

//ssl证书位置 ex:/usr/cert/xxxxxx.pem;
ssl_certificate your certificate path;

//ssl秘钥位置 ex:/usr/cert/xxxxxxx.key
ssl_certificate_key your certificate path;
ssl_prefer_server_ciphers on;
```




<hr>

#### app

> 这是登录后的单页spa，位于`frontend/app`下，是一个vue工程。



**部署url**

修改位于`frontend/ben/.env.环境名`

```vue
//环境名
VUE_APP_NAME="DEV"
//app运行端口
VUE_APP_PORT = 8080
//app 未登录域名
VUE_APP_URL = "http://www.lomofu.com"
//app 已登录域名
VUE_APP_CENTER = "http://app.lomofu.com"
//api 后端网关域名
VUE_APP_GATEWAY="https://api.lomofu.com"
```



**https**

上线需要https支持，所以修改`frontend/ben/nginx.conf`

```nginx
//如果需要https 开启
listen 443 ssl;
//服务名 ex: app.lomofu.com
server_name  xxxxx;

ssl_session_timeout 5m;
ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
ssl_ciphers   ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;

//ssl证书位置 ex:/usr/cert/xxxxxx.pem;
ssl_certificate your certificate path;

//ssl秘钥位置 ex:/usr/cert/xxxxxxx.key
ssl_certificate_key your certificate path;
ssl_prefer_server_ciphers on;
```



## Kubernetes部署文件

部署这里推荐使用rancher的可视化界面完成，非常简单

![rancher控制界面](img/rancher控制界面.png)

部署文件参考位于`k8s/test`下



### ConfigMap

为了更好配置和管理应用的配置，采用的是configmap统一管理，通过环境变量注入

![configmap配置](img/configmap配置.png)

需要configmap支持的服务的配置文件位于`k8s/test/config`下，该文件中用`xxxxxxx`的都需要修改

例如：`k8s/test/config/bot-config.yaml`

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: bot-config
data:
  UNDERTOW_IO_THREADS: 8
  UNDERTOW_WORKER_THREADS: 64
  UNDERTOW_BUFFER_SIZE: 1024
  UNDERTOW_MAX_POST: 1000
  MAIL_SERVICE_ENDPOINT: http://mail-svc:3001
  ASYNC_CORE_POOL: 10
  ASYNC_MAX_POOL: 30
  ASYNC_QUEUE_CAPACITY: 100
  ASYNC_WAIT_FOR_COMPLETE: true
  ASYNC_KEEP_ALIVE: 60
  #redis host ex:  redis-proxy-nlb.jvessel-open-sh.jdcloud.com
  REDIS_HOST: xxxxxxx
  REDIS_PORT: 6379
  REDIS_TIMEOUT: 3000
  #redis password
  REDIS_PASSWORD: xxxxxxx
  #go easy websocket host 参考:https://www.goeasy.io/
  WEBSOCKET_HOST: http://rest-hangzhou.goeasy.io/publish
  #go easy key
  WEBSOCKET_KEY: BC-696d48c8c9794fb6a2ad85230c12365a
  #rocketmq 服务器地址
  ROCKETMQ_NAME_SERVER: xxxxxxx
  #rocketmq 组 可自行配置 ex：bot-producer-dev
  ROCKETMQ_GROUP: xxxxxxx
```

 

### Istio

如果启用服务网格进行服务治理，请务必确认单节点配置不得2core，4GB否则会安装失败。istio的配置文件位于`k8s/test/mesh`下。

> #### 注意：
>
> 这里给出的配置文件仅为参考，我在本应用中主要配置了ben，app，gateway三个mesh配置。如果其余服务想要通过mesh治理，参考配置即可。



流量拓扑图

![Kiali-Console](img/Kiali-Console.png)



指标监控

![Grafana](img/Grafana.png)



## 后续

本应用属于本人毕设作品，因服务器原因，在部分情况没有考虑hpa。但是都可以参考kubernetes的网上资料进行部署。关于devops相关实践，稍后会在[这里](https://github.com/lomofu/documents/tree/2020/devops)更新。

