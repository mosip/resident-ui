# AGENTS.md

## Repository Overview

`resident-ui` is the reference implementation of the Resident UI for MOSIP
(Modular Open Source Identity Platform). It is the web portal residents use
to view their identity data, download UIN/VID cards, raise service
requests, and manage consent/sharing with partners.

The repository holds two independent, separately-built projects plus
deployment assets:

- `resident-ui/` — the Angular web application (the actual product).
- `uitest-resident/` — a Maven/Java Selenium test-automation module that
  drives the deployed UI end-to-end.
- `helm/resident-ui/` — the Helm chart used to deploy the built UI image.
- `deploy/resident-ui/` and `deploy/resident-uitestrig/` — install/restart/
  delete shell scripts that wrap the Helm chart for cluster rollout.

Each of `resident-ui/` and `uitest-resident/` has its own build stack, its
own commands, and its own `AGENTS.md` with the specifics:

- [`resident-ui/AGENTS.md`](resident-ui/AGENTS.md) — Angular app: build,
  lint, unit tests, e2e, Docker image, runtime config.
- [`uitest-resident/AGENTS.md`](uitest-resident/AGENTS.md) — Maven-based
  Selenium test rig: build, configuration, execution, tags.

Read this root file for repo-wide conventions, then the relevant subfolder
file for the module you are changing.

## Technology Stack

| Area | Stack |
|---|---|
| UI app | Angular 7, TypeScript 3.2, Angular CLI 7.2, RxJS 6 |
| UI unit tests | Karma + Jasmine |
| UI e2e tests | Protractor |
| UI web server (container) | nginx |
| Test automation module | Java, Maven, Selenium WebDriver |
| Deployment | Docker, Helm 3, Kubernetes (via shell scripts in `deploy/`) |
| CI | GitHub Actions, reusable workflows from `mosip/kattu` |

There is a root-level `package-lock.json`, but it is an empty stub
(`"packages": {}`) — it is not a real Node project manifest. The real
Angular project and its dependencies live entirely under `resident-ui/`.

## Build & Test Commands

This repo has no root-level build — always `cd` into the relevant module
first. See the module `AGENTS.md` files for full command lists:

```bash
# Angular app
(cd resident-ui && npm install && npm run build && npm test && npm run lint)

# Selenium test rig
(cd uitest-resident && mvn clean install)
```

## Configuration

- Angular runtime config (API base URL, auth endpoints) is read from
  `resident-ui/src/assets/config.json` at runtime — it is not compiled
  into the JS bundle, so it can be swapped per environment without a
  rebuild. It contains only URLs/endpoint paths, no secrets.
- Angular build-time config (`production` flag) is in
  `resident-ui/src/environments/environment.ts` and
  `environment.prod.ts`, swapped by `--configuration production`
  (see `resident-ui/angular.json` `fileReplacements`).
- The i18n translation bundle is **not** shipped in the Docker image. The
  container's `CMD` (`resident-ui/Dockerfile`) downloads it at container
  start from the URL in the `resident_i18n_bundle_url_env` environment
  variable and unzips it into `assets/i18n` before starting nginx.
- Cluster secrets (Keycloak client secrets, OIDC client id, etc.) are
  never hand-edited into a values file. `deploy/resident-ui/install.sh`
  pulls them from existing Kubernetes `Secret`/`ConfigMap` objects via
  `deploy/copy_cm_func.sh` and injects them with `helm --set` / `kubectl
  create secret --from-literal` at install time. Do not tell contributors
  to paste secrets into `helm/resident-ui/values.yaml`.
- `uitest-resident` reads its own `Config.properties` and `TestData.json`
  — see `uitest-resident/AGENTS.md`.

## Project Structure Notes

```text
resident-ui/                    (repo root)
├── resident-ui/                Angular application source
│   ├── src/app/                 components, services, modules
│   ├── src/assets/              runtime config, i18n, static assets
│   ├── src/environments/        build-time environment flags
│   ├── e2e/                     Protractor e2e specs
│   └── Dockerfile
├── uitest-resident/             Selenium/Maven UI test automation
├── helm/resident-ui/            Helm chart for the UI deployment
├── deploy/resident-ui/          install.sh / restart.sh / delete.sh wrappers
├── deploy/resident-uitestrig/   install/delete for the test rig
└── .github/workflows/           CI (build, docker, sonar, codeql, release)
```

The two module directories at the repo root, `resident-ui/` (the Angular
app) and `uitest-resident/` (the Selenium test rig), are easy to confuse
— always check which directory you are in before running `npm` or `mvn`
commands.

## Development Workflow

1. Branch from `develop` (the active integration branch; release branches
   and `master` also exist but `develop` is where day-to-day work lands).
2. Make changes inside the correct module directory (`resident-ui/` or
   `uitest-resident/`).
3. Run the module's lint/test commands locally before pushing (see the
   module `AGENTS.md`).
4. Push to your fork and open a PR against `mosip/resident-ui`'s
   `develop` branch.

## Pull Request Guidelines

- Target `develop`, not `master` (`master` tracks released code).
- CI (`.github/workflows/push-trigger.yml`) runs automatically on PR
  `opened`/`reopened`/`synchronize`: it builds `resident-ui` via the
  shared `npm-build.yml` workflow and builds `uitest-resident` via Maven,
  then builds Docker images for both. Sonar analysis only runs on
  non-PR events (pushes to branches), not on the PR itself.
- Keep changes scoped to one module per PR where practical, since the
  two modules have unrelated stacks and reviewers.
- There is no `CONTRIBUTING.md` or PR template in this repo at the time
  of writing — follow the commit/PR conventions visible in recent merged
  PRs (issue/ticket reference in the title, e.g. `MOSIP-xxxxx` or
  `#<issue-number>`).

## Repository-Specific Considerations

- Angular 7 / TypeScript 3.2 is old. Do not silently bump major
  dependency versions (Angular, TypeScript, RxJS) as a side effect of an
  unrelated change — that is a separate, deliberate upgrade effort.
- The root `package-lock.json` stub and the real
  `resident-ui/package-lock.json` are different files. Only touch the
  one under `resident-ui/` when updating app dependencies.
- `.idea/`, `.project`, `.settings/`, `resident-ui.iml` are IDE metadata
  checked into the repo root; do not treat them as build configuration.

## Agent rules

### Do

1. Work inside the correct module directory (`resident-ui/` or
   `uitest-resident/`) and use that module's own `AGENTS.md` for exact
   commands.
2. Verify claims about build/CI/deploy behavior against the actual files
   (`.github/workflows/*`, `Dockerfile`, `deploy/*/install.sh`) before
   documenting or relying on them — they change over time.
3. Target the `develop` branch for PRs and branch-from.
4. Treat `resident-ui/src/assets/config.json` as the place for runtime
   endpoint config, and keep secrets out of any committed file.

### Do not

1. Do not run `npm` commands from the repo root — there is no real
   Node project there, only an empty `package-lock.json` stub.
2. Do not add secrets, tokens, or credentials to `helm/resident-ui/
   values.yaml` or any file under `deploy/` — the install scripts pull
   them from Kubernetes Secrets/ConfigMaps at deploy time.
3. Do not bump Angular/TypeScript/RxJS major versions as a drive-by
   change.
4. Do not assume the root `package-lock.json` is the app's real lockfile.
