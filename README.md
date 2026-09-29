# Hotel Booking API – Test Automation (Java, Rest-Assured, Cucumber)

Automated API tests for the booking and authentication endpoints of the hotel booking application at https://automationintesting.online.

The tests are written in business language (Gherkin), so every scenario reads as a rule of the hotel's booking process and every step calls the API through a small, reusable client layer.

## Tech stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 21 | Language (records, text blocks, sequenced collections) |
| Maven | 3.9+ | Build and dependency management |
| Rest-Assured | 5.5.2 | HTTP requests and response checks |
| Cucumber | 7.22.2 | BDD scenarios in Gherkin |
| JUnit Platform Suite | 5.12.2 | Runs Cucumber through Maven Surefire |
| PicoContainer | via Cucumber | Shares one scenario context between step classes (dependency injection) |
| Jackson | 2.21.1 | JSON to Java objects |
| Lombok | 1.18.40 | Removes boilerplate (constructors, getters, builders) |

## Project structure

```
src/test/java/com/booking
├── TestRunner.java              Cucumber suite, report plugins
├── api                          How to talk to the API
│   ├── Endpoints                All endpoint paths in one place
│   ├── RequestSpecFactory       Shared base URL, JSON settings and logging
│   ├── AuthClient               Login
│   └── BookingClient            Create, view, update, partial update, cancel
├── config/ApiConfig             Reads config.properties (can be overridden with -D)
├── context/ScenarioContext      State shared by the steps of one scenario
├── data                         Test data
│   ├── BookingDataFactory       Valid bookings with random future dates
│   └── BookingField             Business field names mapped to the booking model
├── model                        Booking and BookingDates as Java records
├── stepdefinitions              One class per feature + Hooks + ParameterTypes
└── support/BookingAssertions    Reusable check that two bookings match

src/test/resources
├── features                     Gherkin feature files
├── spec/booking.yaml            Swagger spec used as the reference
├── config.properties            Base URL and credentials
└── junit-platform.properties    Default tag filter and naming
```

### Design choices
- **API client (service object) pattern:** step definitions never build HTTP requests. They call `AuthClient` and `BookingClient`, so a change in the API is fixed in one place.
- **Factory pattern:** `RequestSpecFactory` builds the shared request setup and `BookingDataFactory` builds test data.
- **Dependency injection with PicoContainer:** clients and `ScenarioContext` are injected through constructors, so no static state is shared between scenarios.
- **Hooks:** `@Before` logs in as administrator for booking scenarios. `@After` deletes every booking the scenario created and then clears the token, so tests leave no data behind on the shared API.
- **Independent tests:** every booking uses random future dates, so scenarios can run in any order without clashing.

## Test coverage

43 scenarios: 36 in the regular run and 7 documenting known bugs.

| Feature | Endpoint | Scenarios | What is checked |
|---|---|---|---|
| Admin authentication | `POST /auth/login` | 5 | Valid login returns a token; wrong password, unknown user and wrong-case credentials are refused |
| Create a room booking | `POST /booking` | 21 | Valid booking and returned details; boundary values for first name, last name and phone; invalid email formats; missing fields; double booking; check-out before check-in |
| View a booking | `GET /booking/{id}` | 4 | Details match the booking; no login and invalid token are denied; unknown id is not found |
| Change a booking | `PUT` / `PATCH /booking/{id}` | 8 | Move to new dates and verify; denied without login; invalid values rejected; partial update |
| Cancel a booking | `DELETE /booking/{id}` | 4 | Cancelled booking can no longer be found; no login and invalid token are denied; unknown id |
| Booking journey | all | 1 | End to end: log in, book, view, move to new dates, cancel, confirm it is gone |

Techniques used: boundary value analysis, equivalence partitioning, positive and negative tests, security (authorisation) tests, and an end-to-end journey.

Cucumber features used: Background, Scenario Outline with several named and tagged Examples tables, tags, tagged hooks, a custom parameter type (`{bookingField}`), and business-language steps shared across features.

## Prerequisites
- JDK 21
- Maven 3.9 or later (or the Maven bundled with IntelliJ IDEA)
- Internet access to https://automationintesting.online

## How to run

Run the regular suite (known bugs excluded):
```
mvn clean test
```

Run a subset by tag (in PowerShell, put the argument in quotes):
```
mvn clean test "-Dcucumber.filter.tags=@smoke"
mvn clean test "-Dcucumber.filter.tags=@booking and @negative"
```

Reproduce the known bugs (these scenarios are expected to fail):
```
mvn clean test "-Dcucumber.filter.tags=@known-bug"
```

Use another environment or user without changing the code:
```
mvn clean test "-Dbase.url=https://other-host/api" "-Dauth.username=admin" "-Dauth.password=secret"
```

### Tags
| Tag | Meaning |
|---|---|
| `@auth`, `@booking` | Area under test |
| `@create`, `@view`, `@update`, `@delete`, `@e2e` | Feature |
| `@smoke` | Main happy paths |
| `@positive`, `@negative` | Expected outcome |
| `@validation`, `@security` | Type of check |
| `@known-bug` | Describes the correct behaviour; excluded from the regular run |

## Reports
- After every run: `target/cucumber-reports.html` (HTML) and `target/cucumber.json`.
- A copy of the latest results is committed in [`reports/`](reports):
    - [`cucumber-report.html`](reports/cucumber-report.html): regular run, 36 passed, 7 skipped
    - [`known-bugs-report.html`](reports/known-bugs-report.html): the 7 `@known-bug` scenarios failing, with the actual API responses

View them online (GitHub Pages):
- [Full run, all 43 scenarios including known bugs](https://subha18mohan.github.io/Subha_API_Testing_KATA/reports/full-test-report.html)
- [Regular run report](https://subha18mohan.github.io/Subha_API_Testing_KATA/reports/cucumber-report.html)
- [Known bugs report](https://subha18mohan.github.io/Subha_API_Testing_KATA/reports/known-bugs-report.html)

GitHub shows HTML files as source code, so use the links above or download the file and open it in a browser.

## Bugs and observations
See [BUG_REPORT.md](BUG_REPORT.md) for 5 bugs and 8 differences between the API and its Swagger specification. Summary:

- `PATCH /booking/{id}` is documented but returns 405.
- A booking cannot be updated without changing its dates (409).
- An invalid update returns internal server details in the error message.
- Check-out before check-in returns 409 instead of 400.
- Email and phone number are not required, although the spec says they are.

Where the API works but differs from the spec (for example 403 instead of 401, 202 instead of 201), the tests accept both values and the difference is recorded as an observation.

## Commit convention
Commits follow [Conventional Commits](https://www.conventionalcommits.org): `feat` (framework code), `test` (scenarios), `refactor`, `build` (Maven), `docs`. The message body explains why the change was made.