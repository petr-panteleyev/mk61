# Сборка

Окружение:
- JDK 25+
- Maven 3.9.X

```
export JAVA_HOME=/path/to/jdk25
mvn clean verify
```

JAR приложения и зависимости будут расположены в каталоге `target/jmods`.

## Запуск из проекта

```
mvn exec:exec@run
```

## Дистрибутив

Скачайте и распакуйте [JavaFX JMODs distribution](https://jdk.java.net/javafx26/).

```
export JAVAFX_JMODS=/path/to/javafx-jmods-{javafx-version}
mvn -DskipTests=true clean verify jpackage:jpackage
```

### OS X и MS Windows

На этих платформах каталог ```target/dist``` будет содержать пакет для установки.

### Linux

На Linux каталог ```target/dist``` будет содержать образ приложения, который может быть запущен как
```MK-61/bin/MK-61```.

Предоставляется скрипт ```bin/install.sh``` для автоматической установки образа и создания ярлыка для запуска
```$HOME/.local/share/applications/mk61.desktop```
