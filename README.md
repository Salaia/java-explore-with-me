# Explore with me
Платформа для публикации событий и поиска товарищей для посещения мероприятий.

<div>
      <img src="assets/landing.png" title="EWM landing" alt="explore with me landing"/>
</div>


### Функционал программы:

Explore with me - микросервисное приложение. В программу входят два микросервиса:
* main-service - основная часть приложения, содержащая бизнес-логику
* stats-service - сервис для сбора статистики просмотра событий по ip

Микросервисная архитектура Explore with me выглядит следующим образом:

<div>
      <img src="assets/micro-services architecture.png" title="microservice architecture" alt="microservice architecture"/>
</div>


### 🛠 Tech & Tools

<div>
      <img src="https://github.com/Salaia/icons/blob/main/green/Java.png?raw=true" title="Java" alt="Java" height="40"/>
</div>

### Инструкция по развёртыванию ▶️

1) Склонируйте репозиторий и перейдите в него
   https://github.com/Salaia/java-explore-with-me.git

2) Запустите проект в выбранной IDE: 
   * Сначала нужно запустить сервис статистики: stats-server/stats-service/src/main/java/ru/practicum/StatsServer.java
   * После этого запустите основное приложение: ewm-service/src/main/java/ru/practicum/EwmService.java

3) Проект работает по адресу:

http://localhost:8080/tasks

### API

Примеры использования программы можно увидеть в приложенных Postman тестах в директории: postman

* http://localhost:8080/tasks/task - POST - создать/обновить задачу
* http://localhost:8080/tasks/epic - POST - создать/обновить задачу-эпик

### Testing

* Unit-тесты: 
* Postman-тесты: postman

### Статус и планы по доработке проекта

На данный момент проект проверен и зачтен ревьюером. Планов по дальнейшему развитию проекта нет.
