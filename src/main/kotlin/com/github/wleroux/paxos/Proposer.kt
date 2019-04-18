package com.github.wleroux.paxos

interface Proposer {
    suspend fun propose(initialValue: Int): Int
}