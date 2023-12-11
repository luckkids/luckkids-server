#!/bin/bash

REPOSITORY=/home/ec2-user/app

CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  sudo kill ${TARGET_PID}
fi

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME에 실행권한 추가"
chmod +x $JAR_NAME

LOG_FILE="$REPOSITORY/nohup.out"

echo "> 로그 파일이 존재하지 않으면 생성하고, 존재하면 내용을 유지 (쓰기 권한 확인)"
touch $LOG_FILE

echo "> $JAR_NAME 실행"
nohup java -jar -Dserver.port=${TARGET_PORT} -Dspring.profiles.active=prod $JAR_NAME > $LOG_FILE 2>&1 &

echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0
