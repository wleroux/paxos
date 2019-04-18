package com.github.wleroux.paxos

import com.github.wleroux.paxos.AcceptorNode
import com.github.wleroux.paxos.LearnerNode
import com.github.wleroux.paxos.ProposerNode
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class PaxosTest {
    @Test
    fun onlySingleValueReturned() = runBlocking {
        val proposers = (0 until 5).map { ProposerNode(it) }.toList()
        val acceptors = (0 until 3).map { AcceptorNode(it) }.toList()
        val learners = (0 until 3).map { LearnerNode(it) }.toList()
        proposers.forEach { it.acceptors.addAll(acceptors) }
        proposers.forEach { it.learners.addAll(learners) }

        val proposerResponses = proposers
            .map { async { it.propose(Random.nextInt()) } }
            .toList()
            .map { it.await() }
        val learnerResponses = learners.map { it.decidedValue!! }
        val expectedResponse = proposerResponses[0]
        proposerResponses.forEach {
            assertEquals(expectedResponse, it)
        }
        learnerResponses.forEach {
            assertEquals(expectedResponse, it)
        }
    }
}
