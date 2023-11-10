FROM openjdk:17-oracle
RUN microdnf install findutils

ARG test_db_url
ARG real_db_id
ARG real_db_password
ARG mail_password
ARG jwt_key
ARG slack_webhook_url

RUN mkdir -p /home/project/luck-kids-server
WORKDIR /home/project/luck-kids-server

COPY . /home/project/luck-kids-server

RUN ./gradlew clean
# Gradle 빌드를 실행하기 전에 환경 변수를 설정하고 실행
RUN export test_db_url=$test_db_url && \
    export real_db_id=$real_db_id && \
    export real_db_password=$real_db_password && \
    export mail_password=$mail_password && \
    export jwt_key=$jwt_key && \
    export slack_webhook_url=$slack_webhook_url && \
    ./gradlew bootJar

ENTRYPOINT java -Duser.timezone=GMT+09:00 -jar -Dspring.profiles.active=local,real-db /home/project/luck-kids-server/build/libs/luck-kids-server-0.0.1-SNAPSHOT.jar
