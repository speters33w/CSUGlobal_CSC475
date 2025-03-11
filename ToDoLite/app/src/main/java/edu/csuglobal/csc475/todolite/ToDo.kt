package edu.csuglobal.csc475.todolite

data class ToDo(
    var id: Long = -1,
    var task: String,
    var isCompleted: Boolean = false
)