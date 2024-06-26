package com.iauth

import com.iauth.data.user.MongoUserDataSource
import com.iauth.data.user.User
import com.iauth.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@OptIn(DelicateCoroutinesApi::class)
@Suppress("unused")
fun Application.module() {

    val mongoPW = System.getenv("MONGO_PW")
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://krishna:$mongoPW@cluster0.lj3dy13.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    ).coroutine
        .getDatabase("ktor-auth")

    val userDataSource = MongoUserDataSource(db)

    GlobalScope.launch {
        val user = User(
            username = "test",
            password = "test-password",
            salt = "salt"
        )
        userDataSource.insertUser(user)
    }

    configureMonitoring()
    configureSerialization()
    configureSecurity()
    configureRouting()
}

