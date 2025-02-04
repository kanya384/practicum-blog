# Блог

Веб-приложение блог, написанное с использованием Spring Framework. Приложение состоит из двух веб-страниц: лента постов,
страница поста.

## Технологии

- Java 21
- Spring Framework 6
- Spring Boot
- Spring Data
- Spring Web MVC
- Thymeleaf
- Project Lombok

## Использование

Для запуска тестов использовать команду:

```sh
./gradlew clean test
```

Для сборки приложение использовать команду:

```sh
./gradlew clean bootJar
```

Для запуска приложения выполнить:

```sh
./build/libs/blog-0.0.1-SNAPSHOT.jar
```

Приложение будет доступно по адресу:

```
http://<адрес контейнера>/blog/posts
```



