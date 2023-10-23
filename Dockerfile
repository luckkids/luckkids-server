FROM openjdk:17-oracle
RUN microdnf install findutils

RUN mkdir -p /home/project/luck-kids-server
WORKDIR /home/project/luck-kids-server

COPY . /home/project/luck-kids-server
