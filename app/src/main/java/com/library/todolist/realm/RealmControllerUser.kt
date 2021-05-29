package com.library.todolist.realm

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults

class RealmControllerUser : RealmController<UserModelRealm>(UserModelRealm::class.java) {
    fun insertChatBaseDataToRoom(
        username: String, pass: String
    ) {
        val connector = object : InterfaceTransaction {
            override fun errorTransaction(throwable: Throwable) {}

            override fun executeTransaction(realm: Realm) {
                val userModel = realm.createObject(UserModelRealm::class.java, username)
                userModel.pass= pass
                realm.insertOrUpdate(userModel)
            }

            override fun successTransaction() {
                if (Realm.compactRealm(Realm.getDefaultConfiguration()!!)) {
                    Log.d("REALM COMPACT", "SUCCESS")
                }
            }
        }
        executeDataAsync(connector)
    }
    fun getUser(username: String, pass: String): RealmResults<UserModelRealm> =
        getDataQueryGlobal().equalTo("userName", username).equalTo("pass", pass).findAll()

    fun isUserCorrect(username: String, pass: String): Boolean=
        getDataQueryGlobal().equalTo("userName", username).equalTo("pass", pass).findAll().size==1

}