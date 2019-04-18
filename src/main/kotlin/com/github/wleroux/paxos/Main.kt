package com.github.wleroux.paxos

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main() = runBlocking {
    val proposers = (0 until 5).map { ProposerNode(it) }.toList()
    val acceptors = (0 until 3).map { AcceptorNode(it) }.toList()
    val learners = (0 until 3).map { LearnerNode(it) }.toList()
    proposers.forEach { it.acceptors.addAll(acceptors) }
    proposers.forEach { it.learners.addAll(learners) }

    proposers.map {
        GlobalScope.launch {
            val result = it.propose(Random.nextInt())
            println("Proposer: $result")
        }
    }.forEach { it.join() }

    learners.forEach { println("Learner: ${it.decidedValue}") }
}