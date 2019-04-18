package com.github.wleroux.paxos.acceptor

import com.github.wleroux.paxos.ProposalNumber


sealed class AcceptorRequest {
    data class PrepareRequest(val proposalNumber: ProposalNumber): AcceptorRequest()
    data class AcceptRequest(val proposalNumber: ProposalNumber, val value: Int): AcceptorRequest()
}