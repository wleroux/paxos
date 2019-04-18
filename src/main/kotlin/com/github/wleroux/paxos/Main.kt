package com.github.wleroux.paxos

import com.github.wleroux.paxos.acceptor.AcceptorNode
import com.github.wleroux.paxos.learner.LearnerNode
import com.github.wleroux.paxos.proposer.ProposeRequest
import com.github.wleroux.paxos.proposer.ProposerNode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main() = runBlocking {
    val proposers = (0 until 5).map { ProposerNode(it) }.toList()
    val acceptors = (0 until 3).map { AcceptorNode() }.toList()
    val learners = (0 until 3).map { LearnerNode() }.toList()
    proposers.forEach { it.acceptors.addAll(acceptors) }
    proposers.forEach { it.learners.addAll(learners) }

    proposers.map {
        GlobalScope.launch {
            val result = it.propose(ProposeRequest(Random.nextInt()))
            println("Proposer: ${result.value}")
        }
    }.forEach { it.join() }

    learners.forEach { println("Learner: ${it.decidedValue}") }
}