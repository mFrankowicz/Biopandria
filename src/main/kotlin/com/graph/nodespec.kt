package com.graph

import arrow.instance
import arrow.typeclasses.Show
import java.util.*

interface Node {
    val uuid: UUID
    val name: String
    var edgeList: MutableList<Edge>
}

interface IndexerNode : Node {
    var indexedNodes: MutableList<UUID>
}

data class MemoryHolder(val entry: String, val information: String)

class MemoryNode : IndexerNode {

    companion object {}

    override val uuid = UUID.randomUUID()!!

    override val name = "memory-${uuid.toString()}"

    override var edgeList= mutableListOf<Edge>()

    override var indexedNodes = mutableListOf<UUID>()

    override fun toString(): String {
        return "MemoryNode $name"
    }

}

fun Node.applyEdge(edge: Edge) {
    this.edgeList.add(edge)
}

data class ActionNode(val action: String) : Node {

    override val uuid = UUID.randomUUID()!!

    override val name = "action-${uuid.toString()}"

    override var edgeList= mutableListOf<Edge>()

}

data class ContextNode(val descriptor: String) : Node {
    override val uuid = UUID.randomUUID()!!

    override val name = "context-${uuid.toString()}"

    override var edgeList= mutableListOf<Edge>()
}

interface Eff<T> {
    fun apply(effect: (input: T) -> T)
}

interface Transformable<T> {
    fun transform(input: T)
}

interface Indexable<E> {
    fun E.indexIt()
}

/*@instance(IndexerNode::class)
interface IndexerNodeIndexableInstance : Indexable<IndexerNode> {
    override fun IndexerNode.indexIt() {
        this.edgeList.forEach {
            this.indexedNodes.add(it.nodeB.uuid)
        }
    }
}*/

@instance(MemoryNode::class)
interface MemoryNodeShowInstance : Show<MemoryNode> {
    override fun MemoryNode.show(): String = this.toString()
}

//fun MemoryNode.Companion.indexer() = object : MemoryNodeIndexerInstance {}

object IndexerNodeContext : MemoryNodeShowInstance

object ForMemoryNode {
    infix fun <L> extensions (f: IndexerNodeContext.() -> L): L =
            f(IndexerNodeContext)
}

/*
@instance(MemoryNode::class)
interface MemoryNodeEffInstance : Eff<MemoryNode> {
    override fun apply(effect: (input: MemoryNode) -> MemoryNode) {

    }
}*/
