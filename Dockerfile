FROM ubuntu:16.04

MAINTAINER Krylov Sergey

# Обновление списка пакетов
RUN apt-get -y update

# Установка JDK
RUN apt-get install -y openjdk-8-jdk-headless

# Установка maven
RUN apt-get install -y maven

#
# Сборка проекта
#

# Копируем исходный код в Docker-контейнер
ENV WORK /opt
ADD . $WORK/java/
RUN mkdir -p /var/www/html

# Собираем и устанавливаем пакет
WORKDIR $WORK/java
RUN mvn package

# Объявлем порт docker
EXPOSE 80

CMD java -jar $WORK/java/target/highload-server-1.0-SNAPSHOT.jar