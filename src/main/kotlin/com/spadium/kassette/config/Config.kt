package com.spadium.kassette.config

interface Config<T> {
    interface ConfigCompanion<T> where T : Config<T>{
        var Instance: T
    }

    fun validate()
}