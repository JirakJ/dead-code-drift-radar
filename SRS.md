# Dead Code & Drift Radar — Software Requirements Specification (SRS)

**Verze:** 2026.2.1
**Autor:** Jakub Jirák
**Datum:** 12. února 2026
**Model:** Freemium JetBrains Plugin

---

## 1. Úvod

### 1.1 Účel
Dead Code & Drift Radar kombinuje statickou analýzu s git-time analýzou pro detekci mrtvého, zapomenutého a driftujícího kódu. Na rozdíl od klasických „unused code" inspections zohledňuje historii změn a identifikuje kód, který nikdo neudržuje.

### 1.2 Rozsah
Plugin analyzuje git historii (frekvence změn, authorship, stáří) a IntelliJ statickou analýzu (usage graph) pro identifikaci „code drift" — kódu, který je technicky reachable, ale prakticky opuštěný.

### 1.3 Reference
- IntelliJ Platform SDK: PSI API, Inspection API, Git4Idea API
- Brand: Code Health Suite (společně s Docstring Freshness Monitor)

---

## 2. Celkový popis

### 2.1 Perspektiva produktu
IntelliJ má built-in „unused" inspections, ale ty detekují pouze staticky nedosažitelný kód. Drift Radar přidává temporální dimenzi: kód, který nikdo neměnil 2+ roky, nemá aktivního autora, nebo divergoval od zbytku modulu.

### 2.2 Uživatelské skupiny
- **Tech leads** — chtějí identifikovat technický dluh
- **Vývojáři** — potřebují vědět, co je bezpečné odstranit
- **Architekti** — hledají oblasti k refaktoringu

---

## 3. Funkční požadavky

### FR-1: Drift skóre
- **FR-1.1:** Pro každý soubor/třídu/metodu vypočítat drift skóre 0–100
- **FR-1.2:** Faktory:
  - Stáří posledního commitu (weight: 0.3)
  - Počet unikátních autorů za posledních 12 měsíců (weight: 0.2)
  - Poměr změn v souboru vs. průměr modulu (weight: 0.2)
  - Statická dosažitelnost — počet incoming references (weight: 0.2)
  - Test coverage (pokud dostupné) (weight: 0.1)
- **FR-1.3:** Thresholds: 🟢 Active (70+), 🟡 Drifting (40–69), 🔴 Abandoned (0–39)

### FR-2: Editor integrace
- **FR-2.1:** Gutter ikona s drift stavem
- **FR-2.2:** File-level badge ve stromu projektu (Project View decorator)
- **FR-2.3:** Hover tooltip: poslední autor, poslední změna, počet referencí, drift faktory

### FR-3: Dead Code Detection (rozšířená)
- **FR-3.1:** Klasická unused detection + temporální filtr
- **FR-3.2:** „Zombie code" — kód dosažitelný jen z testů nebo deprecated cest
- **FR-3.3:** „Orphan code" — kód bez aktivního autora v organizaci (git mailmap aware)
- **FR-3.4:** Safe-to-remove confidence skóre

### FR-4: Projekt Heatmap (Paid)
- **FR-4.1:** Tool window s heatmap vizualizací drift skóre
- **FR-4.2:** Module-level agregace
- **FR-4.3:** Trend analýza: drift se zlepšuje/zhoršuje?
- **FR-4.4:** „Cleanup backlog" — prioritizovaný seznam k odstranění

### FR-5: CI integrace (Paid)
- **FR-5.1:** CLI runner: `./gradlew driftRadar --threshold=40`
- **FR-5.2:** Fail build pokud drift score klesne pod threshold
- **FR-5.3:** SARIF + JUnit XML output

### FR-6: Konfigurace
- **FR-6.1:** Settings: threshold hodnoty, váhy faktorů, ignorované cesty
- **FR-6.2:** `.drift-radar.yaml` v repo root
- **FR-6.3:** Per-file suppress: `@drift-ignore`

---

## 4. Nefunkční požadavky

### NFR-1: Výkon
- Git analýza probíhá na pozadí při otevření projektu
- Inkrementální update při commitu
- Cache git dat s TTL 15 minut

### NFR-2: Kompatibilita
- IntelliJ IDEA 2024.3+
- Jazyky: Java, Kotlin, Python, JS/TS (rozšiřitelné přes EP)

### NFR-3: Přesnost
- Drift skóre validované na open source projektech (calibration dataset)

---

## 5. Datový model

### 5.1 DriftAnalysis
```
DriftAnalysis {
  element: PsiElement
  file: VirtualFile
  lastModified: Instant
  lastAuthor: String
  uniqueAuthors12m: Int
  changeFrequency: Double (changes per month)
  moduleAvgFrequency: Double
  incomingReferences: Int
  testCoverage: Double? (0.0-1.0)
  driftScore: Int (0-100)
  status: Enum (ACTIVE, DRIFTING, ABANDONED)
  confidence: Double (0.0-1.0)
}
```

---

## 6. Freemium model

### Free tier
- Drift skóre pro soubory (file-level)
- Gutter ikony + tooltip
- Max 1 modul

### Paid tier
- Method-level granularita
- Projekt heatmap
- Trend analýza
- CI integrace
- Orphan/zombie code detection
- Export reportů
- Multi-module analýza
