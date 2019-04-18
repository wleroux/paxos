package com.github.wleroux.paxos

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import com.github.wleroux.paxos.ProposalNumber

class ProposalNumberTest {

    @Test
    fun itCompares() {
        val r5s0 = ProposalNumber(5, 0)
        val r5s1 = ProposalNumber(5, 1)
        val r4s2 = ProposalNumber(4, 2)
        val r4s0 = ProposalNumber(4, 0)

        assertAll(
            { assertTrue(r5s1 > null) {"$r5s1 > null"} },
            { assertTrue(r5s1 > r4s2) {"$r5s1 > $r4s2"} },
            { assertTrue(r5s1 > r5s0) {"$r5s1 > $r5s0"} },
            { assertTrue(r5s1 > r4s0) {"$r5s1 > $r4s0"} },
            { assertTrue(r4s2 < r5s1) {"$r4s2 < $r5s1"} },
            { assertTrue(r5s0 < r5s1) {"$r5s0 < $r5s1"} },
            { assertTrue(r4s0 < r5s1) {"$r4s0 < $r5s1"} },
            { assertTrue(r5s1 == r5s1) {"$r5s1 == $r5s1"} }
        )
    }
}