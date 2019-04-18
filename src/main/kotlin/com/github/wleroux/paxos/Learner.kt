package com.github.wleroux.paxos

interface Learner {
    suspend fun decide(decideRequest: DecideRequest)
}