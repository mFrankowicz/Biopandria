package com.run

import processing.core.PApplet
import java.util.*

fun main(args: Array<String>) {
    PApplet.main(Main::class.java)

    /*
    //aplicativesK
    val a = ForSequenceK extensions {
        sequenceOf(1,2,3).k()
                .ap(sequenceOf({x: Int -> x + 1}, {x: Int -> x * 2}).k())
                .toList()
    }

    println(a)

    //SemigroupK
    val hello = sequenceOf('h','e','l','l','o').k()
    val commanSpace = sequenceOf(',', ' ').k()
    val world = sequenceOf('w', 'o', 'r', 'l','d').k()

    val helloWorld = hello.combineK(commanSpace.combineK(world)).toList()
    val helloWorld2 = hello.combineK(commanSpace).combineK(world).toList()

    println(helloWorld2)

    //functor
    val fibonacci = generateSequence(0 to 1) { it.second to it.first + it.second}.map { it.first }.k()
    val fibResult = fibonacci.map { it * 2 }.takeWhile { it < 10 }.toList()

    println(fibResult)
    */


}

class Main : PApplet() {

    val populationFactory = PopulationFactory(500)

    override fun settings() {
        size(700,500)
    }

    override fun setup() {


    }

    override fun draw() {
        background(255)
        //Thread.sleep(100)
        populationFactory.runGA()
    }
}

data class DNA(var target: String,
               var fitness: Double = 0.0,
               var genes: MutableList<Char> = mutableListOf<Char>()
               ) {

    private val numChar = target.length

    init {
        for(c in 0 until numChar) {
            genes.add(c, (32..128).random().toChar())
        }
    }

    fun makeGenes() {

    }

    fun fitness() {
        var score = 0
        for(i in 0 until numChar) {
            if(genes[i] == target[i]) {
                score++
            }
        }

        fitness = score.toDouble()/target.length
    }


    fun crossover(partner: DNA) : DNA {
        var child = DNA(target)
        val midpoint = (0 until genes.size).random()
        for(i in 0 until genes.size) {
            when {
                i > midpoint -> child.genes[i] = genes[i]
                else -> child.genes[i] = partner.genes[i]
            }
        }
        return child
    }

    fun mutate(rate: Double) {
        for(i in 0 until genes.size) {
            if(Random().nextDouble() < rate) {
                genes[i] = (32..128).random().toChar()
            }
        }
    }

    fun phenotype() : String {
        return genes.toString()
    }
}

class PopulationFactory(number: Int) {

    val population = mutableListOf<DNA>()
    val mutationRate = 0.01
    var matingPool = mutableListOf<DNA>()
    val target = "to be or not to be"

    init {
        for(i in 0 until number) {
            population.add(DNA(target))
        }
    }

    fun runGA(){

        population.forEach {
            it.fitness()
            println(it.toString())
        }

        matingPool = mutableListOf<DNA>()

        for(i in 0 until population.size) {
            val n = (population[i].fitness * 100).toInt()
            for(j in 0 until n) {
                matingPool.add(population[i])
            }

        }

        for(i in 0 until population.size) {

            val a = (0..matingPool.size).random()
            val b = (0..matingPool.size).random()

            val partnerA = matingPool[a]
            val partnerB = matingPool[b]

            val child = partnerA.crossover(partnerB)
            child.mutate(mutationRate)

            //println(child.phenotype())

            population[i] = child

        }

        /*val p = population.filter { it.fitness >= 0.0 }.toList()
        p.forEach {
            println(it.toString())
        }*/
    }


}

fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) + start

