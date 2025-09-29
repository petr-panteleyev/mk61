# Сборка и запуск приложения

Для сборки и работы с программой требуется [JDK 25](https://jdk.java.net/25/).

## Сборка

```shell
export JAVA_HOME=/path/to/jdk25
./mvnw clean verify
```

## Запуск из проекта

```shell
./mvnw exec:exec@run
```

## Linux

Для Linux можно собрать исполняемый образ и запускать его как обычную программу.

Скачайте и распакуйте [JavaFX JMODs distribution](https://jdk.java.net/javafx25/).

```shell
export JAVAFX_JMODS=/path/to/javafx-jmods-25
./bin/jlink.sh
[sudo] ./bin/install </install/path>
```

В каталоге ```/install/path/mk61``` будет находиться исполняемый образ, скрипт для запуска ```mk61.sh```, а также
готовый к использованию файл ```mk61.desktop```.
