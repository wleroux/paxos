package com.github.wleroux.paxos

data class ProposalNumber(val round: Int, val server: Int): Comparable<ProposalNumber?> {
    override operator fun compareTo(other: ProposalNumber?): Int {
        return when {
            other == null -> 1
            this.round > other.round -> 1
            this.round < other.round -> -1
            this.server < other.server -> -1
            this.server > other.server -> 1
            else -> 0
        }
    }
    override fun toString() = "$round.$server"
}