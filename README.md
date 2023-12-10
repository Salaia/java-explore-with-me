# Explore with me
Template repository for ExploreWithMe project.

### Функционал программы:

В программе реализовано хранение трех типов задач:

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
