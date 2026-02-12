# Dead Code & Drift Radar — Business Requirements Specification (BRS)

**Verze:** 2026.2.1
**Autor:** Jakub Jirák
**Datum:** 12. února 2026
**Model:** Freemium JetBrains Plugin

---

## 1. Executive Summary

Dead Code & Drift Radar přináší unikátní kombinaci statické analýzy a git-time analýzy pro identifikaci opuštěného a driftujícího kódu. Plugin pomáhá týmům proaktivně řídit technický dluh tím, že vizualizuje, které části codebase nikdo neudržuje.

---

## 2. Obchodní cíle

### 2.1 Primární cíle
1. **Enterprise value prop** — technický dluh kvantifikace pro management
2. **Code Health Suite** — páteřní produkt společného brandu
3. **Sticky metriky** — drift trend vytváří ongoing engagement

### 2.2 KPI
| Metrika | Cíl (6 měsíců) | Cíl (12 měsíců) |
|---------|:---:|:---:|
| Celkové instalace | 6 000 | 25 000 |
| MAU | 2 500 | 10 000 |
| Paid konverze | 4 % | 6 % |
| Marketplace rating | ≥ 4.0 | ≥ 4.2 |

---

## 3. Cílový trh

### 3.1 Primární segmenty
- **Tech leads / Engineering managers** (40 %) — technický dluh reporting
- **Senior vývojáři** (35 %) — refactoring prioritizace
- **Architekti** (25 %) — systémové health metriky

---

## 4. Konkurenční prostředí

### 4.1 Přímá konkurence
Žádný plugin nekombinuje git-time analýzu se statickou analýzou pro drift detection.

### 4.2 Nepřímá konkurence
| Řešení | Omezení |
|--------|---------|
| IntelliJ unused inspections | Pouze statická dosažitelnost |
| SonarQube / CodeClimate | Server-side, ne IDE-native |
| CodeScene | SaaS, drahý, ne IDE plugin |

---

## 5. Revenue model

| Tier | Cena |
|------|------|
| **Free** | $0 — file-level drift, 1 modul |
| **Pro** | $3.90/měsíc nebo $39/rok — method-level, heatmap, CI |
| **Team** | $7.90/user/měsíc — Pro + team metriky, trend reports |

---

## 6. Go-to-Market

1. Blog: „Your Codebase Has Dead Zones — Here's Where They Are"
2. Cross-promo s Docstring Freshness Monitor (Code Health Suite)
3. Engineering management communities (LeadDev, CTO Craft)

---

## 7. Rizika

| Riziko | Mitigace |
|--------|---------|
| Git analýza pomalá na monorepo | Sampling + caching |
| CodeScene vstoupí na Marketplace | IDE-native UX jako diferenciátor |
| Drift skóre subjektivní | Kalibrace na OSS projektech, konfigurovatelné váhy |

---

## 8. Milníky

| Milestone | Popis |
|-----------|-------|
| **M1: MVP** | File-level drift, Java/Kotlin, gutter ikony |
| **M2: Deep** | Method-level, zombie/orphan detection |
| **M3: Viz** | Heatmap, trend, cleanup backlog |
| **M4: CI** | CLI runner, SARIF |
| **M5: Suite** | Code Health Suite integrace |

---

## 9. Success Criteria

1. 12 000+ instalací do 9 měsíců
2. ≥ 4 % paid konverze
3. Code Health Suite brand propojení s Docstring Freshness Monitor
4. Alespoň 2 enterprise zákazníci (team tier)
