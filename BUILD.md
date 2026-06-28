# Сборка

Окружение:
- JDK 25+
- JavaFX 26
- Maven 3.9.14

```shell script
export JAVA_HOME=/path/to/jdk25
mvn clean verify
```

JAR приложения и зависимости будут расположены в каталоге ```target/jmods```.

# Запуск из проекта

```shell
mvn exec:exec@run
```

# Дистрибутив

Скачайте и распакуйте [JavaFX JMODs distribution](https://jdk.java.net/javafx26/).

```shell
export JAVAFX_JMODS=/path/to/javafx-jmods-{javafx-version}
mvn -DskipTests=true clean verify jpackage:jpackage
```

## OS X и MS Windows

На этих платформах каталог ```target/dist``` будет содержать пакет для установки.

## Linux

На Linux каталог ```target/dist``` будет содержать образ приложения, который может быть запущен как
```MK-61/bin/MK-61```.

Предоставляется скрипт ```bin/install.sh``` для автоматической установки образа и создания ярлыка для запуска 
```$HOME/.local/share/applications/mk61.desktop```

---

# Build

Prerequisites:
- JDK 25+
- JavaFX 26
- Maven 3.9.14

```shell script
export JAVA_HOME=/path/to/jdk25
mvn clean verify
```

Application JAR and all dependencies will be placed in ```target/jmods```.

# Run

```shell
mvn exec:exec@run
```

# Binary Distribution

Download and unpack [JavaFX JMODs distribution](https://jdk.java.net/javafx26/).

```shell
export JAVAFX_JMODS=/path/to/javafx-jmods-{javafx-version}
mvn -DskipTests=true clean verify jpackage:jpackage
```

## OS X and MS Windows

On these platforms ```target/dist``` directory will contain an installation package.

## Linux

On Linux ```target/dist``` directory will contain an application image that can be moved to the desired location
and launched as ```MK-61/bin/MK-61```.

There is a convenience script ```bin/install.sh``` that can be used to automatically install and create desktop link
file ```$HOME/.local/share/applications/mk61.desktop```
