package com.github.wleroux.paxos.acceptor

import com.github.wleroux.paxos.acceptor.AcceptorRequest.*

interface Acceptor {
    suspend fun prepare(prepareRequest: PrepareRequest): PrepareResponse
    suspend fun accept(acceptRequest: AcceptRequest): AcceptResponse
}