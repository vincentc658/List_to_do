package com.library.todolist.realm

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlin.collections.ArrayList


class RealmControllerEvent : RealmController<EventModelRealm>(EventModelRealm::class.java) {

    fun insertEvent(
        event: String
    , time : Long, name : String?,  done: ()-> Unit
    ) {
        val connector = object : InterfaceTransaction {
            override fun errorTransaction(throwable: Throwable) {}

            override fun executeTransaction(realm: Realm) {
                val start = time ?: System.currentTimeMillis()
                val eventModel = realm.createObject(EventModelRealm::class.java,start)
                eventModel.note = event
                if(name!= null){
                    eventModel.name = name
                }
                realm.insertOrUpdate(eventModel)
            }

            override fun successTransaction() {
                done()
                if (Realm.compactRealm(Realm.getDefaultConfiguration()!!)) {
                    Log.d("REALM COMPACT", "SUCCESS")
                }
            }
        }
        executeDataAsync(connector)
    }
    fun getEvent(): RealmResults<EventModelRealm> =
        getDataQueryGlobal().findAll()

    fun getTimeLineEvent(): RealmResults<EventModelRealm> =
        getDataQueryGlobal().isNotNull("name").findAll()


}
