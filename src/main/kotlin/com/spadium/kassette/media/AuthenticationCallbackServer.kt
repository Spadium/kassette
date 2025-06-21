package com.spadium.kassette.media

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.OutputStream
import java.net.InetSocketAddress

class AuthenticationCallbackServer {
    lateinit var server: HttpServer

    constructor() {
        server.createContext("/", FileHandler("index.html"))
        server.createContext("/callback", AuthCallbackHandler())
        server.createContext("/favicon.ico", FileHandler("favicon.ico"))
        server.executor = null
        server.start()
    }

    fun kill() {
        server.stop(0)
    }

    fun restart() {
        server = HttpServer.create(InetSocketAddress(61008), 0)
    }

    class FileHandler: HttpHandler {
        var file: String;

        constructor(file: String) {
            this.file = file
        }

        override fun handle(exchange: HttpExchange?) {
            exchange?.sendResponseHeaders(200, 0)
            val outputStream: OutputStream? = exchange?.responseBody
            outputStream?.write(javaClass.getResourceAsStream("/assets/kassette/web/$file")!!.readAllBytes())
            outputStream?.close()
        }
    }

    class AuthCallbackHandler: HttpHandler {
        override fun handle(exchange: HttpExchange?) {
            val response: String = "<h1>you're safe for now</h1>"
            exchange?.sendResponseHeaders(200, response.length.toLong())
            val outStream: OutputStream? = exchange?.responseBody
            outStream?.write(response.toByteArray())
            outStream?.close()
        }
    }
}