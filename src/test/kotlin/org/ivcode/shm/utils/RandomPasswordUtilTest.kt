package org.ivcode.shm.utils

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RandomPasswordUtilTest {
    @ParameterizedTest
    @CsvSource(
        "16, 1, 1, 1, 1",
        "8, 2, 2, 2, 2"
    )
    fun `Test Character Length`(
        length: Int,
        minimumUpper: Int,
        minimumLower: Int,
        minimumNumber: Int,
        minimumSpecial: Int
    ) {
        val password = createRandomPassword(
            length = length,
            enableUpper = true,
            minimumUpper = minimumUpper,
            enableLower = true,
            minimumLower = minimumLower,
            enableNumber = true,
            minimumNumber = minimumNumber,
            enableSpecial = true,
            minimumSpecial = minimumSpecial
        )

        assertEquals(length, password.length)
    }

    @ParameterizedTest
    @CsvSource(
        "16, true, 1",
        "8, true, 2",
        "4, true, 4",
        "16, false, 4"
    )
    fun `Test Number Character Count`(
        length: Int,
        enableNumber: Boolean,
        minimumNumber: Int
    ) {
        val password = createRandomPassword(
            length = length,
            enableUpper = true,
            minimumUpper = null,
            enableLower = true,
            minimumLower = null,
            enableSpecial = true,
            minimumSpecial = null,
            enableNumber = enableNumber,
            minimumNumber = minimumNumber
        )

        val count = password.count { ch -> ch.isDigit() }
        assertTrue("expected value (${minimumNumber}) not less than or equal to the actual value (${count})") {
            if(enableNumber) { minimumNumber <= count } else {count == 0}
        }
    }
}