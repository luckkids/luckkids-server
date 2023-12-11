#!/bin/bash

# 현재 Nginx가 사용 중인 포트 찾기
function find_current_port()
{
    # Nginx 설정 파일에서 포트 번호 추출
    CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
    echo "${CURRENT_PORT}"
}

# 쉬고 있는 포트 찾기
function find_idle_port()
{
    CURRENT_PORT=$(find_current_port)

    # 포트 번호에 따라 쉬고 있는 포트 결정
    if [ "${CURRENT_PORT}" == "8081" ]
    then
      echo "8082"
    else
      echo "8081"
    fi
}