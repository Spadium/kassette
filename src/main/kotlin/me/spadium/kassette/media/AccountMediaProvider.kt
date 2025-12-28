package me.spadium.kassette.media

abstract class AccountMediaProvider: MediaProvider() {
    abstract var isAuthenticated: Boolean

    abstract fun initiateLogin(titleScreen: Boolean)
}