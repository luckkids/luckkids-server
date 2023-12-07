#!/bin/bash

# 현재 실행 중인 Java 프로세스 찾기
CURRENT_PID=$(pgrep -fl .jar | grep '[j]ava' | awk '{print $1}')
echo "Current running Java process ID: $CURRENT_PID"

# 실행 중인 프로세스가 있다면 종료
if [ -z "$CURRENT_PID" ]; then
    echo "No running Java process."
else
    echo "Killing process with ID: $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 3
fi

# JAR 파일 경로 설정
JAR_PATH="/home/ec2-user/luck-kids-server/*.jar"
echo "JAR path: $JAR_PATH"

# JAR 파일 실행
nohup java -jar $JAR_PATH >> /home/ec2-user/luck-kids-server/log/deploy.log 2>> /home/ec2-user/luck-kids-server/log/deploy_err.log &

echo "Java application deployment successful."