package com.github.wleroux.paxos

data class ProposalNumber(val round: Int, val identifier: Int): Comparable<ProposalNumber?> {
    override operator fun compareTo(other: ProposalNumber?): Int {
        return when {
            other == null -> 1
            this.round > other.round -> 1
            this.round < other.round -> -1
            this.identifier < other.identifier -> -1
            this.identifier > other.identifier -> 1
            else -> 0
        }
    }
    override fun toString() = "$round.$identifier"
}