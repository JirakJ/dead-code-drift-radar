# Dead Code & Drift Radar — Implementační plán

**Verze:** 2026.2.1
**Status:** Připraveno k implementaci

---

## Fáze 0: Plugin Ikona & Branding

- [ ] Navrhnout plugin ikonu (SVG + PNG)
  - [ ] Koncept: 📡 Radar s drifting signálem
  - [ ] Hlavní ikona: 40×40 SVG pro Marketplace listing
  - [ ] Toolbar ikona: 16×16 + 13×13 SVG (light + dark varianta)
  - [ ] Tool window ikona: 13×13 SVG
  - [ ] `src/main/resources/META-INF/pluginIcon.svg` (40×40, light theme)
  - [ ] `src/main/resources/META-INF/pluginIcon_dark.svg` (40×40, dark theme)
  - [ ] Action ikony pro toolbar/menu (16×16 SVG, light + dark)
- [ ] Barevná paleta konzistentní s Code Health Suite brandem
- [ ] Marketplace banner obrázek (1280×800)
- [ ] Screenshoty pro Marketplace listing (min 3, 1280×800 PNG)

## Fáze 1: Projekt scaffolding

- [ ] Inicializovat IntelliJ Platform Plugin projekt (Gradle Kotlin DSL)
  - [ ] `build.gradle.kts` — IntelliJ Platform 2024.3+, JVM 17
  - [ ] `settings.gradle.kts`, `gradle.properties` (verze `2026.2.1`)
  - [ ] Gradle wrapper (8.x)
- [ ] `src/main/resources/META-INF/plugin.xml`
  - [ ] Plugin ID: `com.jakubjirak.driftradar`
  - [ ] Závislosti: `com.intellij.modules.platform`, `Git4Idea`, `com.intellij.modules.lang`
- [ ] Balíčková struktura: `com.jakubjirak.driftradar.*`
  - [ ] `.model` — datové třídy
  - [ ] `.git` — git historie analýza
  - [ ] `.static` — statická analýza (reference graph)
  - [ ] `.score` — drift skóre kalkulace
  - [ ] `.detection` — zombie/orphan detection
  - [ ] `.ui` — gutter, project view, heatmap
  - [ ] `.settings` — konfigurace
- [ ] `src/main/resources/messages/DriftRadarBundle.properties`
- [ ] `.gitignore` (build/, .idea/, .gradle/, .kotlin/, out/)
- [ ] `gradle/libs.versions.toml` — version catalog
- [ ] `LICENSE` (Apache-2.0)
- [ ] `.editorconfig` (Kotlin style conventions)
- [ ] Detekt konfigurace (`detekt.yml`)
- [ ] GitHub Actions CI workflow (`.github/workflows/build.yml`)
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

## Fáze 10: Freemium & Licensing Infrastructure

- [ ] Integrace JetBrains Marketplace Licensing API
  - [ ] `LicenseChecker.kt` — ověření licence přes `com.intellij.ide.plugins.PluginManager` a Marketplace API
  - [ ] `LicenseStatus.kt` — enum: FREE, TRIAL, PAID, EXPIRED
  - [ ] `FreemiumGate.kt` — centrální guard pro paid features
    - [ ] `fun isPaidFeatureAvailable(featureId: String): Boolean`
    - [ ] Graceful degradation — paid UI elementy skryté/disabled ve free tieru
  - [ ] Trial period: 30 dní plný přístup
- [ ] `plugin.xml` — `<product-descriptor>` element pro Marketplace licensing
  - [ ] `code` atribut (unique product code)
  - [ ] `release-date`, `release-version`
- [ ] Nag screen / upgrade prompt
  - [ ] Non-intrusive balloon notification při pokusu o paid feature
  - [ ] „Upgrade to Pro" action → Marketplace stránka
  - [ ] Max 1× denně (ne spam)
- [ ] Feature flags
  - [ ] `FeatureRegistry.kt` — definice free vs. paid features
  - [ ] Každý paid feature má `@PaidFeature("feature.id")` anotaci nebo guard check
- [ ] Unit testy pro licensing logic (mock Marketplace API)

## Fáze 11: Production-Grade Quality

- [ ] Error Handling & Resilience
  - [ ] Globální exception handler (`ErrorReportSubmitter` implementace)
  - [ ] Graceful degradation — plugin nesmí crashnout IDE
  - [ ] Try-catch na všech EP entry pointech
  - [ ] Timeout na všech I/O operacích (network, git, file system)
  - [ ] Rate limiting na API volání
- [ ] Logging
  - [ ] `Logger` instance v každé třídě (IntelliJ `Logger.getInstance()`)
  - [ ] Structured logging: DEBUG pro vývoj, INFO pro operace, WARN/ERROR pro problémy
  - [ ] Žádné citlivé údaje v logech
- [ ] Telemetrie (opt-in, anonymní)
  - [ ] `PluginUsageTracker.kt` — FUS (Feature Usage Statistics) integrace
  - [ ] Metriky: aktivace featury, error rate, latence
  - [ ] Registrace FUS group v `plugin.xml`
- [ ] Performance
  - [ ] Profiling na velkých projektech (10k+ souborů)
  - [ ] Background thread pro všechny I/O operace (nesmí blokovat EDT)
  - [ ] Read/write action správně (ReadAction, WriteAction, smart mode check)
  - [ ] Disposal pattern: `Disposable` pro všechny listeners a services
  - [ ] Memory leak check (WeakReference kde potřeba)
- [ ] Testing — production úroveň
  - [ ] Unit testy: ≥80 % coverage core logiky
  - [ ] Integrační testy: `BasePlatformTestCase` pro PSI, inspections, EP
  - [ ] UI testy: `HeavyPlatformTestCase` pro tool windows, dialogy
  - [ ] Performance testy: benchmark na velkých souborech
- [ ] CI/CD Pipeline
  - [ ] GitHub Actions workflow:
    - [ ] `./gradlew check` — kompilace + testy
    - [ ] `./gradlew verifyPlugin` — plugin strukturální validace
    - [ ] `./gradlew verifyPluginProjectConfiguration` — metadata validace
    - [ ] `./gradlew runPluginVerifier` — binární kompatibilita s target IDE verzemi
    - [ ] Qodana scan (JetBrains code quality)
  - [ ] Release workflow:
    - [ ] Tag-based release (push tag → build → publish)
    - [ ] `./gradlew publishPlugin` — automatický upload na Marketplace
    - [ ] Changelog generace z git commitů
  - [ ] Dependabot / Renovate pro dependency updates
- [ ] Kódová kvalita
  - [ ] Kotlin coding conventions (official JetBrains style)
  - [ ] Detekt nebo ktlint pro statickou analýzu
  - [ ] Žádné deprecated API (IntelliJ Platform compatibility)
  - [ ] `@ApiStatus.Experimental` / `@ApiStatus.Internal` anotace
- [ ] Dokumentace
  - [ ] KDoc pro všechny public API
  - [ ] README.md: instalace, konfigurace, screenshoty, FAQ
  - [ ] CHANGELOG.md (Keep a Changelog format)
  - [ ] CONTRIBUTING.md (pokud open source)

## Fáze 12: Polish & Release

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
