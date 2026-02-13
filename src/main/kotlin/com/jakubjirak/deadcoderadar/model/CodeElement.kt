package com.jakubjirak.deadcoderadar.model

/**
 * Represents a code element (class, method, field, etc.) within a file.
 */
data class CodeElement(
    val filePath: String,
    val name: String,
    val type: ElementType,
    val lineStart: Int,
    val lineEnd: Int,
) {
    val qualifiedId: String
        get() = "$filePath:$name:${type.name}"
}
