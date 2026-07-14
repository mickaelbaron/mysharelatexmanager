FROM nginx:stable-alpine

ARG VERSION=1.0.0
ARG REVISION=SNAPSHOT
ARG SUBPATH=""

LABEL \
    org.opencontainers.image.title="MySharelatexManager RP" \
    org.opencontainers.image.authors="Mickael BARON (baron.mickael@gmail.com)" \
    org.opencontainers.image.url="local" \
    org.opencontainers.image.source="https://github.com/mickaelbaron/mysharelatexmanager" \
    org.opencontainers.image.version="$VERSION" \
    org.opencontainers.image.revision="$REVISION"

COPY default.conf.template /tmp/default.conf.template

RUN if [ -n "$SUBPATH" ]; then \
        SUBPATH="/$(echo "$SUBPATH" | sed 's#^/*##; s#/*$##')"; \
        REWRITE="rewrite ^${SUBPATH}(/.*)\$ \$1 break;"; \
    else \
        REWRITE=""; \
    fi && \
    sed \
        -e "s|@@SUBPATH@@|$SUBPATH|g" \
        -e "s|@@REWRITE@@|$REWRITE|g" \
        /tmp/default.conf.template \
        > /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]