package com.github.wleroux.paxos.learner

class LearnerNode: Learner {
    var decidedValue: Int? = null
    override suspend fun decide(decideRequest: DecideRequest) {
        decidedValue = decideRequest.value
    }
}