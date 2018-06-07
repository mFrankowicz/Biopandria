package com

import com.graph.*
import edu.uci.ics.jung.graph.AbstractGraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import edu.uci.ics.jung.graph.util.Pair


fun main(args: Array<String>) {

    var graphA = UndirectedSparseGraph<Node, Edge>()

    val n1 = MemoryNode()
    val n2 = ActionNode("organizar folhas")
    val n3 = ContextNode("cia brasileira de teatro")

    with (graphA) {
        addVertex(n1)
        addVertex(n2)
        addVertex(n3)
        addEdgeExt(n1,n2)
        addEdgeExt(n2,n3)
        println(getInEdges(n2))
    }

    val a = graph {
        nodes {
            action("lala")
            action("lele")
            context("lulu")
            memory()
        }
    }

    println(a.vertices)


}

@DslMarker
annotation class graphDsl

fun graph(block: GraphBuilder.() -> Unit): UndirectedSparseGraph<Node,Edge> =
        GraphBuilder().apply(block).build()

@graphDsl
class GraphBuilder {

    private var nodeList = mutableListOf<Node>()
    var nodeMap = mutableMapOf<String, Node>()
    private var edgeList = mutableListOf<Edge>()

    fun nodes(block: NodeBuilder.() -> Unit) {
        nodeList = NodeBuilder().apply(block).build()
    }

    fun edges(block: EdgeBuilder.() -> Unit) {

    }

    fun nodesMap(block: NodeBuilder.() -> Unit) {
        nodeMap = NodeBuilder().apply(block).buildMap()
    }

    fun build():  UndirectedSparseGraph<Node, Edge> {
        val g = UndirectedSparseGraph<Node, Edge>()
        nodeList.forEach {
            g.addVertex(it)
        }
        return g
    }

    fun buildMap(): UndirectedSparseGraph<Node,Edge> {
        val g = UndirectedSparseGraph<Node, Edge>()
        nodeMap.forEach { t, u ->
            g.addVertex(u)
        }
        return g
    }

}

@graphDsl
class NodeBuilder {

    private var nodes = mutableListOf<Node>()
    private var nodesMap = mutableMapOf<String, Node>()

    fun action(text: String) {
        nodes.add(ActionNode(text))
    }

    fun action(key: String, text: String) {
        nodesMap[key] = ActionNode(text)
    }

    fun context(text: String) {
        nodes.add(ContextNode(text))
    }
    fun memory() {
        nodes.add(MemoryNode())
    }

    fun build() : MutableList<Node> = nodes
    fun buildMap() : MutableMap<String, Node> = nodesMap

}

class EdgeBuilder(var nodeMap: MutableMap<String, Node>) {

    private var edgeList = mutableListOf<Edge>()

    fun relation(from: String, to: String) {
        val nodeFrom = nodeMap[from]
        val nodeTo = nodeMap[to]
        edgeList.add(GenericRelationEdge(nodeFrom!!, nodeTo!!))
    }

    fun build() : MutableList<Edge> = edgeList
}

fun AbstractGraph<Node,Edge>.addEdgeExt(v1: Node, v2: Node) : Boolean {
    val e = GenericRelationEdge(v1 , v2)
    return this.addEdge(e, Pair(v1,v2))
}

