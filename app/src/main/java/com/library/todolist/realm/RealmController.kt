package com.library.todolist.realm

import android.util.Log
import io.realm.*


abstract class RealmController<T : RealmObject>(private val classModel: Class<T>) {
    var realm: Realm = Realm.getDefaultInstance()
    fun getRealmm(): Realm {
        if (realm.isClosed) {
            realm = Realm.getDefaultInstance()
        }
        return realm
    }

    fun getDataQuery(): RealmQuery<T> {
        return getRealmm().where(classModel)
    }

    fun getDataQueryGlobal(): RealmQuery<T> {
        return getRealmm().where(classModel)
    }

    fun executeDataAsync(connector: InterfaceTransaction) {
        getRealmm()
            .executeTransactionAsync({ bgRealm ->
                
                connector.executeTransaction(bgRealm)
            }, {
                getRealmm().refresh()
                connector.successTransaction()
            }, {
                Log.e("error transaction", "${it.printStackTrace()}")
                connector.errorTransaction(it)
            })
    }

    fun deleteAll() {
        getRealmm().beginTransaction()
        getRealmm().deleteAll()
        getRealmm().commitTransaction()
        getRealmm().refresh()
    }

    fun close() {
        try {
//            RealmManager.close()
            getRealmm().close()

            Log.d("REALM CONTROLLER IN", "COUNT ${Realm.getGlobalInstanceCount(Realm.getDefaultConfiguration()!!)}")
            Log.d("REALM CONTROLLER IN", "COUNT ${Realm.getLocalInstanceCount(Realm.getDefaultConfiguration()!!)}")
            if (Realm.compactRealm(Realm.getDefaultConfiguration()!!)) {
                Log.d("REALM COMPACT", "SUCCESS")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun getMaxIndex(): Int {
        return try {
//            val realm = RealmManager.get()
            val max = getRealmm().where(classModel).max("id")!!.toInt() + 1
//            RealmManager.close()
            max
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}
