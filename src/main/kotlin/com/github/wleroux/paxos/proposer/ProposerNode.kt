package com.github.wleroux.paxos.proposer

import com.github.wleroux.paxos.ProposalNumber
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.github.wleroux.paxos.acceptor.AcceptorRequest.*
import com.github.wleroux.paxos.acceptor.PrepareResponse.*
import com.github.wleroux.paxos.acceptor.AcceptResponse
import com.github.wleroux.paxos.acceptor.Acceptor
import com.github.wleroux.paxos.learner.DecideRequest
import com.github.wleroux.paxos.learner.Learner
import kotlin.math.floor

class ProposerNode(private val proposerId: Int): Proposer {
    val acceptors  = mutableListOf<Acceptor>()
    val learners = mutableListOf<Learner>()
    private val consensus get() = (floor(acceptors.size / 2f) + 1).toInt()

    private val proposerMutex = Mutex()
    private var roundCount = 0
    override suspend fun propose(proposeRequest: ProposeRequest): ProposeResponse {
        proposerMutex.withLock {
            while (!Thread.currentThread().isInterrupted) {
                roundCount ++
                val roundIdentifier = ProposalNumber(roundCount, proposerId)

                // Phase 1: Prepare
                val prepareResponses = acceptors.map { it.prepare(
                    PrepareRequest(roundIdentifier)
                ) }.toList()
                val roundHighMark = prepareResponses
                    .filter { it is Reject }.map { it as Reject }
                    .maxBy { it.proposalNumber }
                    ?.proposalNumber?.round
                if (roundHighMark != null) {
                    roundCount = roundHighMark
                    continue
                }
                val acceptedValue = prepareResponses
                    .filter { it is PromiseAccepted }.map { it as PromiseAccepted }
                    .maxBy { it.proposalNumber }
                    ?.value ?: proposeRequest.value

                // Phase 2: Accept
                val agreedNodes = acceptors
                    .map { it.accept(AcceptRequest(roundIdentifier, acceptedValue)) }
                    .filter { it is AcceptResponse.Accept }
                    .size
                if (agreedNodes >= consensus) {
                    learners.forEach { it.decide(DecideRequest(acceptedValue)) }
                    return ProposeResponse(acceptedValue)
                }
            }

            throw InterruptedException()
        }
    }
}