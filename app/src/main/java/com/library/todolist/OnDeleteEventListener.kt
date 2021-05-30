package com.library.todolist

interface OnDeleteEventListener {
    fun onDelete(time : Long, isTimeLineEvent : Boolean)
}