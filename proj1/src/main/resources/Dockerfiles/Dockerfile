# Use the official Node.js 16 image
FROM node:16

# Update package repositories and install logrotate and cron
USER root

RUN mkdir -p /app
RUN mkdir -p /app/log
RUN mkdir -p /app/certs

# Set the working directory
WORKDIR /app

RUN chmod -R 777 /app
COPY package.json /app
COPY package-lock.json /app
COPY .npmrc /app
COPY startup.sh /app/startup.sh
COPY logify.sh /app/logify.sh

RUN npm install -g npm@9.7.1
RUN npm install --force

RUN chown -R node /app/node_modules

COPY . /app

RUN ls /app/

RUN chmod -R 777 /app/certs
RUN chmod -R 777 /app/log
RUN chmod +x /app/startup.sh

EXPOSE 3000

RUN chmod -R 777 /app/log
RUN touch /app/log/formio.log && chmod 777 /app/log/formio.log

RUN chmod -R 777 logify.sh /app/log/formio.log /app/logrotate2.sh
RUN chown node:node logify.sh /app/log/formio.log /app/logrotate2.sh
RUN chmod +x logify.sh
RUN chmod -R u+rwx /app/log
RUN apt-get update


USER node
ENTRYPOINT ["bin/sh", "-c" , "/app/startup.sh"]