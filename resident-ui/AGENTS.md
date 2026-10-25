# AGENTS.md — resident-ui (Angular app)

Parent guide: [`../AGENTS.md`](../AGENTS.md)

## Purpose

The Angular 7 web application residents use to sign in, view their
identity, download UIN/VID documents, raise service requests, and manage
data-sharing consent with partners. This is the module that gets built
into the `resident-ui` Docker image.

## Layout

```text
resident-ui/
├── src/
│   ├── app/            components, services, routing, feature modules
│   ├── assets/         config.json (runtime config), i18n, fonts, images
│   ├── environments/   environment.ts / environment.prod.ts (build-time)
│   └── index.html, main.ts, polyfills.ts, styles.css
├── e2e/                 Protractor end-to-end specs
├── angular.json          Angular CLI project/build config
├── package.json          npm scripts and dependencies
├── Dockerfile             nginx-based runtime image
├── nginx.conf / default.conf   nginx server config used by the image
└── download.conf          headers used when serving downloadable files
```

## How to run

Install dependencies and start a dev server:

```bash
npm install
npm start
```

This runs `ng serve` and serves the app at `http://localhost:4200/` with
live reload (see the `start` script in `package.json`).

## Build & Test Commands

All commands run from this directory (`resident-ui/` from the repo root):

```bash
npm install         # install dependencies
npm run build        # ng build -> output in dist/resident-ui
npm test             # ng test -> Karma/Jasmine unit tests
npm run lint          # ng lint -> TSLint
npm run e2e           # ng e2e -> Protractor end-to-end tests
npm run sonar          # sonar-scanner (requires a configured Sonar server)
```

For a production build (used by CI/Docker), Angular CLI's `production`
configuration swaps in `environment.prod.ts` and enables optimization,
output hashing, and no source maps:

```bash
ng build --configuration production
```

## Configuration

- `src/assets/config.json` is the runtime config (API base URL, auth
  endpoints). It is fetched by the app at runtime, not baked into the JS
  bundle — the same built `dist/` can be pointed at different backends by
  swapping this file. It holds only URLs/paths, no secrets.
- `src/environments/environment.ts` (dev) and `environment.prod.ts`
  (prod) only carry a `production: boolean` flag today — there is no
  secret material here either.
- The i18n bundle is **not** part of the built `dist/` output. The
  Docker image's `CMD` downloads it at container start from the URL in
  the `resident_i18n_bundle_url_env` environment variable and unzips it
  into `assets/i18n` before nginx starts. If you add a new UI string,
  update the source i18n JSON that feeds that external bundle build, not
  a file checked into this repo.
- `nginx.conf` / `default.conf` configure the container's nginx server;
  `download.conf` sets `Content-Disposition: attachment` headers for
  file-download routes.

## Development Workflow

1. `cd resident-ui` (from repo root).
2. `npm install`.
3. Make your change under `src/app/...`.
4. Run `npm run lint` and `npm test` before committing.
5. If the change affects routed pages or user flows, also run
   `npm run e2e` (requires Chrome/Protractor webdriver set up locally).

## Agent rules — Do and Do not

### Do

1. Run `npm install` / `npm run build` / `npm test` / `npm run lint`
   from inside this `resident-ui/` directory, never the repo
   root.
2. Treat `src/assets/config.json` as the file to change for API
   endpoint/base URL config — not `environment.ts`.
3. Keep `environment.ts` / `environment.prod.ts` limited to build-time
   flags; do not put secrets or environment URLs there.
4. Update `e2e/` specs when you change routed page behavior they cover.

### Do not

1. Do not commit an i18n bundle or edit `assets/i18n` output directly —
   it is downloaded and unzipped at container runtime by the Dockerfile
   `CMD`, so in-repo edits to that generated directory would be
   overwritten.
2. Do not bump `@angular/*`, `typescript`, or `rxjs` versions as an
   incidental part of an unrelated fix.
3. Do not hardcode API URLs in TypeScript source — use
   `src/assets/config.json`.
