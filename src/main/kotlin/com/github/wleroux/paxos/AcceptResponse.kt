package com.github.wleroux.paxos

sealed class AcceptResponse {
    object Accept: AcceptResponse()
    object Reject: AcceptResponse()
}