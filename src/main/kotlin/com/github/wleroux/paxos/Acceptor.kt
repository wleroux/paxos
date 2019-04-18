package com.github.wleroux.paxos

interface Acceptor {
    suspend fun prepare(prepareRequest: AcceptorRequest.PrepareRequest): PrepareResponse
    suspend fun accept(acceptRequest: AcceptorRequest.AcceptRequest): AcceptResponse
}