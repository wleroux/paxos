package com.github.wleroux.paxos

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.github.wleroux.paxos.AcceptorRequest.*
import com.github.wleroux.paxos.PrepareResponse.*
import kotlin.math.floor

class ProposerNode(val proposerId: Int): Proposer {
    val acceptors  = mutableListOf<Acceptor>()
    val learners = mutableListOf<Learner>()
    private val consensus get() = (floor(acceptors.size / 2f) + 1).toInt()

    private val proposerMutex = Mutex()
    override suspend fun propose(initialValue: Int): Int {
        return proposerMutex.withLock<Int> {
            var roundCount = 0
            while (true) {
                roundCount ++
                val roundIdentifier = ProposalNumber(roundCount, proposerId)

                // Prepare
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
                    ?.value ?: initialValue

                // Accept
                val agreedNodes = acceptors
                    .map { it.accept(AcceptRequest(roundIdentifier, acceptedValue)) }
                    .filter { it is AcceptResponse.Accept }
                    .size
                if (agreedNodes >= consensus) {
                    learners.forEach { it.decide(DecideRequest(acceptedValue)) }
                    return acceptedValue
                }
            }

            @Suppress("UNREACHABLE_CODE")
            return 0
        }
    }
}