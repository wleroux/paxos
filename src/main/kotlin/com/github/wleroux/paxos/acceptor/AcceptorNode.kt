package com.github.wleroux.paxos.acceptor

import com.github.wleroux.paxos.acceptor.PrepareResponse.*
import com.github.wleroux.paxos.ProposalNumber
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AcceptorNode: Acceptor {
    private var acceptedResponse: Int? = null
    private var currentProposalNumber: ProposalNumber? = null
    private val acceptorMutex = Mutex()
    override suspend fun prepare(prepareRequest: AcceptorRequest.PrepareRequest): PrepareResponse {
        return acceptorMutex.withLock {
            if (currentProposalNumber == null || prepareRequest.proposalNumber > currentProposalNumber) {
                if (acceptedResponse == null) {
                    currentProposalNumber = prepareRequest.proposalNumber
                    PromisePrepare
                } else {
                    PromiseAccepted(currentProposalNumber!!, acceptedResponse!!)
                }
            } else {
                Reject(currentProposalNumber!!)
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