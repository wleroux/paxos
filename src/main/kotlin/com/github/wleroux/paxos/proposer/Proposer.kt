package com.github.wleroux.paxos.proposer

interface Proposer {
    suspend fun propose(proposeRequest: ProposeRequest): ProposeResponse
}