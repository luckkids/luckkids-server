FROM openjdk:17-oracle
RUN microdnf install findutils

RUN mkdir -p /home/project/luck-kids-server
WORKDIR /home/project/luck-kids-server

COPY . /home/project/luck-kids-server

RUN ./gradlew clean
RUN ./gradlew bootJar

ENTRYPOINT java -Duser.timezone=GMT+09:00 -jar -Dspring.profiles.active=local,real-db /home/project/luck-kids-server/build/libs/luck-kids-server-0.0.1-SNAPSHOT.jar
