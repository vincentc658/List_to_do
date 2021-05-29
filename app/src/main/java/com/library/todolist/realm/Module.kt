package com.library.todolist.realm

import io.realm.annotations.RealmModule


@RealmModule(classes = [
    (EventModelRealm::class),
    (UserModelRealm::class)
])
class Module