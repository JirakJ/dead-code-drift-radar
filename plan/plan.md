# Dead Code & Drift Radar — Implementační plán

**Verze:** 2026.2.1
**Status:** Připraveno k implementaci

---

## Fáze 1: Projekt scaffolding

- [ ] Inicializovat IntelliJ Platform Plugin projekt (Gradle Kotlin DSL)
  - [ ] `build.gradle.kts` — IntelliJ Platform 2024.3+, JVM 17
  - [ ] `settings.gradle.kts`, `gradle.properties` (verze `2026.2.1`)
  - [ ] Gradle wrapper (8.x)
- [ ] `src/main/resources/META-INF/plugin.xml`
  - [ ] Plugin ID: `com.jirak.driftradar`
  - [ ] Závislosti: `com.intellij.modules.platform`, `Git4Idea`, `com.intellij.modules.lang`
- [ ] Balíčková struktura: `com.jirak.driftradar.*`
  - [ ] `.model` — datové třídy
  - [ ] `.git` — git historie analýza
  - [ ] `.static` — statická analýza (reference graph)
  - [ ] `.score` — drift skóre kalkulace
  - [ ] `.detection` — zombie/orphan detection
  - [ ] `.ui` — gutter, project view, heatmap
  - [ ] `.settings` — konfigurace
- [ ] `src/main/resources/messages/DriftRadarBundle.properties`
- [ ] `.gitignore`
- [ ] Ověřit `./gradlew build`

## Fáze 2: Datový model

- [ ] `DriftAnalysis.kt`
  - [ ] `element: SmartPsiElementPointer<PsiElement>`, `file: VirtualFile`
  - [ ] `granularity: Granularity` (FILE, CLASS, METHOD)
  - [ ] `lastModified: Instant`, `lastAuthor: String`
  - [ ] `uniqueAuthors12m: Int`, `changeFrequency: Double`
  - [ ] `moduleAvgFrequency: Double`, `incomingReferences: Int`
  - [ ] `driftScore: Int` (0–100), `status: DriftStatus`
  - [ ] `confidence: Double` (0.0–1.0)
- [ ] `DriftStatus.kt` — enum: ACTIVE (70+), DRIFTING (40–69), ABANDONED (0–39)
- [ ] `Granularity.kt` — enum: FILE, CLASS, METHOD
- [ ] `GitElementHistory.kt` — git data per element
- [ ] Unit testy

## Fáze 3: Git Historie Analyzer

- [ ] `GitHistoryAnalyzer.kt`
  - [ ] `fun analyzeFile(file: VirtualFile): GitFileHistory`
  - [ ] `fun analyzeElement(file: VirtualFile, range: TextRange): GitElementHistory`
  - [ ] Data: posledni commit, všichni autoři, commit frequency (per měsíc)
  - [ ] Git4Idea API: `GitHistoryUtils`, `GitLogUtil`
  - [ ] Cache: per-file s TTL 15 min
  - [ ] Asynchronní: `Task.Backgroundable`
- [ ] `GitFileHistory.kt` — agregované git metriky per soubor
- [ ] `ModuleHistoryAggregator.kt` — průměrné metriky per modul (pro relativní porovnání)
- [ ] Unit testy (mock git data)
- [ ] Integrační testy s test git repo

## Fáze 4: Reference Graph Builder

- [ ] `ReferenceGraphBuilder.kt`
  - [ ] `fun countIncomingReferences(element: PsiElement): Int`
  - [ ] Využít IntelliJ `ReferencesSearch.search()` API
  - [ ] Cache výsledků per element
  - [ ] Scope: projekt (ne external libraries)
- [ ] `ReferenceGraphService.kt` — project-level service
  - [ ] Lazy building při prvním dotazu
  - [ ] Invalidace při PSI change events
- [ ] Unit testy

## Fáze 5: Drift Score Calculator

- [ ] `DriftScoreCalculator.kt`
  - [ ] Input: `GitElementHistory` + `referenceCount` + `moduleAvgFrequency`
  - [ ] Algoritmus s konfigurovatelnými vahami:
    - [ ] Stáří posledního commitu (w: 0.3): starší = nižší skóre
    - [ ] Unikátní autoři za 12m (w: 0.2): méně = nižší skóre
    - [ ] Relativní frekvence změn (w: 0.2): pod průměrem = nižší
    - [ ] Incoming references (w: 0.2): méně = nižší
    - [ ] Test coverage (w: 0.1): pokud dostupné
  - [ ] Normalizace na 0–100
  - [ ] Confidence: f(kvalita git dat, počet commitů)
- [ ] `DriftService.kt` — project-level service, orchestruje analýzu
  - [ ] Inkrementální update při file change
  - [ ] Registrace jako `ProjectService`
- [ ] Rozsáhlé unit testy (boundary values, edge cases, calibration)

## Fáze 6: Editor integrace — Gutter

- [ ] `DriftLineMarkerProvider.kt` implements `LineMarkerProvider`
  - [ ] File-level (free): ikona u první deklarace v souboru
  - [ ] Method-level (paid): ikona u každé metody/funkce
  - [ ] Ikony: 🟢 Active, 🟡 Drifting, 🔴 Abandoned
  - [ ] Lazy: skóre na pozadí, ikona se aktualizuje
- [ ] `DriftTooltipProvider.kt`
  - [ ] Poslední autor, poslední změna, reference count
  - [ ] Drift faktory breakdown
  - [ ] Confidence indikátor
- [ ] Gutter ikony (SVG)

## Fáze 7: Project View Decorator

- [ ] `DriftProjectViewDecorator.kt` implements `ProjectViewNodeDecorator`
  - [ ] Registrace v `plugin.xml`
  - [ ] Badge na souboru: malá barevná tečka (🟢🟡🔴) vedle názvu
  - [ ] Tooltip: drift skóre
- [ ] Non-blocking: dekorace z cache, kalkulace na pozadí

## Fáze 8: Konfigurace

- [ ] `DriftRadarSettings.kt` implements `PersistentStateComponent`
  - [ ] Threshold hodnoty (ACTIVE/DRIFTING/ABANDONED hranice)
  - [ ] Váhy faktorů (konfigurovatelné)
  - [ ] Ignorované cesty (glob patterns)
  - [ ] Granularity (file vs. method — paid flag)
- [ ] `DriftRadarConfigurable.kt` implements `Configurable`
  - [ ] Settings → Tools → Drift Radar
  - [ ] Threshold slidery, weight adjusters, ignore patterns
- [ ] `TeamConfigLoader.kt` — `.drift-radar.yaml` z repo root
- [ ] Unit testy

## Fáze 9: Startup & Indexing

- [ ] `DriftRadarStartupActivity.kt` implements `ProjectActivity`
  - [ ] Spustí background analýzu při otevření projektu
  - [ ] Progress bar: „Analyzing code drift..."
  - [ ] Inkrementální: přeskočí soubory s platnou cache
- [ ] `DriftIndexService.kt` — udržuje drift data per projekt
  - [ ] Persistence: `.idea/drift-radar-cache.json` (gitignored)
  - [ ] Invalidace při git pull/merge

## Fáze 10: Polish & Release

- [ ] README.md
- [ ] `./gradlew verifyPlugin`, `./gradlew buildPlugin`
- [ ] Manuální testování na Java/Kotlin projektech
- [ ] Cross-IDE test
- [ ] Marketplace listing
- [ ] Git tag `v2026.2.1`

---

## Budoucí fáze (Paid)

- [ ] Method-level granularita
- [ ] Zombie code detection (reachable jen z testů/deprecated)
- [ ] Orphan code detection (autor opustil organizaci)
- [ ] Heatmap tool window (treemap vizualizace)
- [ ] Trend analýza (drift se zlepšuje/zhoršuje)
- [ ] Cleanup backlog (prioritizovaný seznam)
- [ ] CI integrace (CLI runner, SARIF)
- [ ] Code Health Suite integrace s Docstring Freshness Monitor
