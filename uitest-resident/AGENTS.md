# AGENTS.md — uitest-resident (Selenium UI automation)

Parent guide: [`../AGENTS.md`](../AGENTS.md)

## Purpose

A Maven-based Selenium WebDriver test module ("Admin Automation") that
drives a deployed Resident UI instance through Chrome to cover CRUD
(create/read/update/delete) flows end-to-end against a real environment.
This is not a unit-test module — it needs a running target environment
and a Chrome driver.

## Layout

```text
uitest-resident/
├── pom.xml            Maven build (Java 21, TestNG, REST Assured, Guava)
├── src/                test source, page objects, resources
├── Dockerfile          container image for CI/scheduled runs
├── entrypoint.sh        container entrypoint
└── README.md            usage notes (source of the commands below)
```

## Build & Test Commands

Run from this directory:

```bash
mvn clean install
```

This produces a jar (per `pom.xml`, artifact name pattern
`uitest-resident-<version>-jar-with-dependencies`). Per the module
README, place the built jar in a folder alongside `src/main/resources`
files, then run it with system properties **before** `-jar`:

```bash
java -Dpath="https://env.mosip.net/" -Duserid=user -Dpassword=pwd \
  -jar uitest-resident-1.2.1-SNAPSHOT-jar-with-dependencies.jar
```

A `chromedriver` binary must be present under the working directory in a
folder named `chromedriver`.

## Configuration

- `Config.properties` — keys include `langcode` (login page language,
  described in `TestData.json`) and `bulkwait` (bulk-upload wait time in
  ms).
- `TestData.json` — `setExcludedGroups` controls which scenario tags are
  skipped. Leave empty (`""`) to run everything, or list tag codes (e.g.
  `"BL,CT"`) to exclude those groups. Tag codes map to scenario suites:
  `BL` blocklisted words, `BU` bulk upload, `CTR` center, `CT` center
  type, `DS` device spec, `D` device, `DT` device types, `DOC` document
  categories, `DOCT` document types, `DF` dynamic field, `H` holidays,
  `MS` machine spec, `M` machine, `MT` machine types, `T` template.
- Credentials (`userid`/`password`) and the target `path` are passed as
  JVM `-D` system properties at run time, never committed to
  `Config.properties` or `TestData.json`.

## Execution results

- Failures: `logs/AutomationLogs.log`.
- Results: `test-output/emailable-report.html`.

## CI

`.github/workflows/push-trigger.yml` builds this module with Maven
(`build-maven-uitest-resident` job, `mosip/kattu` reusable
`maven-build.yml@master-java21` workflow), then packages the executable
jars and builds a local Docker image
(`build-uitest-resident-local` / `build-docker-uitest-resident` jobs).
Sonar analysis for this module also runs via a reusable `kattu` workflow,
only on non-PR events.

## Agent rules — Do and Do not

### Do

1. Run `mvn clean install` from inside `uitest-resident/`, not the repo
   root.
2. Pass credentials and the target environment URL as `-D` JVM system
   properties at run time, placed before `-jar` on the command line.
3. Use `setExcludedGroups` in `TestData.json` to scope which scenario
   tags run, rather than deleting or commenting out test classes.

### Do not

1. Do not commit real credentials into `Config.properties` or
   `TestData.json`.
2. Do not point these tests at a production MOSIP environment without
   confirming with the environment owner — these are CRUD tests that
   create/modify/delete real records.
3. Do not confuse this module's Java 21 / Maven toolchain with the
   Angular app's Node/npm toolchain in `../resident-ui/`.
