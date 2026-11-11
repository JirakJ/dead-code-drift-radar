# Changelog

## [Unreleased]

### Fixed
- Drift thresholds configured in Settings are now actually applied (levels always used the defaults).
- Ignored-path globs with `**` never matched; patterns now support `*`, `**`, `?`, path prefixes, and
  may be separated by newlines or commas as the settings hint says.
- Reference counting ran PSI without a read action, so scans silently produced no results; project
  file iteration and reference resolution now run under a read action.
- Tool window refresh and notifications now happen on the EDT; a project scan clears stale results first.
- `git log` errors are no longer parsed as history (stderr discarded, exit code checked, 30 s timeout).

- Minimum IDE raised to 2024.2 (build 242): the plugin is compiled to Java 21 bytecode, which the
  JBR 17 runtime of IDEs 2023.2–2024.1 cannot load, so the old `since-build` 232 was never usable there.

### Removed
- Non-functional "Enable drift analysis" setting.
- Description/README claims for features that never shipped (gutter icons, hover tooltips, project-view
  badges, heatmap, trend analysis, zombie/orphan detection, SARIF/JUnit export, CI task), together with
  the unused helper classes and icons behind them, and the leftover ProGuard obfuscation task.

## [2026.8.0] - 2026-11-10

### Changed
- The plugin is now completely free — all features unlocked, no license needed.
- Source code is now open source under Apache-2.0: https://github.com/JirakJ/dead-code-drift-radar

## [2026.3.6] - 2026-04-18

### Changed
- Enabled freemium licensing via `<product-descriptor optional="true">`. Plugin remains fully functional without a license; premium features may be introduced in future minor releases.


## [2026.2.5] - 2026-02-13

### Added
- Added ProGuard obfuscation for code protection


## [2026.2.4] - 2026-02-13

### Changed
- Version bump for JetBrains Marketplace publication


## [2026.2.3] - 2026-02-13

### Changed
- Expanded compatibility range: sinceBuild 232.0+ (IntelliJ 2023.2+)
- Removed upper version bound for future IDE compatibility


## [2026.2.2] - 2026-02-13

### Added
- Initial release
- Drift scoring (0–100) for every file based on git history and static analysis
- Gutter icons with color-coded drift status (🟢 Active, 🟡 Drifting, 🔴 Abandoned)
- Hover tooltips showing last author, last change, reference count, and drift factors
- File-level badge in Project View tree (Project View decorator)
- Git history analysis: modification frequency, last modified date, unique authors
- Reference analysis: incoming usage count for code elements
- Configurable drift thresholds (active: 25, cooling: 50, drifting: 75)
- Enable/disable toggle for drift analysis
- Ignored paths configuration with glob patterns
- `.drift-radar.yaml` support for team-shared configuration
- `@drift-ignore` annotation for per-element suppression
- Scan Project for Drift action (Tools → Drift Radar)
- Scan Current File for Drift action (Tools → Drift Radar)
- Drift Radar tool window with sortable dashboard
- Drift score factors: commit age (0.3), unique authors (0.2), change frequency (0.2), references (0.2), test coverage (0.1)
- Method-level drift granularity (Pro)
- Project heatmap visualization (Pro)
- Module-level aggregation (Pro)
- Trend analysis (Pro)
- Cleanup backlog — prioritized removal list (Pro)
- Zombie code detection (Pro)
- Orphan code detection with git mailmap support (Pro)
- Safe-to-remove confidence score (Pro)
- Multi-module analysis (Pro)
- CI integration with CLI runner (Pro)
- SARIF + JUnit XML output (Pro)
