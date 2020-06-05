FROM nginx
MAINTAINER lomofu <lomofu@qq.com>
RUN mkdir /app
COPY dist/ /app
COPY nginx.conf /etc/nginx/nginx.conf
RUN echo "echo build success!"
