## Report Portal Launches Automation

Автотесты для раздела `Launches` в [ReportPortal Demo](https://demo.reportportal.io/ui/), реализованы на Java + TestNG + Selenide + RestAssured с разделением по слоям (POM + API client + steps + tests).

## Стек

- Java 17
- Maven
- TestNG
- Selenide
- RestAssured
- Log4j2

## Важная логика API (UUID vs ID)

Для demo-инстанса ReportPortal используется разный тип идентификаторов:

- `POST /launch` возвращает `uuid` (в поле `id`)
- `GET /launch/uuid/{uuid}` возвращает numeric `id`
- `PUT /launch/{id}/update` и `DELETE /launch/{id}` работают с numeric `id`
- `PUT /launch/{uuid}/finish` требует `uuid`

Из-за этого в `LaunchesApiSteps` реализовано сопоставление `id -> uuid`.

## Реализованные проверки

### API (`tests.api.LaunchesCrudApiTest`)

1. `createLaunchTest` — создание прогона, finish, delete
2. `readLaunchTest` — создание и получение по ID
3. `updateLaunchTest` — обновление description/attributes
4. `deleteLaunchTest` — удаление прогона
5. `getLaunchesWithInvalidTokenTest` — негативный токен (`401`)
6. `getLaunchesWithInvalidProjectNameTest` — негативный project (`403/404` в зависимости от окружения)

### UI (`tests.ui.LaunchesTest`)

1. `latestLaunchesTabShouldShowLatestLaunchFromApi`
2. `launchAttributeShouldBeVisible`
3. `launchesSearchShouldFindPreparedLaunch`
4. `shouldAddWidgetOnDashboard`

Для минимизации флаки UI-предусловия готовятся через API.

## Конфигурация секретов

Для работы тестов, в файле .env нужно указать:
- Токен
- Логин
- Пароль

## Параллельный запуск

По умолчанию используется `src/test/resources/testng.xml` (последовательный прогон).

Для параллельного запуска добавлен отдельный suite:

- `src/test/resources/testng.parallel.xml`
  - `parallel="tests"`
  - `thread-count="2"`

Запуск параллельного suite:

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng.parallel.xml
```