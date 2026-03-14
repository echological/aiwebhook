# AI Webhook

Boilerplate Quarkus untuk development cepat - webhook receiver dengan API standar.

## Quick Start (Dev Mode)

```bash
./mvnw quarkus:dev
```

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/q/swagger-ui
- **Dev UI**: http://localhost:8080/q/dev
- **Health**: http://localhost:8080/q/health

## Endpoints

| Endpoint | Method | Deskripsi |
|----------|--------|-----------|
| `/api/info` | GET | App info (name, version, env) |
| `/api/webhook/health` | GET | Webhook health check |
| `/api/webhook/receive` | POST | Terima webhook payload |
| `/hello` | GET | Ping test |

## MongoDB

- **Dev**: Dev Services auto-start MongoDB (Docker/Podman required)
- **Prod**: Set `MONGODB_URI` env atau `quarkus.mongodb.connection-string` di `application-prod.yaml`
- **Database**: `aiwebhook`, collection: `webhook_events`

## Boilerplate Structure

```
org.techsolution.webhook/
├── api/
│   ├── dto/ApiResponse.java      # Response wrapper
│   ├── exception/                # ApiException, ExceptionMappers
│   └── HealthResource.java       # /api/info
├── config/AppConfig.java         # Centralized config
└── webhook/
    ├── WebhookEvent.java         # MongoDB entity (Panache)
    ├── WebhookPayload.java       # DTO webhook
    └── WebhookResource.java      # Webhook endpoints
```

## Response Format

```json
{"success": true, "data": {...}, "message": null, "errorCode": null}
```

## Profiles

- `dev` (default): verbose logging, hot reload
- `test`: untuk unit/integration test
- `prod`: production config

---

This project uses Quarkus. Visit <https://quarkus.io/> for more.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
