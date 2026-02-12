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
- Balíčky: `com.jirak.driftradar.*`
- Drift skóre: 0–100, status: ACTIVE (70+), DRIFTING (40–69), ABANDONED (0–39)
- Git operace na background threadech (`Task.Backgroundable`)
- Cache TTL: 15 minut
- Texty v `messages/DriftRadarBundle.properties`
- Verzování: `2026.2.X`

## Freemium hranice
- Free: file-level drift, 1 modul, gutter ikony
- Paid: method-level, heatmap, trend, CI, orphan/zombie detection
