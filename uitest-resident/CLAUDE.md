# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this project is

Selenium WebDriver-based UI automation test suite for the MOSIP Resident Portal. Tests cover end-to-end browser flows: login via OTP/VID, view history, manage VIDs, update data, share data, and more. Built as a fat JAR that runs against a live MOSIP environment.

## Commands

**Build the fat JAR:**
```
mvn clean install -DskipTests
```

**Run all tests (IDE mode):**
```
mvn exec:java -Dexec.mainClass="io.mosip.testrig.residentui.utility.TestRunner" -Dpath.config=/src/main/resources
```

**Run via JAR:**
```
java -jar target/uitest-resident-1.2.1-SNAPSHOT-jar-with-dependencies.jar
```

**Run a specific scenario subset** (via `residentscenariosToExecute` in `Kernel.properties`, or as an env var):
```
residentscenariosToExecute=logintest,getinformation,trackmyrequest
```

**Run with a specific environment user file** (e.g., `Kernel_dev.properties`):
```
-Denv.user=dev
```

**Run in Docker:**
```
docker build -t uitest-resident .
docker run -e residentPortalPath=https://... -e apiInternalEndPoint=https://... uitest-resident
```

## Configuration

All configuration lives in `src/main/resources/config/`:

- **`Kernel.properties`** — primary config: environment URLs, Keycloak credentials, MOSIP client secrets, test data (UIN, VIDs, emails), feature flags. For multi-env setups, create `Kernel_<name>.properties` and pass `-Denv.user=<name>`.
- **`application.properties`** — MOSIP API endpoint path fragments used by `BaseTestCase`.
- **`valueMapping.properties`** / **`bioValue.properties`** — label/value lookups.

`ConfigManager` resolves each key by checking `System.getenv(key)` first, then falling back to the properties file. All environment URLs and secrets can be overridden via env vars in Docker.

Key properties to set before running:
```
residentPortalPath=https://resident.<env>.mosip.net/
apiInternalEndPoint=https://api-internal.<env>.mosip.net
keycloak-external-url=https://iam.<env>.mosip.net
loginlang=eng          # or sin, hin, etc.
identityGenManual=no   # set to yes and fill uin/perpetualvid/etc. to skip auto-generation
externalemail=...      # email used to receive OTP
headless=yes           # yes for CI/Docker, no for local debugging
docker=yes             # yes uses /usr/bin/chromedriver, no uses WebDriverManager
```

## Architecture

### Execution flow

1. `TestRunner.main()` — entry point; calls `AdminTestUtil.initialize()`, starts `MockSMTPListener` (listens on SMTP for OTPs), generates a UIN + VIDs via MOSIP APIs (unless `identityGenManual=yes`), then invokes TestNG.
2. **TestNG suite** — defined in `src/main/resources/testngFile/testng.xml`; all test classes run under one suite. When `residentscenariosToExecute` is non-empty, `TestRunner` builds the suite programmatically.
3. Each test class extends `BaseClass`, which handles `@BeforeMethod` (launch Chrome, navigate to portal, select language) and `@AfterMethod` (quit driver).

### Key classes

| Class | Role |
|---|---|
| `TestRunner` | Main entry; UIN/VID generation; TestNG wiring |
| `AdminTestUtil` | REST API helpers: generate UIN, generate VID, activate UIN, health checks |
| `BaseTestCase` | Static config/auth state shared across all tests; `initialize()` must be called first |
| `BaseClass` | WebDriver lifecycle per test method; holds the static `driver` instance |
| `ConfigManager` | All config reads; env-var-first resolution; must call `ConfigManager.init()` before use |
| `Commons` | Selenium helpers: `click`, `enter`, `assertCheck`, `isDisplayed`, `wait` — all with built-in waits and screenshot-on-failure |
| `MockSMTPListener` | Embedded SMTP server that captures OTP emails; `getOtp(email)` polls until OTP arrives |
| `KernelAuthentication` | Obtains auth tokens from Keycloak by role for API calls |

### Test classes

Each test class in `io.mosip.testrig.residentui.testcase` maps to a Resident Portal feature:
`LoginTest`, `GetInformation`, `GetPersonalisedCard`, `GetMyUIN`, `ManageMyVid`, `SecureMyId`, `ShareMyData`, `TrackMyRequests`, `UpdateMyData`, `VerifyPhoneNumberEmailID`, `ViewMyHistory`, `BookinganAppointment`

All tests depend on `LoginTest.loginTest()` having run first to establish the browser session (static `driver` is shared within a test method lifecycle, re-initialized each `@BeforeMethod`).

### OTP handling

`MockSMTPListener` is an embedded SMTP server. The MOSIP environment must be configured to route OTP emails to this listener's host/port. `MockSMTPListener.getOtp(email)` polls with a timeout defined by `OTPTimeOut` in `Kernel.properties` (default 181 seconds).

### Reports

TestNG produces `testng-report/` output. The `EmailableReport` listener generates an HTML report named `RESIDENT-<env>-<lang>-run-<timestamp>-report.html`. Optionally pushed to S3/MinIO when `push-reports-to-s3=yes`.