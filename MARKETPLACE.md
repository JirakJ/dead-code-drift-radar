# Dead Code & Drift Radar — JetBrains Marketplace Listing

## Plugin Name

Dead Code & Drift Radar

## Tagline

Find code that's silently dying — before it becomes technical debt.

## Short Description

Combines git history with static analysis to detect drifting and abandoned code. Scores every element 0–100 for health, spots zombie code and orphan code.

## Full Description

```html
<h2>Dead Code &amp; Drift Radar</h2>

<p>Your codebase has dead zones — code that's technically reachable but practically abandoned. Nobody
modifies it, nobody tests it, nobody even reads it. Traditional unused-code detectors miss it because
it's still referenced. <b>Dead Code &amp; Drift Radar</b> combines git history analysis with static
analysis to surface code that's drifting toward death, giving you a 0–100 health score for every
code element.</p>

<h3>Key Features</h3>
<ul>
    <li><b>Drift Score (0–100)</b> — health score per element based on commit age, author count, change frequency, references, and test coverage</li>
    <li><b>Gutter Icons</b> — instant visibility: 🟢 Active (70+), 🟡 Drifting (40–69), 🔴 Abandoned (0–39)</li>
    <li><b>File-Level Badges</b> — drift status shown directly in the Project View tree</li>
    <li><b>Hover Tooltips</b> — last author, last change date, reference count, and individual drift factor breakdown</li>
    <li><b>Git History Analysis</b> — modification frequency, recency, unique author count over 12 months</li>
    <li><b>Reference Analysis</b> — counts incoming usages of each code element</li>
    <li><b>Configurable Thresholds</b> — customize active, cooling, and drifting score boundaries</li>
    <li><b>Scan Actions</b> — scan entire project or current file via Tools → Drift Radar</li>
    <li><b>Method-Level Granularity (Pro)</b> — drill down from file-level to individual method drift scores</li>
    <li><b>Zombie Code Detection (Pro)</b> — finds code reachable only from tests or deprecated paths</li>
    <li><b>Orphan Code Detection (Pro)</b> — identifies code without any active authors (git mailmap aware)</li>
    <li><b>Safe-to-Remove Confidence Score (Pro)</b> — prioritize cleanup with removal risk ratings</li>
    <li><b>Project Heatmap (Pro)</b> — visual heatmap of drift across your entire codebase</li>
    <li><b>Module-Level Aggregation (Pro)</b> — see which modules are healthiest and which need attention</li>
    <li><b>Trend Analysis (Pro)</b> — track whether code health is improving or declining</li>
    <li><b>Cleanup Backlog (Pro)</b> — prioritized list of code to remove, sorted by impact</li>
    <li><b>Multi-Module Analysis (Pro)</b> — analyze drift across all modules simultaneously</li>
    <li><b>Export Reports (Pro)</b> — share findings with your team</li>
    <li><b>CI Integration (Pro)</b> — <code>./gradlew driftRadar --threshold=40</code> with build failure on regression</li>
    <li><b>SARIF + JUnit XML Output (Pro)</b> — integrate with GitHub Code Scanning and CI reporting</li>
</ul>

<h3>Free Tier</h3>
<ul>
    <li>File-level drift scores (0–100)</li>
    <li>Gutter icons and Project View badges</li>
    <li>Hover tooltips with drift factor breakdown</li>
    <li>Git analysis (modification frequency, recency, authors)</li>
    <li>Reference analysis (incoming usages)</li>
    <li>Configurable thresholds</li>
    <li>Single-module analysis</li>
</ul>

<h3>Pro Tier ($3.90/month)</h3>
<ul>
    <li>Method-level drift scores</li>
    <li>Zombie and orphan code detection</li>
    <li>Safe-to-remove confidence score</li>
    <li>Project heatmap and module aggregation</li>
    <li>Trend analysis and cleanup backlog</li>
    <li>Multi-module analysis</li>
    <li>Export reports</li>
    <li>CI integration with SARIF and JUnit XML output</li>
</ul>

<h3>Team Tier ($7.90/user/month)</h3>
<ul>
    <li>All Pro features</li>
    <li>Team-wide drift metrics and trend reports</li>
    <li>Technical debt reporting for management</li>
</ul>

<h3>Getting Started</h3>
<ol>
    <li><b>Install</b> — search "Dead Code & Drift Radar" in Settings → Plugins → Marketplace</li>
    <li><b>Scan</b> — go to Tools → Drift Radar → Scan Project for Drift</li>
    <li><b>Review</b> — check gutter icons in the editor and open the Drift Radar tool window for the full heatmap</li>
</ol>

<h3>Compatibility</h3>
<p>IntelliJ IDEA 2024.3+ and all compatible JetBrains IDEs. Supports Java, Kotlin, Python, and
JavaScript/TypeScript. Requires a Git repository and Java 21+.</p>

<h3>Part of Developer Productivity Suite</h3>
<p>Dead Code &amp; Drift Radar is part of the <b>Code Health Suite</b>, working alongside
<b>Docstring Freshness Monitor</b> for comprehensive code quality tracking.</p>
```

## Tags

`code-quality`, `dead-code`, `technical-debt`, `git`, `refactoring`

## Category

Code Quality

## Pricing

| Tier | Price | Features |
|------|-------|----------|
| Free | $0 | File-level drift scores, gutter icons, tooltips, git + reference analysis, single module |
| Pro | $3.90/month or $39/year | Method-level scores, zombie/orphan detection, heatmap, trends, CI integration, export |
| Team | $7.90/user/month | All Pro + team metrics, trend reports, management reporting |

## Screenshots Needed

1. **Gutter Icons in Editor** — Java file showing 🟢🟡🔴 drift icons next to methods with hover tooltip displaying drift factors
2. **Project Heatmap** — Drift Radar tool window showing a color-coded heatmap of drift across the codebase
3. **Zombie Code Detection** — editor highlighting code reachable only from deprecated paths with safe-to-remove score
4. **Trend Analysis** — chart showing code health improving or declining over time
5. **Project View Badges** — file tree with drift-level badges visible on files and modules
