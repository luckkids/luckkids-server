FROM openjdk:17-oracle
RUN microdnf install findutils

ARG MAIL_PASSWORD
ARG JWT_SECRET_KEY
ARG DEV_DB_PASSWORD
ARG PROD_DB_PASSWORD

RUN mkdir -p /home/project/luck-kids-server
WORKDIR /home/project/luck-kids-server

COPY . /home/project/luck-kids-server

RUN ./gradlew clean
# Gradle 빌드를 실행하기 전에 환경 변수를 설정하고 실행
RUN export MAIL_PASSWORD=$MAIL_PASSWORD && \
    export JWT_SECRET_KEY=$JWT_SECRET_KEY && \
    export DEV_DB_PASSWORD=$DEV_DB_PASSWORD && \
    export PROD_DB_PASSWORD=$PROD_DB_PASSWORD && \
    ./gradlew bootJar

ENTRYPOINT java -Dserver.servlet.context-path=/luckkids -Duser.timezone=GMT+09:00 -jar -Dspring.profiles.active=dev /home/project/luck-kids-server/build/libs/luck-kids-server-1.0.1-SNAPSHOT-*.jar
