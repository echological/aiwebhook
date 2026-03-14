# Dokumentasi Annotation Quarkus

Dokumen ini merangkum annotation yang umum dipakai di project `ai-webhook`, beserta fungsi dan contoh penggunaannya.

## 1) REST Endpoint (Jakarta REST)

| Annotation | Fungsi | Contoh |
|---|---|---|
| `@Path` | Menentukan path endpoint pada class/method | `@Path("/api")` |
| `@GET` | HTTP method GET | `@GET` |
| `@POST` | HTTP method POST | `@POST` |
| `@PUT` | HTTP method PUT | `@PUT` |
| `@DELETE` | HTTP method DELETE | `@DELETE` |
| `@Produces` | Format response | `@Produces(MediaType.APPLICATION_JSON)` |
| `@Consumes` | Format request body | `@Consumes(MediaType.APPLICATION_JSON)` |

Contoh:

```java
@Path("/webhook")
@Produces(MediaType.APPLICATION_JSON)
public class WebhookResource {
    @GET
    @Path("/telegram")
    public Response telegram() {
        return Response.ok().build();
    }
}
```

## 2) Dependency Injection (CDI)

| Annotation | Fungsi | Contoh |
|---|---|---|
| `@ApplicationScoped` | Bean singleton selama lifecycle aplikasi | `@ApplicationScoped` |
| `@Inject` | Inject bean/dependency | `@Inject AppConfig appConfig;` |

Contoh:

```java
@ApplicationScoped
public class SomeService {
}

public class SomeResource {
    @Inject
    SomeService someService;
}
```

## 3) Konfigurasi Aplikasi

| Annotation | Fungsi | Contoh |
|---|---|---|
| `@ConfigProperty` | Ambil nilai konfigurasi dari `application.yaml/properties` | `@ConfigProperty(name = "app.env")` |

Contoh:

```java
@ApplicationScoped
public class AppConfig {
    @ConfigProperty(name = "app.name", defaultValue = "AI Webhook")
    String appName;
}
```

## 4) OpenAPI / Swagger

| Annotation | Fungsi | Contoh |
|---|---|---|
| `@Tag` | Mengelompokkan endpoint di Swagger UI | `@Tag(name = "API")` |
| `@Operation` | Memberi ringkasan operasi endpoint | `@Operation(summary = "App info")` |

Contoh:

```java
@Path("/api")
@Tag(name = "API", description = "General API endpoints")
public class HealthResource {
    @GET
    @Path("/info")
    @Operation(summary = "App info")
    public Response info() {
        return Response.ok().build();
    }
}
```

## 5) Exception Handling

| Annotation | Fungsi | Contoh |
|---|---|---|
| `@Provider` | Mendaftarkan class sebagai JAX-RS provider (mis. `ExceptionMapper`) | `@Provider` |

Contoh:

```java
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        return Response.serverError().build();
    }
}
```

## 6) MongoDB (Quarkus Panache)

| Komponen | Fungsi | Contoh |
|---|---|---|
| `@MongoEntity` | Menentukan koleksi/database untuk entity MongoDB | `@MongoEntity(collection = "webhook_events")` |
| `PanacheMongoEntity` | Base class entity MongoDB dengan method helper Panache | `class Event extends PanacheMongoEntity` |

Contoh:

```java
@MongoEntity(collection = "webhook_events")
public class WebhookEvent extends PanacheMongoEntity {
    public String type;
    public String source;
}
```

## 7) JSON Mapping (Jackson)

| Annotation | Fungsi | Contoh |
|---|---|---|
| `@JsonInclude` | Menentukan field mana yang diserialisasi | `@JsonInclude(JsonInclude.Include.NON_NULL)` |
| `@JsonIgnoreProperties` | Abaikan field JSON yang tidak dikenal | `@JsonIgnoreProperties(ignoreUnknown = true)` |
| `@JsonProperty` | Mapping nama field JSON ke field Java | `@JsonProperty("update_id")` |

## 8) Testing Quarkus

| Annotation | Fungsi | Contoh |
|---|---|---|
| `@QuarkusTest` | Menjalankan test dengan runtime Quarkus | `@QuarkusTest` |
| `@QuarkusIntegrationTest` | Menjalankan integration test pada packaged mode | `@QuarkusIntegrationTest` |

## 9) Lombok (Boilerplate Reduction)

| Annotation | Fungsi | Catatan |
|---|---|---|
| `@Getter` / `@Setter` | Generate getter/setter otomatis | Cocok untuk class config/model mutable |
| `@Data` | Generate getter/setter + `toString` + `equals/hashCode` | Hati-hati untuk entity besar (efek ke `equals`) |
| `@Builder` | Builder pattern | Bagus untuk DTO request/response |
| `@NoArgsConstructor` | Constructor kosong | Sering dibutuhkan serializer/deserializer |
| `@AllArgsConstructor` | Constructor semua field | Mempercepat instansiasi object |

Contoh:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
}
```

## Praktik yang Disarankan

- Gunakan annotation sekecil mungkin sesuai kebutuhan (hindari over-annotation).
- Untuk endpoint, selalu deklarasikan `@Produces` dan `@Consumes` agar eksplisit.
- Untuk config, selalu beri `defaultValue` jika memungkinkan.
- Untuk Lombok:
  - DTO: boleh `@Data`/`@Builder`.
  - Exception/Config: lebih aman `@Getter` saja.
  - Entity database: hindari `@Data` jika tidak perlu.

