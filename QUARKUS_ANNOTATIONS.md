# Quarkus Annotations Reference (Komprehensif)

Dokumen ini adalah referensi komprehensif annotation yang umum dipakai di ekosistem Quarkus (core + extension populer).
Catatan: daftar ini sangat luas, tapi tetap bukan 100% seluruh annotation dari semua extension pihak ketiga.

## 1) CDI / Dependency Injection (jakarta.enterprise + jakarta.inject)

| Annotation | Fungsi singkat |
|---|---|
| `@Inject` | Inject dependency |
| `@ApplicationScoped` | Bean singleton level aplikasi (umum untuk service/facade/adapter) |
| `@Singleton` | Singleton scope |
| `@Dependent` | Default scope CDI (instance mengikuti titik inject) |
| `@RequestScoped` | Scope per request |
| `@SessionScoped` | Scope per session |
| `@Named` | Beri nama bean untuk disambiguasi |
| `@Qualifier` | Membuat custom qualifier |
| `@Produces` | Producer method/field CDI |
| `@Disposes` | Dispose object dari producer |
| `@Alternative` | Implementasi alternatif untuk injection |
| `@Priority` | Prioritas interceptor/alternative |
| `@PostConstruct` | Hook setelah bean diinisialisasi |
| `@PreDestroy` | Hook sebelum bean dihancurkan |
| `@Observes` | Observer event sinkron |
| `@ObservesAsync` | Observer event asynchronous |
| `@Startup` (Quarkus) | Paksa bean startup saat boot |

## 2) REST API (jakarta.ws.rs)

| Annotation | Fungsi singkat |
|---|---|
| `@Path` | Mapping path class/method |
| `@GET` `@POST` `@PUT` `@DELETE` `@PATCH` `@HEAD` `@OPTIONS` | HTTP method |
| `@Produces` | Media type response |
| `@Consumes` | Media type request |
| `@PathParam` | Ambil path variable |
| `@QueryParam` | Ambil query param |
| `@HeaderParam` | Ambil header request |
| `@CookieParam` | Ambil cookie |
| `@FormParam` | Ambil form value |
| `@BeanParam` | Gabung param object |
| `@DefaultValue` | Nilai default parameter |
| `@Context` | Akses context object JAX-RS |
| `@Provider` | Daftarkan provider (`ExceptionMapper`, filter, dll) |
| `@ApplicationPath` | Base path untuk aplikasi JAX-RS |
| `@MatrixParam` | Ambil matrix parameter |
| `@Encoded` | Disable URL decoding |
| `@Suspended` | Async response (`AsyncResponse`) |

## 3) REST Client (MicroProfile Rest Client)

| Annotation | Fungsi singkat |
|---|---|
| `@RegisterRestClient` | Registrasi interface sebagai REST client |
| `@RestClient` | Inject REST client |
| `@RegisterProvider` | Daftarkan provider per client |
| `@ClientHeaderParam` | Tambah header statis/dinamis |
| `@ClientExceptionMapper` (Quarkus) | Mapping error response client |

## 4) Konfigurasi (MicroProfile Config + SmallRye Config)

| Annotation | Fungsi singkat |
|---|---|
| `@ConfigProperty` | Inject config by key |
| `@ConfigProperties` | Bind grup property ke class (legacy MP style) |
| `@ConfigMapping` (SmallRye) | Bind strongly-typed interface dari prefix config |
| `@WithName` | Override nama key di `@ConfigMapping` |
| `@WithDefault` | Default value di `@ConfigMapping` |
| `@WithParentName` | Gunakan nama parent key |
| `@WithConverter` | Gunakan converter custom |

## 5) Validasi (jakarta.validation)

| Annotation | Fungsi singkat |
|---|---|
| `@Valid` | Trigger validasi object nested |
| `@NotNull` | Harus tidak null |
| `@NotBlank` | String tidak boleh blank |
| `@NotEmpty` | Collection/String tidak boleh kosong |
| `@Size` | Batas panjang/ukuran |
| `@Min` `@Max` | Batas numeric |
| `@Positive` `@PositiveOrZero` | Numeric positif |
| `@Negative` `@NegativeOrZero` | Numeric negatif |
| `@Email` | Format email |
| `@Pattern` | Regex |
| `@Past` `@Future` | Validasi tanggal |

## 6) Transaksi

| Annotation | Fungsi singkat |
|---|---|
| `@Transactional` (jakarta.transaction) | Boundary transaksi pada method/class |
| `@TransactionScoped` | Scope berbasis transaksi |

## 7) Security

| Annotation | Fungsi singkat |
|---|---|
| `@RolesAllowed` | Batasi akses berdasar role |
| `@PermitAll` | Boleh untuk semua user |
| `@DenyAll` | Tolak semua akses |
| `@Authenticated` (Quarkus) | Wajib user authenticated |
| `@PermissionsAllowed` (Quarkus) | Cek permission granular |

## 8) OpenAPI / Swagger (MicroProfile OpenAPI)

| Annotation | Fungsi singkat |
|---|---|
| `@Tag` | Kelompok endpoint |
| `@Operation` | Ringkasan endpoint |
| `@APIResponse` / `@APIResponses` | Dokumentasi response |
| `@RequestBody` | Dokumentasi request body |
| `@Parameter` / `@Parameters` | Dokumentasi parameter |
| `@Schema` | Dokumentasi schema model |
| `@Content` | Konten response/request |
| `@ExampleObject` | Contoh payload |
| `@SecurityScheme` / `@SecuritySchemes` | Definisi skema security |
| `@SecurityRequirement` / `@SecurityRequirements` | Security requirement endpoint |
| `@Server` / `@Servers` | Definisi server |
| `@OpenAPIDefinition` | Metadata global OpenAPI |
| `@ExternalDocumentation` | Link dokumentasi eksternal |
| `@Callback` / `@Callbacks` | OpenAPI callback |

## 9) Fault Tolerance (MicroProfile FT)

| Annotation | Fungsi singkat |
|---|---|
| `@Retry` | Retry otomatis saat gagal |
| `@Timeout` | Timeout eksekusi |
| `@Fallback` | Fallback handler |
| `@CircuitBreaker` | Putuskan traffic saat error tinggi |
| `@Bulkhead` | Batasi konkurensi |
| `@Asynchronous` | Eksekusi async |

## 10) Scheduler (Quarkus Scheduler)

| Annotation | Fungsi singkat |
|---|---|
| `@Scheduled` | Menjalankan task terjadwal (cron/every) |
| `@Scheduled.ConcurrentExecution` | Atur mode konkurensi scheduler |
| `@Scheduled.SkipPredicate` | Kondisi skip run |

## 11) Caching (Quarkus Cache)

| Annotation | Fungsi singkat |
|---|---|
| `@CacheResult` | Cache hasil method |
| `@CacheInvalidate` | Invalidasi cache key tertentu |
| `@CacheInvalidateAll` | Invalidasi semua key cache |
| `@CacheKey` | Tandai argumen sebagai key cache |
| `@CacheName` | Nama cache |

## 12) Reactive Messaging (SmallRye / MicroProfile)

| Annotation | Fungsi singkat |
|---|---|
| `@Incoming` | Consumer channel |
| `@Outgoing` | Producer channel |
| `@Channel` | Inject emitter/stream channel |
| `@Broadcast` | Broadcast message ke banyak subscriber |
| `@Acknowledgment` | Atur strategi ack |
| `@Blocking` | Jalankan consumer di worker thread |
| `@Merge` | Merge beberapa publisher |

## 13) JSON (Jackson)

| Annotation | Fungsi singkat |
|---|---|
| `@JsonProperty` | Mapping nama field JSON |
| `@JsonIgnoreProperties` | Abaikan properti tak dikenal |
| `@JsonInclude` | Atur field yang diserialisasi |
| `@JsonIgnore` | Abaikan field |
| `@JsonFormat` | Format date/time |
| `@JsonAlias` | Alias nama field input |
| `@JsonCreator` | Constructor/factory deserializer |

## 14) Panache MongoDB (Quarkus)

| Annotation / Base Class | Fungsi singkat |
|---|---|
| `@MongoEntity` | Tentukan collection/database/client |
| `PanacheMongoEntity` | Entity aktif dengan `id` bawaan |
| `PanacheMongoEntityBase` | Entity base tanpa `id` bawaan |
| `PanacheMongoRepository<T>` | Repository pattern MongoDB |
| `PanacheMongoRepositoryBase<T, ID>` | Repository dengan custom ID |

## 15) Panache JPA / Hibernate (jika pakai SQL)

| Annotation / Base Class | Fungsi singkat |
|---|---|
| `@Entity` | Tandai entity JPA |
| `@Table` | Nama tabel |
| `@Id` / `@GeneratedValue` | Primary key |
| `@Column` | Mapping kolom |
| `@OneToMany` `@ManyToOne` `@OneToOne` `@ManyToMany` | Relasi |
| `PanacheEntity` / `PanacheEntityBase` | Active record style |
| `PanacheRepository<T>` | Repository style |

## 16) Quarkus Native / Reflection

| Annotation | Fungsi singkat |
|---|---|
| `@RegisterForReflection` | Daftarkan class agar tersedia untuk reflection di native image |
| `@RegisterForProxy` | Daftarkan interface/class untuk proxy native |

## 17) Testing (Quarkus Test)

| Annotation | Fungsi singkat |
|---|---|
| `@QuarkusTest` | Jalankan test dengan runtime Quarkus |
| `@QuarkusIntegrationTest` | Jalankan test pada packaged app |
| `@TestHTTPEndpoint` | Set endpoint dasar untuk REST-assured |
| `@TestHTTPResource` | Inject URL endpoint test |
| `@InjectMock` | Inject mock CDI bean |
| `@InjectSpy` | Inject spy CDI bean |
| `@QuarkusTestResource` | Register external test resource |
| `@QuarkusMainTest` | Test mode `@QuarkusMain` |

## 18) Event Bus / Vert.x (opsional)

| Annotation | Fungsi singkat |
|---|---|
| `@ConsumeEvent` (Quarkus Vert.x) | Konsumsi event dari event bus |

## 19) GraphQL (opsional)

| Annotation | Fungsi singkat |
|---|---|
| `@GraphQLApi` | Tandai class API GraphQL |
| `@Query` / `@Mutation` | Operasi query/mutation |
| `@Name` | Ubah nama field/argumen GraphQL |
| `@Description` | Deskripsi schema GraphQL |

## 20) gRPC (opsional)

| Annotation | Fungsi singkat |
|---|---|
| `@GrpcService` (Quarkus) | Daftarkan gRPC service implementation |
| `@GrpcClient` (Quarkus) | Inject gRPC client |

## 21) Qute Template (opsional)

| Annotation | Fungsi singkat |
|---|---|
| `@CheckedTemplate` | Type-safe template binding |
| `@Location` | Lokasi template |
| `@TemplateData` | Expose class ke template |

## 22) Lombok (Bukan Quarkus, tapi dipakai di project ini)

| Annotation | Fungsi singkat |
|---|---|
| `@Getter` / `@Setter` | Generate accessor |
| `@Data` | Getter/setter + equals/hashCode + toString |
| `@Builder` | Builder pattern |
| `@NoArgsConstructor` / `@AllArgsConstructor` / `@RequiredArgsConstructor` | Constructor generator |
| `@EqualsAndHashCode` | Custom equals/hashCode |
| `@ToString` | Custom toString |
| `@Value` | Immutable class |
| `@Slf4j` | Logger otomatis |

## Contoh Starter Paling Umum

```java
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "API")
public class DemoResource {

    @Inject
    DemoService service;

    @GET
    @Path("/hello")
    @Operation(summary = "Hello endpoint")
    public Response hello() {
        return Response.ok().build();
    }
}

@ApplicationScoped
class DemoService {
    @Transactional
    public void run() {
    }
}
```

## Rekomendasi Praktis

- Gunakan `@ApplicationScoped` untuk service/facade/adapter stateless.
- Gunakan `@Valid` + bean validation di boundary REST request.
- Gunakan `@Transactional` hanya di service yang memang ubah state database.
- Gunakan `@Tag` + `@Operation` + `@APIResponse` untuk dokumentasi API yang rapi.
- Gunakan `@ConfigMapping` untuk config kompleks; `@ConfigProperty` untuk config sederhana.
- Hindari over-annotation: tambah annotation hanya saat ada kebutuhan jelas.

