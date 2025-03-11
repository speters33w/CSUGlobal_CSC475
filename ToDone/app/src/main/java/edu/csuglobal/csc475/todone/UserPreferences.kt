package edu.csuglobal.csc475.todone

import java.util.Calendar


data class UserPreferences(
    val normalDaysOff: List<Int> = listOf(Calendar.SATURDAY, Calendar.SUNDAY),
    val normalWorkSchedule: Pair<Int, Int> = Pair(7, 17), // 24-hour format
    val normalSleepSchedule: Pair<Int, Int> = Pair(21, 6) // 24-hour format
)