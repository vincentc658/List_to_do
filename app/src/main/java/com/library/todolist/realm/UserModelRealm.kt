package com.library.todolist.realm

import com.library.todolist.adapter.BaseRecylerViewModel
import com.library.todolist.adapter.GenericAdapter
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserModelRealm : RealmObject() {
    @PrimaryKey
    var userName: String ? = null
    var pass: String? = null
}