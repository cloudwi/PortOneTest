package com.cloudwi.service

import com.cloudwi.database.KeyValueTable
import io.grpc.stub.StreamObserver
import io.protone.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class KeyValueServiceImpl: KeyValueServiceGrpc.KeyValueServiceImplBase() {

    override fun get(request: GetRequest, responseObserver: StreamObserver<GetResponse>) {
        val key = request.key ?: return

        GlobalScope.launch(Dispatchers.IO) {
            val value = transaction {
                KeyValueTable.select { KeyValueTable.key eq key }.firstOrNull()?.let {
                    it[KeyValueTable.value]
                }
            }

            val response = GetResponse.newBuilder()
                .setValue(value)
                .build()

            responseObserver.onNext(response)
            responseObserver.onCompleted()
        }
    }

    override fun save(request: SaveRequest, responseObserver: StreamObserver<SaveResponse>?) {
        val key = request.key ?: return
        val value = request.value ?: return

        GlobalScope.launch(Dispatchers.IO) {
            transaction {
                KeyValueTable.insert {
                    it[KeyValueTable.key] = key
                    it[KeyValueTable.value] = value
                }
            }

            val response = SaveResponse.newBuilder()
                .setKey(key)
                .setValue(value)
                .build()

            responseObserver?.onNext(response)
            responseObserver?.onCompleted()
        }
    }
}
