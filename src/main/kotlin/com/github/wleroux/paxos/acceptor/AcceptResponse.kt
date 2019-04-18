package com.github.wleroux.paxos.acceptor

sealed class AcceptResponse {
    object Accept: AcceptResponse()
    object Reject: AcceptResponse()
}