package nl.jacobras.vexflowcompose

import kotlin.math.absoluteValue

class Fraction(var numerator: Int = 1, var denominator: Int = 1) {

    companion object {
        val CATEGORY = Category.Fraction

        private val fractionA = Fraction()
        private val fractionB = Fraction()

        fun GCD(a: Int, b: Int): Int {
            //require(!(a.isNaN() || b.isNaN())) { "Invalid numbers: $a, $b" }

            var temp: Int
            var numA = a
            var numB = b

            while (numB != 0) {
                temp = numB
                numB = numA % numB
                numA = temp
            }

            return numA
        }

        fun LCM(a: Int, b: Int): Int {
            return (a * b) / GCD(a, b)
        }

        fun LCMM(args: List<Int>): Int {
            return when {
                args.isEmpty() -> 0
                args.size == 1 -> args[0]
                args.size == 2 -> LCM(args[0], args[1])
                else -> {
                    val first = args.first()
                    LCMM(args.drop(1)).let { lcmRest ->
                        LCM(first, lcmRest)
                    }
                }
            }
        }
    }

    init {
        simplify()
    }

    fun set(numerator: Int = 1, denominator: Int = 1): Fraction {
        this.numerator = numerator
        this.denominator = denominator
        simplify()
        return this
    }

    fun value(): Double {
        return numerator.toDouble() / denominator.toDouble()
    }

    fun simplify(): Fraction {
        var u = numerator
        var d = denominator

        val gcd = GCD(u, d)
        u /= gcd
        d /= gcd

        if (d < 0) {
            d = -d
            u = -u
        }
        return set(u, d)
    }

    fun add(param1: Fraction = Fraction(), param2: Int = 1): Fraction {
        val (otherNumerator, otherDenominator) = getNumeratorAndDenominator(param1, param2)
        val lcm = LCM(denominator, otherDenominator)
        val a = lcm / denominator
        val b = lcm / otherDenominator
        val u = numerator * a + otherNumerator * b
        return set(u, lcm)
    }

    fun subtract(param1: Fraction = Fraction(), param2: Int = 1): Fraction {
        val (otherNumerator, otherDenominator) = getNumeratorAndDenominator(param1, param2)
        val lcm = LCM(denominator, otherDenominator)
        val a = lcm / denominator
        val b = lcm / otherDenominator
        val u = numerator * a - otherNumerator * b
        return set(u, lcm)
    }

    fun multiply(param1: Fraction = Fraction(1, 1), param2: Int = 1): Fraction {
        val (otherNumerator, otherDenominator) = getNumeratorAndDenominator(param1, param2)
        return set(numerator * otherNumerator, denominator * otherDenominator)
    }

    fun divide(param1: Fraction = Fraction(1, 1), param2: Int = 1): Fraction {
        val (otherNumerator, otherDenominator) = getNumeratorAndDenominator(param1, param2)
        return set(numerator * otherDenominator, denominator * otherNumerator)
    }

    fun equals(compare: Fraction): Boolean {
        val a = fractionA.copy(compare).simplify()
        val b = fractionB.copy(this).simplify()

        return a.numerator == b.numerator && a.denominator == b.denominator
    }

    fun greaterThan(compare: Fraction): Boolean {
        val a = fractionA.copy(this)
        a.subtract(compare)
        return a.numerator > 0
    }

    fun greaterThanEquals(compare: Fraction): Boolean {
        val a = fractionA.copy(this)
        a.subtract(compare)
        return a.numerator >= 0
    }

    fun lessThan(compare: Fraction): Boolean {
        return !greaterThanEquals(compare)
    }

    fun lessThanEquals(compare: Fraction): Boolean {
        return !greaterThan(compare)
    }

    fun clone(): Fraction {
        return Fraction(numerator, denominator)
    }

    fun copy(other: Fraction): Fraction {
        return set(other.numerator, other.denominator)
    }

    fun quotient(): Int {
        return numerator / denominator
    }

    fun remainder(): Int {
        return numerator % denominator
    }

    fun makeAbs(): Fraction {
        denominator = denominator.absoluteValue
        numerator = numerator.absoluteValue
        return this
    }

    override fun toString(): String {
        return "$numerator/$denominator"
    }

    fun toSimplifiedString(): String {
        return fractionA.copy(this).simplify().toString()
    }

    fun toMixedString(): String {
        var s = ""
        val q = quotient()
        val f = fractionA.copy(this)

        if (q < 0) {
            f.makeAbs()
        }

        if (q != 0) {
            s += q

            if (f.numerator != 0) {
                s += " ${f.toSimplifiedString()}"
            }
        } else if (f.numerator == 0) {
            s = "0"
        } else {
            s = f.toSimplifiedString()
        }

        return s
    }

    fun parse(str: String): Fraction {
        val i = str.split('/')
        val n = i[0].toInt()
        val d = if (i.size > 1) i[1].toInt() else 1

        return set(n, d)
    }
}

fun getNumeratorAndDenominator(n: Fraction, d: Int = 1): Pair<Int, Int> {
    return n.numerator to n.denominator
}