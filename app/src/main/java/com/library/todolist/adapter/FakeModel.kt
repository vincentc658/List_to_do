package com.library.todolist.adapter

import com.library.todolist.adapter.BaseRecylerViewModel

data class FakeModel(var id: Int, var text : String=""): BaseRecylerViewModel {
    override fun getIdentifier(): Int? = id
}