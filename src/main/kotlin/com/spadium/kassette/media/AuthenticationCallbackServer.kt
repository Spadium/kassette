package com.spadium.kassette.media

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.OutputStream
import java.net.InetSocketAddress

class AuthenticationCallbackServer {
    var server: HttpServer;

    constructor() {
        server = HttpServer.create(InetSocketAddress(61008), 0)
        server.createContext("/callback", AuthCallbackHandler())
        server.createContext("/favicon.ico", FaviconHandler())
        server.executor = null
        server.start()
    }

    class FaviconHandler: HttpHandler {
        override fun handle(exchange: HttpExchange?) {
            exchange?.sendResponseHeaders(200, 0)
            val outputStream: OutputStream? = exchange?.responseBody
            outputStream?.write(javaClass.getResourceAsStream("/assets/kassette/favicon.ico")!!.readAllBytes())
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