# Changelog

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
