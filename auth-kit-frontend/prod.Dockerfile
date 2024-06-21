FROM node:14
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
COPY ./ ./
# ARG DEPLOY_ENV
# ENV DEPLOY_ENV=$DEPLOY_ENV
RUN npm install -g @angular/cli
RUN npm install
RUN npm run build:prod
EXPOSE 5011
CMD [ "node", "server.js" ]