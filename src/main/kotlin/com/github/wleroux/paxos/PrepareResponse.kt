package com.github.wleroux.paxos

sealed class PrepareResponse {
    object PromisePrepare: PrepareResponse()
    data class PromiseAccepted(val proposalNumber: ProposalNumber, val value: Int): PrepareResponse()
    data class Reject(val proposalNumber: ProposalNumber): PrepareResponse()
}