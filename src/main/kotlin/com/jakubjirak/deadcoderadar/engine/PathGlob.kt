package com.jakubjirak.deadcoderadar.engine

/**
 * Matches project-relative paths against user-supplied ignore patterns.
 *
 * A pattern matches when it is a plain path prefix (`build/`, `generated`) or a glob where
 * `*` matches within one path segment, `**` matches across segments, a `**` directory segment
 * also matches zero directories, and `?` matches a single non-separator character.
 */
internal object PathGlob {

    fun isIgnored(path: String, patterns: List<String>): Boolean =
        patterns.any { pattern -> path.startsWith(pattern) || matches(pattern, path) }

    fun matches(pattern: String, path: String): Boolean = toRegex(pattern).matches(path)

    fun toRegex(pattern: String): Regex {
        val sb = StringBuilder("^")
        var i = 0
        while (i < pattern.length) {
            val c = pattern[i]
            i += when {
                pattern.startsWith("**/", i) -> {
                    sb.append("(?:.*/)?")
                    DOUBLE_STAR_SLASH
                }
                pattern.startsWith("**", i) -> {
                    sb.append(".*")
                    DOUBLE_STAR
                }
                else -> {
                    sb.append(
                        when (c) {
                            '*' -> "[^/]*"
                            '?' -> "[^/]"
                            else -> Regex.escape(c.toString())
                        }
                    )
                    1
                }
            }
        }
        return Regex(sb.append('$').toString())
    }

    private const val DOUBLE_STAR = 2
    private const val DOUBLE_STAR_SLASH = 3
}
