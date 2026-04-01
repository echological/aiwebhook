# Quarkus Annotations Reference (Lengkap Praktis)

Dokumen ini adalah referensi lengkap-praktis annotation yang umum dipakai saat membangun aplikasi Quarkus.  
Catatan: ekosistem Quarkus sangat besar, jadi daftar ini fokus pada annotation inti + extension yang paling sering dipakai di production.

## 1) CDI / Dependency Injection (Arc)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Inject` | `jakarta.inject` | Inject dependency ke field/constructor/method |
| `@Named` | `jakarta.inject` | Bean name qualifier sederhana |
| `@Qualifier` | `jakarta.inject` | Membuat custom qualifier |
| `@Singleton` | `jakarta.inject` | Singleton bean |
| `@ApplicationScoped` | `jakarta.enterprise.context` | Satu instance per aplikasi |
| `@RequestScoped` | `jakarta.enterprise.context` | Satu instance per HTTP request |
| `@SessionScoped` | `jakarta.enterprise.context` | Scope session |
| `@Dependent` | `jakarta.enterprise.context` | Scope default CDI |
| `@Produces` | `jakarta.enterprise.inject` | Producer method/field bean |
| `@Disposes` | `jakarta.enterprise.inject` | Cleanup bean produced |
| `@Alternative` | `jakarta.enterprise.inject` | Bean alternatif |
| `@Priority` | `jakarta.annotation` | Prioritas interceptor/filter/alternative |
| `@PostConstruct` | `jakarta.annotation` | Hook setelah bean dibuat |
| `@PreDestroy` | `jakarta.annotation` | Hook sebelum bean dihancurkan |
| `@Observes` | `jakarta.enterprise.event` | Observer CDI event |
| `@ObservesAsync` | `jakarta.enterprise.event` | Observer async CDI event |

## 2) REST API (Jakarta REST, dipakai Quarkus REST)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Path` | `jakarta.ws.rs` | Mendefinisikan path endpoint |
| `@GET` / `@POST` / `@PUT` / `@DELETE` / `@PATCH` | `jakarta.ws.rs` | HTTP method mapping |
| `@Consumes` | `jakarta.ws.rs` | Tipe body request |
| `@Produces` | `jakarta.ws.rs` | Tipe response |
| `@PathParam` | `jakarta.ws.rs` | Ambil variable path |
| `@QueryParam` | `jakarta.ws.rs` | Ambil query param |
| `@HeaderParam` | `jakarta.ws.rs` | Ambil header |
| `@CookieParam` | `jakarta.ws.rs` | Ambil cookie |
| `@MatrixParam` | `jakarta.ws.rs` | Ambil matrix param |
| `@BeanParam` | `jakarta.ws.rs` | Gabung banyak param |
| `@DefaultValue` | `jakarta.ws.rs` | Default value param |
| `@FormParam` | `jakarta.ws.rs` | Ambil form field |
| `@Context` | `jakarta.ws.rs.core` | Inject context JAX-RS |
| `@Provider` | `jakarta.ws.rs.ext` | Registrasi provider |
| `@NameBinding` | `jakarta.ws.rs` | Binding filter/interceptor custom |
| `@RegisterRestClient` | `org.eclipse.microprofile.rest.client.inject` | Registrasi REST client interface |
| `@RestClient` | `org.eclipse.microprofile.rest.client.inject` | Inject REST client |

## 3) Validation (Bean Validation)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Valid` | `jakarta.validation` | Validasi object nested |
| `@NotNull` | `jakarta.validation.constraints` | Tidak boleh null |
| `@NotBlank` | `jakarta.validation.constraints` | String tidak boleh kosong/blank |
| `@NotEmpty` | `jakarta.validation.constraints` | Koleksi/string tidak kosong |
| `@Size` | `jakarta.validation.constraints` | Batas panjang/jumlah |
| `@Min` / `@Max` | `jakarta.validation.constraints` | Batas angka |
| `@Positive` / `@PositiveOrZero` | `jakarta.validation.constraints` | Angka positif |
| `@Negative` / `@NegativeOrZero` | `jakarta.validation.constraints` | Angka negatif |
| `@Pattern` | `jakarta.validation.constraints` | Regex validation |
| `@Email` | `jakarta.validation.constraints` | Format email |
| `@AssertTrue` / `@AssertFalse` | `jakarta.validation.constraints` | Validasi boolean |

## 4) Config (MicroProfile Config)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@ConfigProperty` | `org.eclipse.microprofile.config.inject` | Ambil config dari `application.*` |
| `@ConfigMapping` | `io.smallrye.config` | Mapping config ke interface typed |
| `@WithName` | `io.smallrye.config` | Alias nama property pada `@ConfigMapping` |
| `@WithDefault` | `io.smallrye.config` | Default value typed config |

## 5) Transaction (Narayana/JTA)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Transactional` | `jakarta.transaction` | Menjalankan method dalam transaksi |
| `@TransactionScoped` | `jakarta.transaction` | Scope bean per transaksi |

## 6) Scheduling

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Scheduled` | `io.quarkus.scheduler` | Menjalankan job terjadwal |
| `@Scheduled.ConcurrentExecution` | `io.quarkus.scheduler` | Atur perilaku concurrent |
| `@Scheduled.SkipPredicate` | `io.quarkus.scheduler` | Skip job berdasarkan kondisi |

## 7) Caching (Quarkus Cache)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@CacheResult` | `io.quarkus.cache` | Cache hasil method |
| `@CacheInvalidate` | `io.quarkus.cache` | Invalidate satu key |
| `@CacheInvalidateAll` | `io.quarkus.cache` | Invalidate semua key |
| `@CacheKey` | `io.quarkus.cache` | Tandai parameter sebagai key |

## 8) Fault Tolerance (MicroProfile FT)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Retry` | `org.eclipse.microprofile.faulttolerance` | Retry saat gagal |
| `@Timeout` | `org.eclipse.microprofile.faulttolerance` | Batas waktu eksekusi |
| `@CircuitBreaker` | `org.eclipse.microprofile.faulttolerance` | Membuka circuit saat sering gagal |
| `@Bulkhead` | `org.eclipse.microprofile.faulttolerance` | Batasi concurrency |
| `@Fallback` | `org.eclipse.microprofile.faulttolerance` | Method fallback |
| `@Asynchronous` | `org.eclipse.microprofile.faulttolerance` | Eksekusi async |

## 9) Security

| Annotation | Paket | Fungsi |
|---|---|---|
| `@RolesAllowed` | `jakarta.annotation.security` | Batasi endpoint untuk role tertentu |
| `@PermitAll` | `jakarta.annotation.security` | Semua role boleh akses |
| `@DenyAll` | `jakarta.annotation.security` | Tidak ada yang boleh akses |
| `@Authenticated` | `io.quarkus.security` | Harus login/authenticated |
| `@PermissionsAllowed` | `io.quarkus.security` | Kontrol akses berbasis permission |

## 10) OpenAPI / Swagger (SmallRye OpenAPI)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@OpenAPIDefinition` | `org.eclipse.microprofile.openapi.annotations` | Metadata global OpenAPI |
| `@Info` | `org.eclipse.microprofile.openapi.annotations.info` | Informasi API |
| `@Tag` | `org.eclipse.microprofile.openapi.annotations.tags` | Group endpoint |
| `@Operation` | `org.eclipse.microprofile.openapi.annotations` | Ringkasan endpoint |
| `@APIResponse` / `@APIResponses` | `org.eclipse.microprofile.openapi.annotations.responses` | Dokumentasi response |
| `@Parameter` | `org.eclipse.microprofile.openapi.annotations.parameters` | Dokumentasi parameter |
| `@RequestBody` | `org.eclipse.microprofile.openapi.annotations.parameters` | Dokumentasi body |
| `@Schema` | `org.eclipse.microprofile.openapi.annotations.media` | Dokumentasi model schema |
| `@SecurityScheme` / `@SecurityRequirement` | `org.eclipse.microprofile.openapi.annotations.security` | Dokumentasi security |

## 11) Health Check (SmallRye Health)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Liveness` | `org.eclipse.microprofile.health` | Health check liveness |
| `@Readiness` | `org.eclipse.microprofile.health` | Health check readiness |
| `@Startup` | `org.eclipse.microprofile.health` | Health check startup |

## 12) Metrics (Micrometer / MP Metrics)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Counted` | `org.eclipse.microprofile.metrics.annotation` | Hitung jumlah panggilan method |
| `@Timed` | `org.eclipse.microprofile.metrics.annotation` | Ukur durasi method |
| `@Gauge` | `org.eclipse.microprofile.metrics.annotation` | Nilai saat ini |
| `@Metric` | `org.eclipse.microprofile.metrics.annotation` | Inject metric |

## 13) Reactive Messaging (SmallRye Messaging)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Incoming` | `org.eclipse.microprofile.reactive.messaging` | Consumer channel |
| `@Outgoing` | `org.eclipse.microprofile.reactive.messaging` | Producer channel |
| `@Channel` | `org.eclipse.microprofile.reactive.messaging` | Inject channel |
| `@Broadcast` | `io.smallrye.reactive.messaging.annotations` | Broadcast ke banyak subscriber |
| `@Blocking` | `io.smallrye.reactive.messaging.annotations` | Jalankan di worker thread |
| `@Acknowledgment` | `org.eclipse.microprofile.reactive.messaging` | Strategy ack message |

## 14) Panache ORM (Hibernate / Mongo)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@PanacheEntity` | `io.quarkus.hibernate.orm.panache` | Entity aktif-record untuk ORM |
| `@PanacheEntityBase` | `io.quarkus.hibernate.orm.panache` | Base entity tanpa id default |
| `@MongoEntity` | `io.quarkus.mongodb.panache.common` | Mapping koleksi MongoDB |
| `PanacheMongoEntity` | `io.quarkus.mongodb.panache` | Base class Mongo active-record |
| `PanacheMongoEntityBase` | `io.quarkus.mongodb.panache` | Base Mongo tanpa id default |

## 15) Jakarta Persistence (JPA, jika pakai SQL)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@Entity` | `jakarta.persistence` | Menandai class entity |
| `@Table` | `jakarta.persistence` | Nama tabel |
| `@Id` | `jakarta.persistence` | Primary key |
| `@GeneratedValue` | `jakarta.persistence` | Auto-generate id |
| `@Column` | `jakarta.persistence` | Detail kolom |
| `@OneToOne` / `@OneToMany` / `@ManyToOne` / `@ManyToMany` | `jakarta.persistence` | Relasi entity |
| `@JoinColumn` | `jakarta.persistence` | FK column |
| `@Enumerated` | `jakarta.persistence` | Mapping enum |
| `@Lob` | `jakarta.persistence` | CLOB/BLOB |
| `@Version` | `jakarta.persistence` | Optimistic locking |

## 16) Test (Quarkus Testing)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@QuarkusTest` | `io.quarkus.test.junit` | Test dengan runtime Quarkus |
| `@QuarkusIntegrationTest` | `io.quarkus.test.junit` | Test packaged artifact |
| `@TestHTTPEndpoint` | `io.quarkus.test.common.http` | Base URL endpoint test |
| `@TestHTTPResource` | `io.quarkus.test.common.http` | Inject URL endpoint test |
| `@InjectMock` | `io.quarkus.test.junit.mockito` | Mock bean untuk test |
| `@InjectSpy` | `io.quarkus.test.junit.mockito` | Spy bean untuk test |
| `@TestProfile` | `io.quarkus.test.junit` | Pakai test profile custom |

## 17) Native Image / Reflection

| Annotation | Paket | Fungsi |
|---|---|---|
| `@RegisterForReflection` | `io.quarkus.runtime.annotations` | Registrasi class untuk reflection |
| `@RegisterForProxy` | `io.quarkus.runtime.annotations` | Registrasi interface proxy |

## 18) JSON (Jackson, sering dipakai di Quarkus REST)

| Annotation | Paket | Fungsi |
|---|---|---|
| `@JsonProperty` | `com.fasterxml.jackson.annotation` | Mapping nama field JSON |
| `@JsonIgnoreProperties` | `com.fasterxml.jackson.annotation` | Abaikan field tidak dikenal |
| `@JsonInclude` | `com.fasterxml.jackson.annotation` | Atur field yang diserialisasi |
| `@JsonIgnore` | `com.fasterxml.jackson.annotation` | Abaikan field/method |
| `@JsonFormat` | `com.fasterxml.jackson.annotation` | Format tanggal/angka |

## 19) Lombok (Bukan Quarkus, tapi umum dipakai)

| Annotation | Fungsi |
|---|---|
| `@Getter` / `@Setter` | Generate accessor |
| `@Data` | Getter+Setter+equals/hashCode+toString |
| `@Builder` | Builder pattern |
| `@NoArgsConstructor` / `@AllArgsConstructor` / `@RequiredArgsConstructor` | Generate constructor |
| `@Value` | Immutable class |
| `@Slf4j` | Logger otomatis |

## Contoh Kombinasi Annotation (Recommended)

```java
@Path("/api/telegram")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Telegram")
public class TelegramResource {

    @Inject
    TelegramService service;

    @POST
    @Operation(summary = "Receive Telegram webhook")
    @RolesAllowed("webhook")
    public Response receive(@Valid TelegramUpdateDto dto) {
        service.process(dto);
        return Response.ok().build();
    }
}
```

## Praktik yang Disarankan

- Gunakan scope default service sebagai `@ApplicationScoped`.
- Pakai `@Transactional` hanya di boundary write operation.
- Tambahkan `@Valid` di request body DTO.
- Gunakan `@Tag` dan `@Operation` agar Swagger selalu rapi.
- Gunakan `@RegisterForReflection` hanya jika benar-benar dibutuhkan.
- Untuk helper murni stateless, lebih baik class `final` + method `static` tanpa CDI annotation.

