package com.cloudwi.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseManager {
    fun connect() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MySQL", driver = "org.h2.Driver", user = "sa")
        org.h2.tools.Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start()
    }

    fun createTables() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(KeyValueTable)
            commit()
        }
    }
}

object KeyValueTable : Table("key_value_table") {
    val id = integer("id").autoIncrement()

    val key = varchar("key", 255)
    val value = varchar("value", 255)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}