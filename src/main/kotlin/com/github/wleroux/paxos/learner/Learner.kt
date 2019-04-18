package com.github.wleroux.paxos.learner

interface Learner {
    suspend fun decide(decideRequest: DecideRequest)
}