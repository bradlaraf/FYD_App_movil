# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```powershell
# Debug build
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Unit tests
./gradlew test

# Single test class
./gradlew test --tests com.mobile.massiveapp.domain.articulouc.GetArticuloPorItemCodeUseCaseTest

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Lint
./gradlew lint
```

**Project config:** compileSdk 34, minSdk 24, Kotlin 1.8.20, package `com.mobile.massiveapp`

## Architecture

**MVVM + Clean Architecture** with Hilt dependency injection.

**Layers:**
- `ui/view/` — Activities annotated with `@AndroidEntryPoint`
- `ui/viewmodel/` — `@HiltViewModel` ViewModels with LiveData state
- `domain/` — 200+ UseCase classes (one responsibility each, suspend functions)
- `data/` — Repository implementations, Room DAOs, Retrofit services

**Data flow:** Activity → ViewModel → UseCase → Repository → (Network SOAP service | Room DAO)

**Coroutines:** ViewModels use `viewModelScope.launch` to call suspend UseCases. No RxJava.

## Key Technical Details

### Networking
- Protocol: **SOAP/XML** via Retrofit + SimpleXml converter
- Base URL: `http://159.138.116.164:9110/`
- Custom `ClientXmlInterceptor` wraps all requests in SOAP envelopes
- Services: `ArticuloService`, `DatosMaestrosService`, `PedidoService`, `ReportesService`, `LoginService`
- Timeouts: connect 60s, read 80s, write 60s

### Database
- **Room** database named `massive_database` with 70+ entities
- Schema exported to `app/schemas/` — update version and migration when changing entities
- Destructive migration fallback is enabled (dev convenience, not for production data)
- DAOs are all provided via `RoomModule` in Hilt

### DI
- `NetworkModule` — Retrofit, OkHttp, all API services
- `RoomModule` — Room DB instance + all 70+ DAOs
- Application class: `MassiveApp` (`@HiltAndroidApp`)

### Preferences
- `Prefs` — general session data
- `PrefsPedido` — order-specific limits
- `PrefsApp` / `PrefsSocio` — app and partner settings

### Background Work
- `SincronizarDatosWorker` (WorkManager) — syncs master data from server
- `CustomWorkerFactory` enables Hilt injection inside Workers
- `SincMaestrosBroadcastReceiver`, `ConnectionBroadcastReceiver`, `TaskReceiver`

## Feature Modules (under `ui/view/`)

| Directory | Feature |
|-----------|---------|
| `login/` | Authentication |
| `pedidocliente/` | Sales orders (largest module, 14+ activities) |
| `sociodenegocio/` | Business partners / customers |
| `cobranzas/` | Collections and payments |
| `manifiesto/` | Shipping manifests |
| `facturas/` | Invoices |
| `inventario/` | Inventory and pricing |
| `reportes/` | PDF reports and charts |
| `programacion/` | Route scheduling (RutaComercialActivity) |
| `configuracion/` | App settings |

Each feature has corresponding UseCases under `domain/<feature>/` and a ViewModel in `ui/viewmodel/`.

## Notable Libraries
- **Lottie** — loading/success animations
- **MPAndroidChart** — charts in reports
- **iTextPDF** — PDF generation
- **Glide** — image loading
- **Timber** — logging (use `Timber.d/e/w`, not `Log.*`)
- **Firebase Crashlytics** — production crash reporting
- **Mockk** + JUnit 4 — unit testing

## Conventions
- All activities must be registered in `AndroidManifest.xml` with `@AndroidEntryPoint`
- New features follow the pattern: Entity → DAO → Repository → UseCases → ViewModel → Activity
- SOAP responses are modeled with `@Element`/`@ElementList` annotations in `data/network/response/`
- Room entities live in `data/database/entities/`, DAOs in `data/database/dao/`
- Portrait orientation is enforced on all activities via manifest

## Coding Patterns (follow these exactly)

### ViewModel
- Name the ViewModel variable after its type: `rutaComercialViewModel`, `pedidoViewModel`, etc. — never just `viewModel`
- Public `MutableLiveData` with a `data` prefix — **not** private backing field + public `LiveData`:
  ```kotlin
  val dataGetAllDireccionesCliente = MutableLiveData<List<DoDireccion>>()
  fun getAllDireccionesCliente(cardCode: String) {
      viewModelScope.launch {
          val result = getAllDireccionesClienteUseCase(cardCode)
          result.let { dataGetAllDireccionesCliente.postValue(it) }
      }
  }
  ```
- `result.let { ... }` is the ViewModel pattern for posting LiveData — **never use `let` inside an Activity observer**
- Flow-backed list data stays as a plain `Flow` property (no LiveData wrapper):
  ```kotlin
  val dataGetAllRutas: Flow<List<DoRutaComercialView>> = getAllRutasComercialUseCase.getAllRutas()
  ```

### Use Cases
- Always wrap the body in `try/catch`; return `Boolean` (`true`/`false`) or `emptyList<T>()` on exception
- May expose a named method (e.g., `getAllRutas()`) instead of — or in addition to — `invoke()`

### Activity structure
- `onCreate()` calls `setDefaultUi()` then `setData()`
- `setDefaultUi()` — all UI setup: adapter initialization, click listeners, swipe callbacks
- `setData()` — all data loading and Flow/LiveData observation
- Keep a `listaXxx: List<DoXxxView>` field, updated inside `collectLatest`, so swipe callbacks can read the current item by position

### No extra variables en Activity — usar binding directo
Leer valores de los TextViews directamente desde `binding` en lugar de guardar variables auxiliares:
```kotlin
// CORRECTO
val nombreVendedor = binding.txvNuevaRutaVendedorValue.text.toString()

// INCORRECTO
private var slpNameSeleccionado: String = ""  // variable innecesaria
```

### Observers en Activity
Acceder directamente al valor recibido, sin `let`:
```kotlin
// CORRECTO
rutaComercialViewModel.dataGetRutaComercial.observe(this) { ruta ->
    binding.txvFechaValue.text = ruta?.FechaRuta ?: ""
}

// INCORRECTO — let es del ViewModel, no del Activity
rutaComercialViewModel.dataGetRutaComercial.observe(this) { ruta ->
    ruta?.let { binding.txvFechaValue.text = it.FechaRuta }
}
```

### Observing one-shot LiveData (dialogs, etc.)
- Para LiveData que **no** tiene valor previo: usar `observeOnce` de `ui.view.util`
- Para LiveData **nullable** que se resetea antes de cargar (patrón llamada repetida): usar `observeOnceNotNull` — ignora la emisión null y se activa solo con datos reales:
```kotlin
// En el ViewModel: reset a null antes de cargar
fun getAllDireccionesCliente(cardCode: String) {
    dataGetAllDireccionesCliente.value = null  // reset sincrónico
    viewModelScope.launch {
        val result = getAllDireccionesClienteUseCase(cardCode)
        result.let { dataGetAllDireccionesCliente.postValue(it) }
    }
}

// En el Activity: observeOnceNotNull evita disparar con el valor viejo
rutaComercialViewModel.getAllDireccionesCliente(detalle.CardCode)
rutaComercialViewModel.dataGetAllDireccionesCliente.observeOnceNotNull(this) { direcciones ->
    BaseDialogChecklistWithId(
        checkSelected = detalle.Address,
        opciones = direcciones.map { it.Street }
    ) { calleSeleccionada, _ ->
        if (calleSeleccionada.isNotEmpty()) rutaComercialViewModel.updateAddress(...)
    }.show(supportFragmentManager, "tag")
}
```
- **Nunca** poner un `LiveData.observe()` dentro de un bloque `collectLatest` — se apilan observers en cada emisión del Flow. Separarlos siempre en `setData()`
- **Nunca** poner `LiveData.observe()` dentro de un click listener (`setOnClickListener`, `onOptionsItemSelected`, etc.) — cada click registra un observer nuevo que nunca se elimina. Usar siempre `observeOnce` en esos casos.

### Search bar
Use `menu_socio_lupa_add.xml` + `SearchViewHelper` (the app standard). Do **not** embed a `SearchView` inside a custom menu XML.

### Swipe-to-delete
Use the existing `SwipeToDeletePedidos` abstract class (`ui.adapters.extension`). It swipes RIGHT with a red background + delete icon. Override `onSwiped` using `bindingAdapterPosition`, call `notifyItemChanged(position)` after triggering the delete:
```kotlin
val swipeToDeleteCallback = object : SwipeToDeletePedidos(this) {
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        rutaComercialViewModel.deleteRutaComercialDetalle(
            listaRutaDetalles[position].DocLine,
            listaRutaDetalles[position].AccDocEntry
        )
        binding.rvClientes.adapter?.notifyItemChanged(position)
    }
}
ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.rvClientes)
```

Para hacer el swipe **condicional** (solo permitirlo si el ítem cumple una condición), sobrescribir `getMovementFlags()` en el callback anónimo:
```kotlin
val swipeCallback = object : SwipeToDeletePedidos(this) {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val position = viewHolder.bindingAdapterPosition
        if (position == RecyclerView.NO_ID || lista.isEmpty()) return makeMovementFlags(0, 0)
        return if (lista[position].Status == "P") makeMovementFlags(0, ItemTouchHelper.RIGHT)
        else makeMovementFlags(0, 0)
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { ... }
}
```

### Indicador de estado en items del RecyclerView
Para mostrar una barra de color lateral con ícono (verde=confirmado, rojo=pendiente), el `MaterialCardView` (que extiende `FrameLayout`) acepta múltiples hijos apilados. El primer hijo es el strip de estado, el segundo el contenido:
```xml
<MaterialCardView>
    <ConstraintLayout android:id="@+id/clXxxStatus" android:layout_width="wrap_content"
        android:layout_height="match_parent" android:background="@color/color_green_dark">
        <ImageView android:id="@+id/imvXxxStatus" app:srcCompat="@drawable/icon_pending" ... />
    </ConstraintLayout>
    <ConstraintLayout android:layout_width="match_parent" android:layout_height="wrap_content" android:padding="12dp">
        <!-- contenido — txvDocLine necesita android:layout_marginStart="24dp" para no solaparse -->
    </ConstraintLayout>
</MaterialCardView>
```
En `render()` del ViewHolder evaluar `AccMigrated` y `Status`:
```kotlin
val colorIcono = ContextCompat.getColor(itemView.context, R.color.color_white)
imvXxxStatus.setColorFilter(colorIcono, PorterDuff.Mode.SRC_IN)
if (item.AccMigrated == "Y" && item.Status == "A") {
    imvXxxStatus.setImageResource(R.drawable.icon_confirmed)
    clXxxStatus.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_green_dark))
} else {
    imvXxxStatus.setImageResource(R.drawable.icon_pending)
    clXxxStatus.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_red))
}
```

### Deleting detalle rows
Always delete by `(docLine, accDocEntry)` — never by `cardCode` alone.

### Adapter ViewHolder
Declare properties without initializers and assign them in an `init {}` block (matching `ManifiestoAdapter` style):
```kotlin
class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txvCardName: TextView
    val txvStreet: TextView
    init {
        txvCardName = view.findViewById(R.id.txvCardName)
        txvStreet   = view.findViewById(R.id.txvStreet)
    }
}
```

### JOIN / view domain models
When a list screen needs computed or joined fields, define a `DoXxxView` data class in the **same file** as the main `DoXxx` domain model. The DAO's `getAllFlow()` returns `Flow<List<DoXxxView>>` via a SQL JOIN query. Example computed field: `CantidadClientes` via a COUNT subquery.

### Badge de estado migrado
Para mostrar estado de migración en items del RecyclerView, usar un `TextView` con fondo de color en el item layout. En el `render()` del ViewHolder:
```kotlin
val migrado = item.AccMigrated == "Y"
txvMigrado.text = if (migrado) "S" else "N"
val color = if (migrado)
    ContextCompat.getColor(itemView.context, R.color.color_green_dark)
else
    ContextCompat.getColor(itemView.context, R.color.color_red)
txvMigrado.setBackgroundColor(color)
```
El campo `AccMigrated` en el `DoXxxView` se mapea desde `AccAction` de la entity en la query del DAO: `T0.AccAction AS AccMigrated`.

### Dialog de comentarios / texto libre
Usar `BaseDialogComentarioRuta` (`ui.base`) para popups con un campo de texto multilinea. Recibe el texto actual (para pre-llenar) y un callback con el texto ingresado. No requiere que el campo tenga contenido (a diferencia de `BaseDialogEdt`).

### ItemTouchHelper — drag vs swipe
Cuando una pantalla tiene drag-and-drop Y swipe-to-delete, usar **dos** `ItemTouchHelper` separados:
1. El de drag (clase-nivel): solo `UP or DOWN`, sin dirección de swipe (`0`), `isLongPressDragEnabled = false`
2. El de swipe: usar `SwipeToDeletePedidos` dentro de `setDefaultUi()`
No mezclar swipe LEFT en el callback de drag — generan conflicto en el RecyclerView.

### Abrir Google Maps con coordenadas
Usar `abrirEnGoogleMaps(context, latitud, longitud)` de `ui.view.util.Utils`. Incluye fallback al browser si Maps no está instalado:
```kotlin
abrirEnGoogleMaps(context, item.Latitud, item.Longitud)
```
Al agregar intents hacia apps externas, declarar el paquete en `<queries>` dentro de `AndroidManifest.xml` (obligatorio desde Android 11 / API 30):
```xml
<queries>
    <package android:name="com.google.android.apps.maps" />
</queries>
```
