package com.library.todolist.realm

import io.realm.Realm
import java.lang.Exception

interface InterfaceTransaction {
    fun executeTransaction(realm: Realm)
    fun errorTransaction(throwable : Throwable)
    fun successTransaction()
}