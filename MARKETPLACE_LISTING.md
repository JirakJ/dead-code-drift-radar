# Dead Code & Drift Radar — JetBrains Marketplace Listing

---

## 📝 Plugin Information

| Field            | Value                                |
|------------------|--------------------------------------|
| **Name**         | Dead Code & Drift Radar              |
| **Plugin ID**    | com.jakubjirak.deadcoderadar         |
| **Version**      | 2026.2.9                             |
| **Category**     | Code Quality                         |
| **Tags**         | Dead Code, Code Health, Technical Debt, Git Analysis, Code Cleanup |

---

## 🎯 Short Description

> Detect dead and drifting code using git history and usage analysis. Drift scores, gutter icons, heatmaps, and cleanup backlogs help you find and safely remove abandoned code.

_(168 characters)_

---

## 📖 Full Description

### 🧹 Find the Code Nobody Touches — Before It Becomes a Liability

**The Problem:** Every codebase accumulates dead code — methods nobody calls, classes nobody maintains, modules that quietly rot. This code increases complexity, slows onboarding, and hides bugs. But identifying what's truly dead vs. rarely-used is hard without data.

**The Solution:** Dead Code & Drift Radar combines git history analysis with reference counting to assign every code element a **Drift Score (0–100)**. It surfaces abandoned, orphaned, and zombie code with visual indicators right in your editor — so you can clean up with confidence.

---

### ✨ Key Features

#### Spot Drifting Code Instantly

- 📊 **Drift Score (0–100)** — Health score combining git history, modification frequency, author activity, and reference counts
- 🚦 **Gutter Icons** — Color-coded status at a glance: 🟢 Active, 🟡 Drifting, 🔴 Abandoned
- 💬 **Hover Tooltips** — Detailed breakdown: last author, last change date, reference count, drift factors
- 📁 **File-Level Badges** — Drift status visible in the Project View tree
- 🔍 **Git Analysis** — Tracks modification frequency, last modified date, and unique authors
- 🔗 **Reference Analysis** — Counts incoming usages of each code element
- ⚙️ **Configurable Thresholds** — Customize what counts as active, cooling, drifting, or abandoned
- 📦 **Single-Module Analysis** — Works out of the box for single-module projects

#### Advanced Features

- 🔬 **Method-Level Granularity** — Individual drift scores for every method, not just files
- 🗺️ **Project Heatmap** — Visual heatmap in a dedicated tool window showing hotspots and cold zones
- 📦 **Module-Level Aggregation** — Roll up drift scores across multi-module projects
- 📈 **Trend Analysis** — Track drift trajectory over time to catch deterioration early
- 📋 **Cleanup Backlog** — Prioritized removal list ranked by impact and safety
- 🧟 **Zombie Code Detection** — Finds code reachable only from tests or deprecated paths
- 👻 **Orphan Code Detection** — Identifies code without an active author (git mailmap aware)
- 🛡️ **Safe-to-Remove Confidence** — Quantified safety score for each removal candidate
- 🏗️ **Multi-Module Analysis** — Full support for complex multi-module projects
- 📤 **Export Reports** — Generate reports for team review and documentation
- 🔧 **CI Integration** — CLI runner: `./gradlew driftRadar --threshold=40`
- 📄 **SARIF + JUnit XML Output** — Integrate with GitHub Code Scanning and CI dashboards

---

### 💰 Pricing

Free & open source (Apache-2.0) — every feature is available to everyone, no license needed.

---

### 📊 Drift Score Breakdown

| Factor                              | Weight |
|--------------------------------------|--------|
| Last commit age                      | 0.30   |
| Unique authors (12 months)           | 0.20   |
| Change frequency vs. module average  | 0.20   |
| Incoming references                  | 0.20   |
| Test coverage                        | 0.10   |

---

### 🎯 Perfect For

- **Tech leads** managing technical debt and planning cleanup sprints
- **Teams** preparing for large refactors or module extractions
- **Developers** inheriting unfamiliar codebases who need to know what's safe to touch
- **Engineering managers** tracking code health metrics over time

---

### ⚙️ Requirements

| Requirement        | Version                          |
|---------------------|----------------------------------|
| **JetBrains IDE**  | 2024.3+ (build 232+)            |
| **Java**           | 21+                             |
| **Git**            | Required for drift score calculation |

---

### 🚀 Quick Start

1. **Install** — Search "Dead Code & Drift Radar" in `Settings → Plugins → Marketplace`
2. **Scan** — Go to `Tools → Drift Radar → Scan Project for Drift`
3. **Explore** — Look for gutter icons (🟢🟡🔴) in your editor and file badges in Project View
4. **Hover** — Mouse over gutter icons for detailed drift factor breakdowns
5. **Configure** — Adjust thresholds in `Settings → Tools → Dead Code & Drift Radar`

---

## 📸 Screenshots & Media

| #  | Screenshot Description                                                                 |
|----|----------------------------------------------------------------------------------------|
| 1  | **Gutter icons in editor** — Shows 🟢 Active, 🟡 Drifting, and 🔴 Abandoned indicators next to code elements |
| 2  | **Hover tooltip detail** — Expanded tooltip showing last author, last change, reference count, and drift factor weights |
| 3  | **Project View file badges** — Drift status badges visible on files and directories in the tree |
| 4  | **Project heatmap tool window** — Visual heatmap showing hotspots (active) and cold zones (abandoned) across the project |
| 5  | **Cleanup backlog panel** — Prioritized list of removal candidates with safe-to-remove confidence scores |
| 6  | **Trend analysis chart** — Graph showing drift trajectory over time for selected code elements |
| 7  | **Settings panel** — Configurable thresholds for active/cooling/drifting/abandoned boundaries |
| 8  | **CI integration output** — Terminal showing `./gradlew driftRadar` output with SARIF results |

---

## 🏷️ Marketplace Categories

| Type          | Category                |
|---------------|-------------------------|
| **Primary**   | Code Quality            |
| **Secondary** | Inspection / Static Analysis |

---

## 🔍 Keywords (SEO)

`dead code`, `unused code`, `code cleanup`, `drift score`, `code health`, `technical debt`, `orphan code`, `zombie code`, `git analysis`, `code removal`, `refactoring`, `static analysis`, `code maintenance`, `heatmap`, `code quality`, `safe delete`

---

## 📋 Compatibility

### Compatible JetBrains IDEs

| IDE                        | Compatible |
|----------------------------|:----------:|
| IntelliJ IDEA Community    | ✅          |
| IntelliJ IDEA Ultimate     | ✅          |
| PyCharm                    | ✅          |
| WebStorm                   | ✅          |
| GoLand                     | ✅          |
| PhpStorm                   | ✅          |
| Rider                      | ✅          |
| CLion                      | ✅          |
| RubyMine                   | ✅          |
| Android Studio             | ✅          |

**Minimum IDE Version:** 2024.3 (build 232)

---

## 📞 Vendor Information

| Field        | Value                              |
|--------------|------------------------------------|
| **Vendor**   | Jakub Jirak                        |
| **Email**    | jirak@jakubjirak.com               |
| **Website**  | https://jakubjirak.com             |
| **Support**  | support@jakubjirak.com             |

---

## ✅ Pre-Launch Checklist

- [ ] Plugin icon meets JetBrains Marketplace guidelines (SVG, 40x40 & 80x80)
- [ ] Short description is under 255 characters
- [ ] Full description renders correctly in Marketplace preview
- [ ] Screenshots are high-resolution (1280×800 or higher), PNG format
- [ ] Plugin ID matches `gradle.properties` and `plugin.xml`
- [ ] `plugin.xml` vendor email and URL are correct
- [ ] Compatibility range tested on minimum supported IDE version (2024.3)
- [ ] Plugin verified with Plugin Verifier (`runPluginVerifier` Gradle task)
- [ ] CHANGELOG.md is up to date for the release version
- [ ] Support email is monitored and responsive
- [ ] Privacy policy URL is set
- [ ] Plugin signed with JetBrains Marketplace certificate
