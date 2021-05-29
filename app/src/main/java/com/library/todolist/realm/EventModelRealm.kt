package com.library.todolist.realm

import com.library.todolist.adapter.BaseRecylerViewModel
import com.library.todolist.adapter.GenericAdapter
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class EventModelRealm : RealmObject(), BaseRecylerViewModel {
    @PrimaryKey
    var time: Long? = null
    var note: String ? = null
    var name: String? = null
    override fun getIdentifier(): Int? = GenericAdapter.ITEM
}