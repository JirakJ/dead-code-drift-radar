# Dead Code & Drift Radar

[![Plugin Verification](https://github.com/JirakJ/dead-code-drift-radar/actions/workflows/verify.yml/badge.svg)](https://github.com/JirakJ/dead-code-drift-radar/actions/workflows/verify.yml)
[![Version](https://img.shields.io/badge/version-2026.9.0-blue.svg)](https://github.com/JirakJ/dead-code-drift-radar/releases)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue)](LICENSE)

> Combines git history with reference analysis to find files that are drifting toward becoming dead code. Every scanned file gets a 0–100 drift score.

## ✨ Features

- **Drift score** — 0–100 per file, based on git history and incoming references
- **Drift levels** — Active, Cooling, Drifting, Dead (configurable thresholds)
- **Drift Radar tool window** — sortable table of scanned files with a level filter
- **Scan actions** — scan the whole project or just the current file
- **Ignored paths** — exclude path prefixes or globs from scanning

## 📦 Installation

### From JetBrains Marketplace
1. Open **Settings** → **Plugins** → **Marketplace**
2. Search for **"Dead Code & Drift Radar"**
3. Click **Install** and restart the IDE

### From ZIP
1. Download the latest release from [Releases](https://github.com/JirakJ/dead-code-drift-radar/releases)
2. Open **Settings** → **Plugins** → **⚙️** → **Install Plugin from Disk**
3. Select the downloaded ZIP file and restart the IDE

## 🚀 Quick Start

1. Open a project that is a git repository
2. Run **Tools → Drift Radar → Scan Project for Drift**
3. Open the **Drift Radar** tool window (bottom panel) to see all scanned files sorted by drift score
4. Use **Tools → Drift Radar → Scan Current File for Drift** for a quick single-file check

## ⚙️ Configuration

Settings are available at **Settings → Tools → Dead Code & Drift Radar**.

| Setting | Default | Description |
|---------|---------|-------------|
| Active threshold | `25` | Scores up to this value are **Active** |
| Cooling threshold | `50` | Scores up to this value are **Cooling** |
| Drifting threshold | `75` | Scores up to this value are **Drifting**; anything above is **Dead** |
| Ignored paths | *(empty)* | Path prefixes or globs (`*`, `**`, `?`), one per line or comma-separated |

### Drift Score Factors

| Factor | Weight | Description |
|--------|--------|-------------|
| Last change age | 0.35 | Days since the last commit touching the file, linear up to one year |
| Change frequency | 0.25 | Number of commits touching the file (10+ counts as fully active) |
| Incoming references | 0.25 | Resolved references from other project files (10+ counts as fully used) |
| Test coverage | 0.15 | Not measured yet — contributes a neutral 50 |

Supported file types: Java, Kotlin, Python, JavaScript and TypeScript.

## 🔌 Actions

| Action | Location | Description |
|--------|----------|-------------|
| Scan Project for Drift | Tools → Drift Radar | Analyze all project files for code drift |
| Scan Current File for Drift | Tools → Drift Radar | Analyze the current file for code drift |

## 📋 Requirements

- IntelliJ IDEA 2024.2+ (or any compatible JetBrains IDE)
- Git repository and the `git` executable on the PATH

## 🏗️ Building from Source

```bash
git clone https://github.com/JirakJ/dead-code-drift-radar.git
cd dead-code-drift-radar
./gradlew build
```

## 📝 License

Apache License 2.0 — see [LICENSE](LICENSE) and [NOTICE](NOTICE).

## 👤 Author

**Jakub Jirák** — [@JirakJ](https://github.com/JirakJ)

## Free & open source

Since version 2026.8.0 the plugin is completely free — every feature is unlocked and no license is needed. The source code is open source under the [Apache License 2.0](LICENSE).

## Contributing

Issues and pull requests are welcome at https://github.com/JirakJ/dead-code-drift-radar.
