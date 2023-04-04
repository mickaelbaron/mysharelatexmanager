# Build env
FROM node:lts-alpine as build-npm-stage
LABEL maintainer="Mickael BARON (baron.mickael@gmail.com)"

ARG script_name=build

WORKDIR /mysharelatexmanager-vuejs
COPY package*.json ./
RUN npm install
COPY public ./public
COPY index.html ./
COPY src ./src
COPY vite.config.js ./
COPY .env.production ./
COPY .env.subpath ./

RUN npm run $script_name

# Run env
FROM nginx:stable-alpine
COPY --from=build-npm-stage /mysharelatexmanager-vuejs/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
