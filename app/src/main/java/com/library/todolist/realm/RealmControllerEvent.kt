package com.library.todolist.realm

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlin.collections.ArrayList


class RealmControllerEvent : RealmController<EventModelRealm>(EventModelRealm::class.java) {

    fun insertEvent(
        event: String
    , time : Long, name : String?,subNote: String?,attent : Boolean,   done: ()-> Unit
    ) {
        val connector = object : InterfaceTransaction {
            override fun errorTransaction(throwable: Throwable) {}

            override fun executeTransaction(realm: Realm) {
                val start = time ?: System.currentTimeMillis()
                val eventModel = realm.createObject(EventModelRealm::class.java,start)
                eventModel.note = event
                eventModel.attend= attent
                if(name!= null){
                    eventModel.name = name
                }
                if(subNote.isNullOrEmpty()){
                    eventModel.subNote = "-"
                }else{
                    eventModel.subNote = subNote
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
        getDataQueryGlobal().equalTo("attend", true).findAll()

    fun getTimeLineEvent(): RealmResults<EventModelRealm> =
        getDataQueryGlobal().isNotNull("name").findAll()

    fun deleteEvent(time: Long, done: ()-> Unit) {
        val connector = object : InterfaceTransaction {
            override fun errorTransaction(throwable: Throwable) {}
            override fun successTransaction() {
                done()
            }
            override fun executeTransaction(realm: Realm) {
                val event =
                    realm.where(EventModelRealm::class.java).equalTo("time", time).findFirst()
                event?.deleteFromRealm()
            }
        }
        executeDataAsync(connector)
    }
     fun updateDataEvent(
        time: Long,attend: Boolean,  done: ()-> Unit
    ) {
        val connector = object : InterfaceTransaction {
            override fun errorTransaction(throwable: Throwable) {}
            override fun successTransaction() {
                done()
            }
            override fun executeTransaction(realm: Realm) {
                val event =
                    realm.where(EventModelRealm::class.java).equalTo("time", time).findFirst()
                event?.attend= attend
                realm.insertOrUpdate(event)

            }
        }
        executeDataAsync(connector)
    }
}
