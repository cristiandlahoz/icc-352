# Etapa 1: Construcción
FROM eclipse-temurin:21-jdk-jammy AS build
LABEL author="@github/cristiandlahoz"
WORKDIR /build
COPY . .
RUN ./gradlew shadowJar --no-daemon

# Etapa 2: Imagen Final (Solo JRE)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /build/build/libs/*-all.jar /app/app.jar

ENV PORT=8765
ENV PORT_GRPC=9876

EXPOSE 8765 9876

# El comando de inicio
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
