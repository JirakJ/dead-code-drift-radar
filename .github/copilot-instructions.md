# Copilot Instructions — Dead Code & Drift Radar

## Projekt
JetBrains IDE plugin — kombinuje statickou analýzu s git-time analýzou pro detekci mrtvého a driftujícího kódu.

## Technický stack
- **Jazyk:** Kotlin (JVM 17+)
- **Build:** Gradle (Kotlin DSL) s `org.jetbrains.intellij.platform` plugin 2.x
- **Platform:** IntelliJ Platform SDK 2024.3+
- **Testování:** JUnit 5 + `BasePlatformTestCase`

## Build a testy
```bash
./gradlew build
./gradlew test
./gradlew test --tests "*.DriftScoreCalculatorTest.testAbandonedCode"
./gradlew runIde
./gradlew verifyPlugin
```

## Architektura
- **Core:** `DriftScoreCalculator` — váží git metriky (stáří, autoři, frekvence) se statickou analýzou (reference count)
- **Git layer:** `GitHistoryAnalyzer` — extrahuje commit history, authorship, change frequency per element
- **Static layer:** `ReferenceGraphBuilder` — PSI-based incoming reference count
- **UI:** `LineMarkerProvider` (gutter), `ProjectViewDecorator` (file badges), `ToolWindowFactory` (heatmap)
- **Detection:** `ZombieCodeDetector` (reachable jen z testů), `OrphanCodeDetector` (bez aktivního autora)

## Konvence
- Balíčky: `com.jakubjirak.driftradar.*`
- Drift skóre: 0–100, status: ACTIVE (70+), DRIFTING (40–69), ABANDONED (0–39)
- Git operace na background threadech (`Task.Backgroundable`)
- Cache TTL: 15 minut
- Texty v `messages/DriftRadarBundle.properties`
- Verzování: `2026.2.X`

## Production-grade požadavky
- Všechny I/O operace na background threadech (`Task.Backgroundable`, coroutines)
- Read/Write actions správně (`ReadAction.run {}`, `WriteAction.run {}`)
- `Disposable` pattern pro listeners a services
- Exception handling: žádné unhandled exceptions, graceful degradation
- Logging: `Logger.getInstance(MyClass::class.java)` v každé třídě
- EDT nesmí být blokován — žádné síťové/git/file operace na EDT
- Smart mode check (`DumbService.isDumb()`) kde potřeba

## Freemium architektura
- `FreemiumGate` centrální guard: `FreemiumGate.isPaidFeatureAvailable("feature.id")`
- JetBrains Marketplace Licensing API přes `<product-descriptor>` v `plugin.xml`
- Paid UI elementy: skryté nebo disabled ve free tieru (ne crash/error)
- Trial: 30 dní plný přístup, pak downgrade na free tier
- Upgrade prompt: max 1× denně, non-intrusive balloon

## CI/CD
- GitHub Actions: `./gradlew check`, `verifyPlugin`, `runPluginVerifier`
- Detekt pro statickou analýzu Kotlin kódu
- Tag-based release → `publishPlugin` na Marketplace
- Kotlin coding conventions (JetBrains official style)

## Ikona
- `src/main/resources/META-INF/pluginIcon.svg` (40×40, light)
- `src/main/resources/META-INF/pluginIcon_dark.svg` (40×40, dark)
- Action ikony: 16×16 + 13×13 SVG (light + dark varianty)

## Freemium hranice
- Free: file-level drift, 1 modul, gutter ikony
- Paid: method-level, heatmap, trend, CI, orphan/zombie detection
