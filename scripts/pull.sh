#!/bin/bash

# 현재 작업 디렉토리 확인
current_dir=$(pwd)

# "backend/script" 디렉토리에서 실행된 경우
if [[ "$current_dir" == */luckkids-server/script ]]; then
  echo "현재 디렉토리가 'luckkids-server/script'입니다. 상위 디렉토리로 이동합니다."
  cd ..
elif [[ "$current_dir" == */luckkids-server ]]; then
  echo "현재 디렉토리가 'luckkids-server'입니다. 실행을 계속합니다."
else
  echo "이 스크립트는 'luckkids-server' 또는 'luckkids-server/script' 디렉토리에서 실행되어야 합니다."
  exit 1
fi


git pull origin dev

git submodule update --remote

./gradlew copySecret

