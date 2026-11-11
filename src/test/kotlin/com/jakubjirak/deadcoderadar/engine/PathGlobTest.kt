package com.jakubjirak.deadcoderadar.engine

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PathGlobTest {

    @Test
    fun `plain prefix ignores everything below it`() {
        assertTrue(PathGlob.isIgnored("build/generated/A.kt", listOf("build/")))
        assertFalse(PathGlob.isIgnored("src/build/A.kt", listOf("build/")))
    }

    @Test
    fun `single star stays within one segment`() {
        assertTrue(PathGlob.matches("src/*.kt", "src/A.kt"))
        assertFalse(PathGlob.matches("src/*.kt", "src/sub/A.kt"))
    }

    @Test
    fun `double star directory matches zero or more directories`() {
        assertTrue(PathGlob.matches("**/test/**", "test/A.kt"))
        assertTrue(PathGlob.matches("**/test/**", "module/src/test/kotlin/A.kt"))
        assertFalse(PathGlob.matches("**/test/**", "module/src/main/A.kt"))
    }

    @Test
    fun `trailing double star crosses segments`() {
        assertTrue(PathGlob.matches("gen/**", "gen/a/b/C.java"))
    }

    @Test
    fun `dots and regex metacharacters are literal`() {
        assertTrue(PathGlob.matches("*.generated.kt", "Foo.generated.kt"))
        assertFalse(PathGlob.matches("*.generated.kt", "FooXgeneratedXkt"))
        assertTrue(PathGlob.matches("a+b(c)/[x].kt", "a+b(c)/[x].kt"))
    }

    @Test
    fun `question mark matches one non-separator char`() {
        assertTrue(PathGlob.matches("v?.kt", "v1.kt"))
        assertFalse(PathGlob.matches("v?.kt", "v12.kt"))
        assertFalse(PathGlob.matches("a?b", "a/b"))
    }

    @Test
    fun `empty pattern list ignores nothing`() {
        assertFalse(PathGlob.isIgnored("src/A.kt", emptyList()))
    }
}
