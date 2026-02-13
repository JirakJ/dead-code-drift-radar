# Dead Code & Drift Radar — Git Enhancement Specifikace

**Verze:** 2026.2.1
**Status:** Plánováno
**Závislost:** Git4Idea API, `com.intellij.git4idea`

---

## Přehled

Rozšíření pluginu o pokročilé Git-based funkce pro detekci ghost kódu, zombie branches a remote drift analýzu. Tyto funkce doplňují stávající drift score kalkulaci o live Git stav a umožňují identifikovat problémy, které statická analýza neodhalí.

---

## FR-G1: Ghost Code Detection

**Popis:** Detekce kódu, který existuje lokálně ale nebyl commitnut — uncommitted changes, které vypadají jako dead code (nové soubory bez referencí, nové metody bez volání).

### Akceptační kritéria

- [ ] AC-1: Plugin identifikuje soubory ve stavu `Untracked` nebo `Modified` které nemají žádné incoming references v projektu
- [ ] AC-2: Ghost code se zobrazí v dedikovaném panelu tool window s ikonou 👻
- [ ] AC-3: U každého ghost elementu se zobrazí doba od poslední modifikace (file system timestamp)
- [ ] AC-4: Quick-fix action: „Stage & Commit" nebo „Delete" nebo „Add to .gitignore"
- [ ] AC-5: Konfigurovatelné ignorované cesty (glob patterns) — default: `build/`, `out/`, `.gradle/`

### Datový model — rozšíření

```kotlin
data class GhostCodeEntry(
    val file: VirtualFile,
    val element: SmartPsiElementPointer<PsiElement>?,
    val gitStatus: GitFileStatus, // UNTRACKED, MODIFIED, ADDED
    val lastModifiedFs: Instant,
    val incomingReferences: Int,
    val confidence: Double // 0.0–1.0
)

enum class GitFileStatus {
    UNTRACKED, MODIFIED, ADDED, RENAMED
}
```

### Implementační poznámky

- Využít `GitRepositoryManager` a `ChangeListManager` pro detekci uncommitted changes
- Reference count přes existující `ReferenceGraphBuilder`
- Background thread s `Task.Backgroundable`, invalidace při VCS change events

---

## FR-G2: Zombie Branch Detector

**Popis:** Detekce stale branches s uncommitted changes — opuštěné branch které nikdo nemergnul a obsahují potenciálně cenný nebo mrtvý kód.

### Akceptační kritéria

- [ ] AC-1: Plugin skenuje všechny lokální branches a identifikuje „zombie" branches (poslední commit starší než konfigurovatelný threshold, default 90 dní)
- [ ] AC-2: Pro každou zombie branch se zobrazí: název, poslední autor, datum posledního commitu, počet commitů ahead/behind oproti main/master
- [ ] AC-3: Barevné kódování: 🟡 Stale (30–90 dní), 🔴 Zombie (90+ dní)
- [ ] AC-4: Action: „Switch to Branch", „Delete Branch", „Cherry-pick Commits"
- [ ] AC-5: Notifikace při detekci nových zombie branches (max 1× týdně)

### Datový model — rozšíření

```kotlin
data class ZombieBranch(
    val name: String,
    val lastCommitDate: Instant,
    val lastAuthor: String,
    val commitsAhead: Int,
    val commitsBehind: Int,
    val baseBranch: String, // main/master
    val status: ZombieBranchStatus,
    val uniqueFiles: List<String> // soubory změněné jen v tomto branchi
)

enum class ZombieBranchStatus {
    ACTIVE,  // < 30 dní
    STALE,   // 30–90 dní
    ZOMBIE   // > 90 dní
}
```

### Implementační poznámky

- Git4Idea: `GitBranchUtil`, `GitHistoryUtils` pro branch metadata
- Porovnání oproti `HEAD` hlavního branch (`main` nebo `master` — auto-detect)
- Lazy loading: branch list z cache, detail on-demand

---

## FR-G3: Remote Drift Analysis

**Popis:** Analýza lokálního driftu od origin — identifikace souborů a modulů kde lokální stav významně diverguje od remote repository.

### Akceptační kritéria

- [ ] AC-1: Plugin porovnává lokální HEAD s `origin/main` (nebo konfigurovatelný remote branch)
- [ ] AC-2: Pro každý divergující soubor se zobrazí: počet změněných řádků, typ změny (přidáno/odebráno/modifikováno), drift skóre
- [ ] AC-3: Modul-level agregace: celkový drift skóre per modul/adresář
- [ ] AC-4: Vizualizace v Project View: drift ikona (↕️) u divergujících souborů
- [ ] AC-5: Action: „Fetch & Compare", „Rebase onto Remote", „Show Diff"
- [ ] AC-6: Automatický fetch s konfigurovatelným intervalem (default: 30 min)

### Datový model — rozšíření

```kotlin
data class RemoteDriftEntry(
    val file: VirtualFile,
    val linesAdded: Int,
    val linesRemoved: Int,
    val linesModified: Int,
    val localCommits: Int,   // commity lokálně které nejsou na remote
    val remoteCommits: Int,  // commity na remote které nejsou lokálně
    val driftScore: Int,     // 0–100, vyšší = více diverguje
    val direction: DriftDirection
)

enum class DriftDirection {
    LOCAL_AHEAD,    // lokální změny nepushnuty
    REMOTE_AHEAD,   // remote má nové změny
    DIVERGED,        // obě strany mají unikátní commity
    SYNCED           // vše synchronizováno
}
```

### Implementační poznámky

- `git rev-list --left-right --count HEAD...origin/main` pro commit count
- `git diff --stat HEAD origin/main` pro file-level diff
- Background fetch: `GitFetcher` API s konfigurovatelným intervalem
- Integrace s existujícím drift score — remote drift jako dodatečný faktor

---

## Integrace s existujícím drift score

Ghost code a remote drift data se integrují do stávajícího `DriftScoreCalculator`:

```kotlin
// Rozšířené váhy (konfigurovatelné)
val weights = DriftWeights(
    lastCommitAge = 0.25,      // sníženo z 0.3
    uniqueAuthors = 0.15,      // sníženo z 0.2
    changeFrequency = 0.15,    // sníženo z 0.2
    incomingReferences = 0.15, // sníženo z 0.2
    testCoverage = 0.10,       // beze změny
    ghostCodePenalty = 0.10,   // NOVÉ: penalizace pro ghost code
    remoteDrift = 0.10         // NOVÉ: penalizace pro remote drift
)
```

---

## Konfigurace — rozšíření `DriftRadarSettings`

```yaml
# .drift-radar.yaml — nové sekce
git-enhancement:
  ghost-code:
    enabled: true
    ignore-patterns:
      - "build/**"
      - "out/**"
      - ".gradle/**"
      - "*.generated.*"
  zombie-branches:
    enabled: true
    stale-threshold-days: 30
    zombie-threshold-days: 90
    base-branch: "main"        # auto-detect pokud neuvedeno
    notification-frequency: "weekly"
  remote-drift:
    enabled: true
    remote-branch: "origin/main"
    auto-fetch-interval-minutes: 30
    drift-score-weight: 0.10
```
