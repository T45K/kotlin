fun foo() = withIntList {
    withStringSequence {
        forEach { line ->
            line.rem(1)
            line.<!UNRESOLVED_REFERENCE!>length<!>
        }
    }
}

fun withIntList(x: List<Int>.() -> Unit) {}

fun <T> withStringSequence(action: Sequence<String>.() -> T): T = TODO()
