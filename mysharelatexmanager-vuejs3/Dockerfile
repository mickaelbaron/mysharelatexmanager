# Build env
FROM node:lts-alpine AS builder

ARG SUBPATH=""

WORKDIR /app

COPY package*.json ./
RUN npm install
COPY public ./public
COPY index.html ./
COPY src ./src
COPY vite.config.js ./
COPY .env.production.template ./

# Génération du .env.production
RUN if [ -n "$SUBPATH" ]; then \
      SUBPATH="/$(echo "$SUBPATH" | sed 's#^/*##; s#/*$##')"; \
    fi && \
    sed \
      -e "s|@@SUBPATH@@|${SUBPATH}/|g" \
      -e "s|@@SERVER_URL@@|${SUBPATH}/server|g" \
      .env.production.template > .env.production

RUN npm run build

# Run env
FROM nginx:stable-alpine

ARG VERSION=1.0.0
ARG REVISION=SNAPSHOT

LABEL \
    org.opencontainers.image.title="MySharelatexManager Frontend" \
    org.opencontainers.image.authors="Mickael BARON (baron.mickael@gmail.com)" \
    org.opencontainers.image.url="local" \
    org.opencontainers.image.source="https://github.com/mickaelbaron/mysharelatexmanager" \
    org.opencontainers.image.version="$VERSION" \
    org.opencontainers.image.revision="$REVISION"

COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
