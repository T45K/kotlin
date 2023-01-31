// FIR_IDENTICAL
// WITH_STDLIB
// FIR_DUMP

// FILE: First.kt

package sample.pack

@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
@kotlin.internal.HidesMembers
fun A.forEach() = "::A.forEach"

class A {
    fun B.forEach() = "A::B.forEach"
}

class B

// FILE: Second.kt

package sample

import sample.pack.*

fun box() {
    return with(A()) {
        with(B()) {
            // K1 resolves to A::B.check
            // K2 - to ::A.check
            forEach()
        }
    }
}
