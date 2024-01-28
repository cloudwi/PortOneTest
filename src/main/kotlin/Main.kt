package com.cloudwi

import com.cloudwi.database.DatabaseManager
import com.cloudwi.service.KeyValueServiceImpl
import com.cloudwi.util.GrpcServer

object App {

    @JvmStatic
    fun main (args: Array<String>) {
        val databaseManager = DatabaseManager()
        val keyValueService = KeyValueServiceImpl()
        val grpcServer = GrpcServer(keyValueService)

        databaseManager.connect()
        databaseManager.createTables()

        grpcServer.start()
    }
}