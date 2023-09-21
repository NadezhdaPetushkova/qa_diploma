# Дипломный проект по профессии «Тестировщик»

### Для выполнения работы необходимо:

1) Установить [Google Chrome](https://www.google.ru/chrome/);
2) Установить [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=windows);
3) Установить [Github](https://desktop.github.com/);
4) Установить [Docker Desktop](https://www.docker.com/).

### Запуск тестов 
1) Клонировать репозиторий командой `git clone https://github.com/NadezhdaPetushkova/qa_diploma`
2) Открыть проект в IntelliJ IDEA;
3) С помощью терминала создать контейнеры  `docker-compose up -d`;


#### Для проверки MySQL:
1) Запустить jar-файл с базой данных MySQL командой: `java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar`;

2) Убедиться в готовности системы. Приложение должно быть доступно по адресу: `http://localhost:8080/`;

3) В новой вкладке терминала запустить тесты командой: `./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"` ;

4) Для создания отчета запустить команду:`./gradlew allureServe`, `./gradlew allureReport`

#### Для проверки PostgreSQL:
1) В новой вкладке терминала запустить тестируемое приложение командой: `java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar`;

2) Убедиться в готовности системы. Приложение должно быть доступно по адресу: `http://localhost:8080/`;

3) В новой вкладке терминала запустить тесты командой: `./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`  ;

4) Для создания отчета запустить команду: `./gradlew allureServe`,`./gradlew allureReport`

 - Для остановки приложений использовать команду `Ctrl C`.
- Для удаления контейнеров использовать команду `docker-compose down`