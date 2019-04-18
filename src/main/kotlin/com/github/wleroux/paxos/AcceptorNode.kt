package com.github.wleroux.paxos

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AcceptorNode(val server: Int): Acceptor {
    private var acceptedResponse: Int? = null
    private var currentProposalNumber: ProposalNumber? = null
    private val acceptorMutex = Mutex()
    override suspend fun prepare(prepareRequest: AcceptorRequest.PrepareRequest): PrepareResponse {
        return acceptorMutex.withLock {
            if (currentProposalNumber == null || prepareRequest.proposalNumber > currentProposalNumber) {
                if (acceptedResponse == null) {
                    currentProposalNumber = prepareRequest.proposalNumber
                    PrepareResponse.PromisePrepare
                } else {
                    PrepareResponse.PromiseAccepted(
                        currentProposalNumber!!,
                        acceptedResponse!!
                    )
                }
            } else {
                PrepareResponse.Reject(currentProposalNumber!!)
            }
        }
    }

    override suspend fun accept(acceptRequest: AcceptorRequest.AcceptRequest): AcceptResponse {
        return acceptorMutex.withLock<AcceptResponse> {
            return if (acceptRequest.proposalNumber >= currentProposalNumber) {
                currentProposalNumber = acceptRequest.proposalNumber
                acceptedResponse = acceptRequest.value
                AcceptResponse.Accept
            } else {
                AcceptResponse.Reject
            }
        }
    }
}