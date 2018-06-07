package com.graph

import arrow.core.Tuple2
import java.util.*

interface Edge {
    val uuid: UUID
    var nodeA: Node
    var nodeB: Node

}

data class GenericRelationEdge(override var nodeA: Node, override var nodeB: Node) : Edge {
    override val uuid = UUID.randomUUID()!!
}