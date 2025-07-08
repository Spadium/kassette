package com.spadium.kassette.media

abstract class AccountMediaProvider: MediaProvider() {
    open var isAuthenticated: Boolean = true

    abstract fun initiateLogin()
}