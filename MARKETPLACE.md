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
    <li><b>Method-Level Granularity</b> — drill down from file-level to individual method drift scores</li>
    <li><b>Zombie Code Detection</b> — finds code reachable only from tests or deprecated paths</li>
    <li><b>Orphan Code Detection</b> — identifies code without any active authors (git mailmap aware)</li>
    <li><b>Safe-to-Remove Confidence Score</b> — prioritize cleanup with removal risk ratings</li>
    <li><b>Project Heatmap</b> — visual heatmap of drift across your entire codebase</li>
    <li><b>Module-Level Aggregation</b> — see which modules are healthiest and which need attention</li>
    <li><b>Trend Analysis</b> — track whether code health is improving or declining</li>
    <li><b>Cleanup Backlog</b> — prioritized list of code to remove, sorted by impact</li>
    <li><b>Multi-Module Analysis</b> — analyze drift across all modules simultaneously</li>
    <li><b>Export Reports</b> — share findings with your team</li>
    <li><b>CI Integration</b> — <code>./gradlew driftRadar --threshold=40</code> with build failure on regression</li>
    <li><b>SARIF + JUnit XML Output</b> — integrate with GitHub Code Scanning and CI reporting</li>
</ul>

<h3>Free &amp; Open Source</h3>
<p>Every feature is free for everyone — no license, no tiers. Source code: <a href="https://github.com/JirakJ/dead-code-drift-radar">GitHub</a> (Apache-2.0).</p>

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

Free & open source (Apache-2.0) — every feature is available to everyone, no license needed.

## Screenshots Needed

1. **Gutter Icons in Editor** — Java file showing 🟢🟡🔴 drift icons next to methods with hover tooltip displaying drift factors
2. **Project Heatmap** — Drift Radar tool window showing a color-coded heatmap of drift across the codebase
3. **Zombie Code Detection** — editor highlighting code reachable only from deprecated paths with safe-to-remove score
4. **Trend Analysis** — chart showing code health improving or declining over time
5. **Project View Badges** — file tree with drift-level badges visible on files and modules
