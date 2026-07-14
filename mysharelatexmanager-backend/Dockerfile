# Build env
FROM maven:3.9.16-eclipse-temurin-11 AS builder

WORKDIR /mysharelatexmanager
COPY pom.xml .
COPY mysharelatexmanager-api/pom.xml mysharelatexmanager-api/
COPY mysharelatexmanager-server/pom.xml mysharelatexmanager-server/

RUN mvn -B dependency:go-offline

COPY mysharelatexmanager-api mysharelatexmanager-api
COPY mysharelatexmanager-server mysharelatexmanager-server

RUN mvn -f pom.xml clean package -DskipTests

# Run env
FROM openliberty/open-liberty:kernel-slim-java11-openj9-ubi-minimal

ARG VERSION=1.0.0
ARG REVISION=SNAPSHOT

LABEL \
    org.opencontainers.image.title="MySharelatexManager Backend" \
    org.opencontainers.image.authors="Mickael BARON" \
    org.opencontainers.image.url="local" \
    org.opencontainers.image.source="https://github.com/mickaelbaron/mysharelatexmanager" \
    org.opencontainers.image.version="$VERSION" \
    org.opencontainers.image.revision="$REVISION" \
    vendor="Mickael BARON" \
    name="mysharelatexmanager-backend" \
    version="$VERSION-$REVISION" \
    summary="MySharelatexManager Backend provides REST APIs to manage users and projects for self-hosted Overleaf/ShareLaTeX Community Edition instances." \
    description="MySharelatexManager Backend is a RESTful service for managing users and projects in self-hosted Overleaf/ShareLaTeX Community Edition instances. It provides APIs for user administration, project management, transfers, and data persistence."

WORKDIR /config
COPY --chown=1001:0 --from=builder /mysharelatexmanager/mysharelatexmanager-server/src/main/liberty/config/server.xml /config/server.xml
COPY --chown=1001:0 --from=builder /mysharelatexmanager/mysharelatexmanager-server/target/mysharelatexmanager-server.war /config/apps/

EXPOSE 9080

RUN features.sh && configure.sh
