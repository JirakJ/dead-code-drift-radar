# Dead Code & Drift Radar

**Find forgotten code before it becomes technical debt.**

---

## The Problem

Every codebase accumulates dead code over time. Files that nobody touches, methods that nobody calls, code whose original authors have left. This dormant code increases maintenance burden, confuses new developers, and hides potential bugs. Without visibility into code drift, teams unknowingly carry dead weight.

---

## Key Features

### Drift Score (0-100)
Every file gets a health score based on modification frequency, contributor activity, reference count, and test coverage. Higher scores mean healthier, actively maintained code.

### Visual Gutter Icons
See code health at a glance with color-coded icons: 🟢 Active (frequently modified), 🟡 Drifting (losing momentum), 🔴 Abandoned (no recent activity).

### Rich Hover Tooltips
Hover over any file to see detailed drift factors: last author, last change date, number of references, and what's driving the score.

### Project View Badges
Spot drifting files without opening them. Badges in the project explorer show drift status for every file.

### Git-Powered Analysis
Combines git history (modification frequency, last-modified dates, unique authors) with static analysis (incoming usages, call sites) for accurate scoring.

### Configurable Thresholds
Define what "drifting" and "abandoned" mean for your project. Set custom score boundaries to match your team's standards.

### Method-Level Granularity (Pro)
Go beyond file-level analysis. Get drift scores for individual methods to pinpoint exactly which code needs attention.

### Project Heatmap (Pro)
Visualize drift across your entire project with an interactive heatmap. Identify hotspots and cold zones at a glance.

---

## How It Works

1. **Install the plugin** — Available from the JetBrains Marketplace for all JetBrains IDEs.
2. **Scan your project** — Run "Tools → Drift Radar → Scan Project for Drift" to analyze all files.
3. **Review drift scores** — See gutter icons, hover tooltips, and project view badges with drift information.
4. **Take action** — Use the cleanup backlog (Pro) to prioritize which dead code to remove first.

---

## Pricing

| Feature | Free | Pro ($3.90/month) |
|---------|------|---------------------|
| Drift score (0-100) | ✅ | ✅ |
| Gutter icons | ✅ | ✅ |
| Hover tooltips | ✅ | ✅ |
| Project view badges | ✅ | ✅ |
| Git history analysis | ✅ | ✅ |
| Reference counting | ✅ | ✅ |
| Configurable thresholds | ✅ | ✅ |
| Single-module analysis | ✅ | ✅ |
| Method-level granularity | ❌ | ✅ |
| Project heatmap | ❌ | ✅ |
| Trend analysis | ❌ | ✅ |
| Cleanup backlog | ❌ | ✅ |
| Zombie code detection | ❌ | ✅ |
| Orphan code detection | ❌ | ✅ |
| Multi-module analysis | ❌ | ✅ |
| Export reports (SARIF/JUnit) | ❌ | ✅ |

---

## Testimonials

> *"We removed 15% of our codebase after running Drift Radar. All dead weight we didn't know existed."*
> — **Engineering Manager** *(placeholder)*

> *"The gutter icons make code reviews so much more informed. You immediately see what's active and what's rotting."*
> — **Senior Developer** *(placeholder)*

---

## FAQ

**Q: How is the drift score calculated?**
A: The score combines five factors: last commit age (30%), unique authors (20%), change frequency (20%), incoming references (20%), and test coverage (10%).

**Q: Does it work with monorepos?**
A: Free tier supports single-module analysis. Pro tier supports multi-module projects and monorepos.

**Q: Will it slow down my IDE?**
A: Analysis runs on demand or in the background. It doesn't interfere with your coding workflow.

**Q: What languages does it support?**
A: It works with any language since it relies on git history and IDE reference analysis rather than language-specific parsing.

**Q: Can I use it in CI/CD pipelines?**
A: Yes, Pro tier includes a Gradle task (`./gradlew driftRadar --threshold=40`) and SARIF output for GitHub Code Scanning.

---

## Get Started

Install Dead Code & Drift Radar from the JetBrains Marketplace and discover what's hiding in your codebase.

[Install from JetBrains Marketplace](#) | [View Documentation](#) | [Report Issues](#)
