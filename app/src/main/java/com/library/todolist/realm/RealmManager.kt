package com.library.todolist.realm

import io.realm.Realm
import java.lang.Exception

object RealmManager {
    private val localRealms = ThreadLocal<Realm>()
    private var globalRealm: Realm? = null

    private fun open(): Realm {
        if (localRealms.get() != null) {
            return localRealms.get()!!
        }
        val realm = Realm.getDefaultInstance()
        localRealms.set(realm)
        return realm
    }

    private fun openGlobalRealm(): Realm {
        if (globalRealm != null) {
            return globalRealm!!
        }
        globalRealm = Realm.getDefaultInstance()
        return globalRealm!!
    }

    fun get(): Realm {
        return localRealms.get() ?: open()
    }

    fun getGlobalRealm(): Realm {
        return globalRealm ?: openGlobalRealm()

    }

    fun close() {
        try {
           if( localRealms.get() != null){
               localRealms.get()!!.close()
           }
            if (globalRealm != null) {
                globalRealm!!.close()
            }
            if (Realm.getLocalInstanceCount(Realm.getDefaultConfiguration()!!) == 0) {
                localRealms.set(null)
                globalRealm = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isExist(): Boolean = localRealms.get() != null
}