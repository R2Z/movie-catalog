package org.lt.imdb.moviecatalog.util

import org.lt.imdb.moviecatalog.model.MovieCatalog._

import scala.collection.immutable.Queue
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.hashing.MurmurHash3

object GraphUtils {

  object NodeType extends Enumeration {
    val Movie, Crew = Value
  }

  class NodeValue(val id: String, val value: String, val nodeType: NodeType.Value) {

    override def equals(that: Any): Boolean = {
      that match {
        case nodeValue: NodeValue => this.id == nodeValue.id && this.value == nodeValue.value && this.nodeType == nodeValue.nodeType
        case _ => false
      }
    }

    override def hashCode(): Int = {
      MurmurHash3.stringHash(this.id ++ this.value)
    }

    override def toString = s"[$id, $value, $nodeType]"
  }

  object NodeValue {
    def apply(id: String, value: String, nodeType: NodeType.Value) = new NodeValue(id, value, nodeType)
  }

  case class Node(nodeValue: NodeValue, parent: Option[Node] = None)

  case class Graph(graph: Map[NodeValue, List[Node]] = Map.empty)

  case class BFSRequestParams(queue: Queue[Node], currNode: Node, visited: Set[NodeValue] = Set.empty, found: Boolean = false)


  def buildGraph(movieAndCrewDetails: Iterable[MovieCrewDetail]): Graph = {
    val graphMap = movieAndCrewDetails.foldLeft[Map[NodeValue, List[Node]]](Map.empty) { (graph, detail) =>
      val movieNode = NodeValue(detail.movieId, detail.movieName, NodeType.Movie)
      val actorNode = NodeValue(detail.actorId, detail.actorName, NodeType.Crew)
      val actorEdges = graph.getOrElse(movieNode, List.empty)
      val movieEdges = graph.getOrElse(actorNode, List.empty)
      // add Movie Node and Actor Edges
      val graph1 = graph ++ Map(movieNode -> (actorEdges ++ List(Node(actorNode))))
      // add Actor Node and Movie Edges
      val graph2 = graph1 ++ Map(actorNode -> (movieEdges ++ List(Node(movieNode))))
      graph2
    }
    Graph(graphMap)
  }

  def breadthFirstSearch(graph: Map[NodeValue, List[Node]], firstName: String, secondName: String): List[List[String]] = {

    def getNodes(name: String): Iterable[NodeValue] = graph.filter(_._1.value == name).map(_._1)

    val startNodes = getNodes(firstName)
    val endNodes = getNodes(secondName)
    val allCombinations = for (s <- startNodes; e <- endNodes) yield (Node(s), Node(e))
    val answer = allCombinations.foldLeft(List.empty[List[String]]) {
      (acc, comb) =>
        val res = acc ++ List(BFS(graph, comb._1, comb._2))
        res
    }
    answer.filter(_.nonEmpty)
  }

  private def BFS(graph: Map[NodeValue, List[Node]], startNode: Node, endNode: Node): List[String] = {
    val request = BFSRequestParams(Queue(startNode), startNode)

    val result = Iterator.iterate(request) { req =>
      val (currNode, remainingNodesInQue) = req.queue.dequeue
      val unVisitedNeighbours = graph.getOrElse(currNode.nodeValue, List.empty[Node]).filterNot(node => req.visited.contains(node.nodeValue)).map(node => node.copy(parent = Some(currNode)))
      val newQue = remainingNodesInQue.enqueue(unVisitedNeighbours.toList)
      val newVisited = req.visited ++ Set(currNode.nodeValue)
      BFSRequestParams(newQue, currNode, newVisited, currNode.nodeValue == endNode.nodeValue)
    }.find(res => res.queue.isEmpty || res.found).
      getOrElse(BFSRequestParams(Queue.empty, startNode))

    if (result.found)
      buildPath(result.currNode)
    else List.empty

  }

  private def buildPath(endNode: Node): List[String] = {

    def addPath(node: Option[Node]): String = node match {
      case None => ""
      case _ => s"${node.get.nodeValue.nodeType} : ${node.get.nodeValue.value}"
    }

    val path = ListBuffer.empty[String]

    if (endNode.parent.nonEmpty) {
      path += addPath(Some(endNode))
      var next = endNode.parent
      while (next.nonEmpty) {
        path += addPath(next)
        next = next.get.parent
      }
    }
    path.toList.reverse
  }
}
