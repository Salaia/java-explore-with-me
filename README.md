# Explore with me
Платформа для публикации событий и поиска товарищей для посещения мероприятий.

<div>
      <img src="assets/landing.png" title="EWM landing" alt="explore with me landing"/>
</div>


## Функционал программы:

Explore with me - микросервисное приложение. В программу входят два микросервиса:
* main-service - основная часть приложения, содержащая бизнес-логику
* stats-service - сервис для сбора статистики просмотра событий по ip

Микросервисная архитектура Explore with me выглядит следующим образом:

<div>
      <img src="assets/micro-services architecture.png" title="microservice architecture" alt="microservice architecture"/>
</div>

Доступная функциональность зависит от уровней доступа пользователей:

### Неавторизованные пользователи
* просматривать все события, в том числе по категориям
* видеть детали отдельных событий
* видеть закрепленные подборки событий 

### Авторизованные пользователи
* добавление в приложение новые мероприятия, редактировать их и просматривать после добавления
* подача заявок на участие в интересующих мероприятиях
* создатель мероприятия может подтверждать заявки, которые отправили другие пользователи сервиса

### Администраторы
* добавление, изменение и удаление категорий для событий
* добавление, удаление и закрепление на главной странице подборки мероприятий
* модерация событий, размещённых пользователями, — публикация или отклонение
* управление пользователями — добавление, активация, просмотр и удаление

## API

Примеры использования программы можно увидеть в приложенных Postman тестах в директории: postman

### main-service

* POST /users/{userId}/events - добавить новое событие
* GET /users/{userId}/events/{eventId} - получить событие
* PATCH /users/{userId}/events/{eventId} - изменить событие
* GET /users/{userId}/events - получить события пользователя
* GET /users/{userId}/events/{eventId}/requests - получить запросы пользователя на участие в событии
* PATCH /users/{userId}/events/{eventId}/requests - изменить статус (подтверждение, отмена) заявок на участие пользователя в событии

* GET /categories - получить все категории
* GET /categories/{catId} - получить категорию

* GET /compilations - получить все подборки событий
* GET /compilations/{compId} - получить подборку событий

* GET /admin/events - получить события по любым параметрам:
  * users - список id пользователей
  * states - список статусов события (PENDING, PUBLISHED, CANCELED)
  * categories - список id категорий событий
  * rangeStart - начало временного отрезка в формате yyyy-MM-dd HH:mm:ss
  * rangeEnd - конец временного отрезка в формате yyyy-MM-dd HH:mm:ss
  * from - параметр для пагинации
  * size - параметр для пагинации
* PATCH /admin/events/{eventId} - изменить событие

* GET /events - получить события по любым параметрам:
  * text - текст для поиска в названии и описании событий
  * categories - список id категорий событий
  * paid - только платные события (true/false)
  * rangeStart - начало временного отрезка в формате yyyy-MM-dd HH:mm:ss
  * rangeEnd - конец временного отрезка в формате yyyy-MM-dd HH:mm:ss
  * onlyAvailable - только доступные события, т.е. у которых еще не исчерпан лимит участников (true/false)
  * sort - способ сортировки событий (EVENT_DATE, VIEWS)
  * from - параметр для пагинации
  * size - параметр для пагинации
* GET /events/{id} - получить событие

* POST /users/{userId}/requests - добавить запрос на участие в событии
* GET /users/{userId}/requests - получить запросы пользователя на участие в событиях
* DELETE /users/{userId}/requests/{requestId}/cancel - отменить запрос на участие в событии

* POST /users/{userId}/events/{eventId}/comments - добавить комментарий к событию
* PATCH /users/{userId}/events/{eventId}/comments/{commentId} - обновить комментарий
* GET /users/{userId}/events/{eventId}/comments/{commentId} - получить комментарий к событию
* DELETE /users/{userId}/events/{eventId}/comments/{commentId} - удалить комментарий к событию
* GET /users/{userId}/events/{eventId}/comments - получить список комментариев пользователя к событию
* GET /users/{userId}/comments - получить все комментарии пользователя

* POST /admin/users - добавить пользователя
* GET /admin/users - получить всех пользователей
* DELETE /admin/users/{userId} - удалить пользователя
* POST /admin/compilations - добавить подборку событий
* DELETE /admin/compilations/{compId} - удалить подборку событий
* PATCH /admin/compilations/{compId} - обновить подборку событий
* POST /admin/categories - добавить новую категорию
* GET /admin/categories/{catId} - получить категорию событий
* DELETE /admin/categories/{catId} - удалить категорию
* GET /admin/comments - получить комментрии по любым параметрам:
  * text - текст для поиска в содержании комментария
  * users - список id пользователей
  * events - список id событий
  * statuses - статусы событий (PENDING, PUBLISHED, DELETED)
  * rangeStart - начало временного отрезка в формате yyyy-MM-dd HH:mm:ss
  * rangeEnd - конец временного отрезка в формате yyyy-MM-dd HH:mm:ss
  * from - параметр для пагинации
  * size - параметр для пагинации
* PATCH /admin/comments - изменить статусы комментариев

### stats-service

* GET /stats - Получение статистики по посещениям
* POST /hit - Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем

## Testing

* Unit-тесты: 
* Postman-тесты: postman

## 🛠 Tech & Tools

<div>
      <img src="https://github.com/Salaia/icons/blob/main/green/Java.png?raw=true" title="Java" alt="Java" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/SPRING%20boot.png?raw=true" title="Spring Boot" alt="Spring Boot" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/SPRING%20MVC.png?raw=true" title="Spring MVC" alt="Spring MVC" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/Maven.png?raw=true" title="Apache Maven" alt="Apache Maven" height="40"/>
</div>

Java, Spring (Boot, MVC), Maven, REST API, Microservice, Hibernate, Mockito, Lombok, JUnit, PostgreSQL, Postman, Docker

## Инструкция по развёртыванию ▶️

У каждого микросервиса есть своя база данных.
Микросервисы и базы данных запускаются в собственных Docker контейнерах (4 шт).

1) Склонируйте репозиторий и перейдите в него
   https://github.com/Salaia/java-explore-with-me.git

2) Запустите проект в выбранной IDE:
   * Сначала нужно запустить сервис статистики: stats-server/stats-service/src/main/java/ru/practicum/StatsServer.java
   * После этого запустите основное приложение: ewm-service/src/main/java/ru/practicum/EwmService.java

3) Проект работает по адресу:

http://localhost:8080/tasks

## Статус и планы по доработке проекта

На данный момент проект проверен и зачтен ревьюером. Планов по дальнейшему развитию проекта нет.
