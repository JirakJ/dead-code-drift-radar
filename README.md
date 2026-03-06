# Dead Code & Drift Radar

[![Build](https://github.com/JirakJ/dead-code-drift-radar/actions/workflows/build.yml/badge.svg)](https://github.com/JirakJ/dead-code-drift-radar/actions/workflows/build.yml)
[![Version](https://img.shields.io/badge/version-2026.2.2-blue.svg)](https://github.com/JirakJ/dead-code-drift-radar/releases)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

> Combines git history analysis with static analysis to identify code that's drifting toward becoming dead code. Tracks how often code is modified, referenced, and tested to produce a code health score.

## ✨ Features

- **Drift score** — 0–100 health score for every file based on git history and usage analysis
- **Gutter icons** — see drift level at a glance (🟢 Active, 🟡 Drifting, 🔴 Abandoned)
- **Hover tooltips** — last author, last change, reference count, drift factors
- **File-level badge** — drift status in Project View tree (Project View decorator)
- **Git analysis** — modification frequency, last modified date, unique authors
- **Reference analysis** — counts incoming usages of code elements
- **Configurable thresholds** — customize drift level boundaries
- **Single-module analysis**
- **Method-level granularity** — drift scores for individual methods, not just files
- **Project heatmap** — tool window with heatmap visualization of drift scores
- **Module-level aggregation** — see drift across your entire project structure
- **Trend analysis** — is drift getting better or worse over time?
- **Cleanup backlog** — prioritized list of code to remove
- **Zombie code detection** — code reachable only from tests or deprecated paths
- **Orphan code detection** — code without an active author in the organization (git mailmap aware)
- **Safe-to-remove confidence score**
- **Multi-module analysis**
- **Export reports**
- **CI integration** — CLI runner: `./gradlew driftRadar --threshold=40`
- **SARIF + JUnit XML output**

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

1. Install the plugin and restart your IDE
2. Open a project with git history
3. Go to **Tools → Drift Radar → Scan Project for Drift** to run a full analysis
4. Look at the **gutter icons** next to code elements — they show drift status
5. Open the **Drift Radar** tool window (bottom panel) for a sortable dashboard of all elements by drift score
6. Use **Tools → Drift Radar → Scan Current File for Drift** for a quick single-file analysis

## ⚙️ Configuration

Settings are available at **Settings → Tools → Dead Code & Drift Radar**.

| Setting | Default | Description |
|---------|---------|-------------|
| Enable drift analysis | `true` | Enable or disable the drift analysis engine |
| Active threshold | `25` | Score below which an element is considered "active" (healthy) |
| Cooling threshold | `50` | Score below which an element is considered "cooling" |
| Drifting threshold | `75` | Score above which an element is considered "drifting" toward dead code |
| Ignored paths | *(empty)* | Glob patterns for paths to exclude (one per line) |

### Team-shared Configuration

Create a `.drift-radar.yaml` in your repository root for shared team settings. Use `@drift-ignore` in code to suppress warnings for individual elements.

### Drift Score Factors

| Factor | Weight | Description |
|--------|--------|-------------|
| Last commit age | 0.3 | Time since the last modification |
| Unique authors (12 months) | 0.2 | Number of distinct contributors in the past year |
| Change frequency vs. module average | 0.2 | How often the file changes relative to its module |
| Incoming references | 0.2 | Number of static usages pointing to this code |
| Test coverage | 0.1 | Code coverage percentage (if available) |

## 🎯 Usage

### Editor Integration
- **Gutter icons** show drift status for code elements
- **Hover** over the icon for details: last author, last change, reference count, and individual drift factors
- **Project View decorator** shows file-level drift badges in the project tree

### Scanning
- **Full project scan**: Tools → Drift Radar → Scan Project for Drift
- **Current file scan**: Tools → Drift Radar → Scan Current File for Drift
- Results appear in the **Drift Radar** tool window

### Dashboard
- Open the **Drift Radar** tool window from the bottom panel
- Sort elements by drift score to find the most at-risk code
- Use the cleanup backlog to prioritize refactoring

## 🔌 Actions

| Action | Location | Description |
|--------|----------|-------------|
| Scan Project for Drift | Tools → Drift Radar | Analyze all project files for code drift |
| Scan Current File for Drift | Tools → Drift Radar | Analyze the current file for code drift |

## 📋 Requirements

- IntelliJ IDEA 2024.3+ (or any compatible JetBrains IDE)
- Git repository (for drift score calculation)
- Java 21+

## 🏗️ Building from Source

```bash
git clone https://github.com/JirakJ/dead-code-drift-radar.git
cd dead-code-drift-radar
./gradlew build
```

## 📝 License

MIT — [LICENSE](LICENSE)

## 👤 Author

**Jakub Jirák** — [@JirakJ](https://github.com/JirakJ)
