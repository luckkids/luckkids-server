#!/bin/bash

create_log_file() {
    local file_path=$1
    if [ ! -f "$file_path" ]; then
        touch "$file_path"
    fi
}

# 기본 변수 설정
BASE_DIR="/home/ec2-user/luck-kids-server"
LOG_DIR="${BASE_DIR}/log"
LOG_FILE="${LOG_DIR}/deploy.log"
ERR_LOG_FILE="${LOG_DIR}/deploy_err.log"
JAR_PATH="${BASE_DIR}/*.jar"

# 로그 디렉토리 및 파일 생성
mkdir -p "$LOG_DIR"
create_log_file "$LOG_FILE"
create_log_file "$ERR_LOG_FILE"

# 현재 실행 중인 Java 프로세스 ID 추출 및 종료
CURRENT_PID=$(pgrep -fl .jar | grep '[j]ava' | awk '{print $1}')
if [ -z "$CURRENT_PID" ]; then
    echo "No running Java process."
else
    echo "Killing process with ID: $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 3
fi

# JAR 파일 실행 및 로깅
echo "JAR path: $JAR_PATH"
nohup java -jar $JAR_PATH >> $LOG_FILE 2>> $ERR_LOG_FILE &

echo "Java application deployment successful."