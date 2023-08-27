package org.ivcode.shm.utils

import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.RandomUtils
import kotlin.text.StringBuilder

/**
 * Random password generator
 *
 * @param length the length the password
 * @param enableUpper if `true` include upper-case characters
 * @param minimumUpper minimum number of upper-case characters (only applies if upper-case is enabled)
 * @param enableLower if `true` include lower-case characters
 * @param minimumLower minimum number of lower-case characters (only applies if lower-case is enabled)
 * @param enableNumber if `true` include numeric characters
 * @param minimumNumber minimum number of numeric characters (only applies if numeric is enabled)
 * @param enableSpecial if `true` include special characters
 * @param minimumSpecial minimum number of special characters (only applies if special is enabled)
 */
fun createRandomPassword(
    length: Int,
    enableUpper: Boolean,
    minimumUpper: Int?,
    enableLower: Boolean,
    minimumLower: Int?,
    enableNumber: Boolean,
    minimumNumber: Int?,
    enableSpecial: Boolean,
    minimumSpecial: Int?
): String {

    // The number of groups enabled.
    // How the number of characters generated for a group depends on if there are more group to generate
    var enabledCount = 0

    val minimumUpper = if(enableUpper) {
        enabledCount++
        findMinimumCount(
            length = length,
            groupMin = minimumUpper,
            minimumLower, minimumNumber, minimumSpecial)
    } else 0

    val minimumLower = if(enableLower) {
        enabledCount++
        findMinimumCount(
            length = length,
            groupMin = minimumLower,
            minimumUpper, minimumNumber, minimumSpecial)
    } else 0

    val minimumNumber = if(enableNumber) {
        enabledCount++
        findMinimumCount(
            length = length,
            groupMin = minimumNumber,
            minimumUpper, minimumLower, minimumSpecial)
    } else 0

    val minimumSpecial = if(enableSpecial) {
        enabledCount++
        findMinimumCount(
            length = length,
            groupMin = minimumSpecial,
            minimumUpper, minimumLower, minimumNumber)
    } else 0

    if(!enableUpper && !enableLower && !enableNumber && !enableSpecial) {
        throw IllegalArgumentException("no password characters enabled")
    }

    if(minimumUpper + minimumLower + minimumNumber + minimumSpecial > length) {
        throw IllegalArgumentException("minimums total greater than length")
    }

    val upperCount = if(enableUpper) {
        enabledCount--
        if(enabledCount>0) {
            // If there are more groups to find, then take a random portion with allowing for the defined minimums
            val mod = length - minimumLower - minimumSpecial - minimumNumber - minimumUpper
            if(mod==0) minimumUpper else (RandomUtils.nextInt() % mod) + minimumUpper
        } else {
            // If there are no more groups to find, then take the remainder
            length
        }
    } else 0

    val lowerCount = if(enableLower) {
        enabledCount--
        if(enabledCount>0) {
            // If there are more groups to find, then take a random portion with allowing for the defined minimums
            val mod = length - upperCount - minimumNumber - minimumSpecial - minimumLower
            if(mod==0) minimumLower else (RandomUtils.nextInt() % mod) + minimumLower
        } else {
            // If there are no more groups to find, then take the remainder
            length - upperCount
        }
    } else 0

    val numberCount = if(enableNumber) {
        enabledCount--
        if(enabledCount>0) {
            val mod = length - upperCount - lowerCount - minimumSpecial - minimumNumber
            if(mod==0) minimumNumber else (RandomUtils.nextInt() % mod) + minimumNumber
        } else {
            length - upperCount - lowerCount
        }
    } else 0

    val specialCount = if(enableSpecial) {
        length - upperCount - lowerCount - numberCount
    } else 0

    val sb = StringBuilder().apply {
        if (upperCount > 0) {
            append(RandomStringUtils.random(upperCount, 65, 90, true, true))
        }
        if(lowerCount > 0) {
            append(RandomStringUtils.random(lowerCount, 97, 122, true, true))
        }
        if(numberCount > 0) {
            append(RandomStringUtils.randomNumeric(numberCount))
        }
        if(specialCount > 0) {
            append(RandomStringUtils.random(specialCount, 33, 47, false, false))
        }
    }
    return sb.toMutableList()
        .apply { shuffle() }
        .stream()
        .collect(::StringBuilder, StringBuilder::append, StringBuilder::append)
        .toString()
}

/**
 * A grouping will have a minimum count of 1 if not defined but only if the other groupings doesn't sum up to the length
 */
private fun findMinimumCount(
    length: Int,
    groupMin: Int?,
    vararg groupMins: Int?
): Int {
    if(groupMin!=null) {
        return groupMin
    }

    val sum = groupMins.filterNotNull().sum()
    if(sum<length) {
        return 1
    } else {
        return 0
    }
}