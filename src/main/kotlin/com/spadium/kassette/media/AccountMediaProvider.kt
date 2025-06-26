package com.spadium.kassette.media

abstract class AccountMediaProvider: MediaProvider() {
    abstract fun initiateLogin()
}