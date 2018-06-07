package com

import arrow.Kind
import arrow.core.ForOption
import arrow.core.Option
import arrow.core.fix
import arrow.instance
import arrow.instances.OptionSemigroupInstance
import arrow.instances.semigroup
import arrow.typeclasses.Monoid
import arrow.typeclasses.Semigroup
import arrow.typeclasses.Show
import com.run.random

interface  IDNA <T> {

    var genes: MutableList<T>

    fun fitness()

    fun crossover(partner: IDNA<T>) : IDNA<T>

    fun mutate(rate: Double)

}

class ADNA <T> () : IDNA<T> {

    override var genes = mutableListOf<T>()

    override fun fitness() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*init {
        genes
    }*/

    override fun crossover(partner: IDNA<T>): IDNA<T> {
        var child = ADNA<T>()
        val midpoint = (0 until genes.size).random()
        for(i in 0 until genes.size) {
            when {
                i > midpoint -> child.genes[i] = genes[i]
                else -> child.genes[i] = partner.genes[i]
            }
        }
        return child
    }

    fun mcrossover(partnersPool: MutableList<ADNA<T>>) {
        val a = mutableListOf(ADNA<T>().crossover(this))
        partnersPool.forEach {
            a.add(this)
        }
    }

    override fun mutate(rate: Double) {

    }

    fun  phenotype(): List<T> {
        return genes.toList()
    }

}

class Field {

}

@instance(Constant::class)
interface ConstantSemigroupInstance: Semigroup<Constant> {
    override fun Constant.combine(b: Constant): Constant {
        val c = Constant()
        c.e = this.e + b.e
        return c
    }
}

@instance(Constant::class)
interface ConstantMonoidInstance: Monoid<Constant>, ConstantSemigroupInstance {
    override fun empty(): Constant {
        val c = Constant()
        c.e = 0.0
        return c
    }
}

@instance(Constant::class)
interface ConstantShowInstance : Show<Constant> {
    override fun Constant.show(): String = this.e.toString()
}
///
fun Constant.Companion.semigroup() : Semigroup<Constant> =
    object : ConstantSemigroupInstance {}

fun Constant.Companion.moinoid() : Monoid<Constant> =
    object : ConstantMonoidInstance {}

///
object ConstantContext : ConstantMonoidInstance, ConstantShowInstance

object ForConstant {
    infix fun <L> extensions (f: ConstantContext.() -> L): L =
            f(ConstantContext)
}

class Constant {

    var e = 0.01

    companion object {
        val ZERO = 0.01
    }


}

fun main(args: Array<String>) {
    val a = Constant()
    val b = Constant()

    ForConstant extensions {

        val c = combineAll(a,b,empty())
        println(c.show())

    }


}