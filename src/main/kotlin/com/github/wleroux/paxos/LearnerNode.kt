package com.github.wleroux.paxos

class LearnerNode(val learnerId: Int): Learner {
    var decidedValue: Int? = null
    override suspend fun decide(decideRequest: DecideRequest): Unit {
        decidedValue = decideRequest.value
    }
}