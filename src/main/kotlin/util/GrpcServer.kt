package com.cloudwi.util

import com.cloudwi.service.KeyValueServiceImpl
import io.grpc.ServerBuilder

class GrpcServer(
    private val service: KeyValueServiceImpl
) {
    private val server = ServerBuilder.forPort(50051)
        .addService(service)
        .build()

    fun start() {
        server.start()
        Runtime.getRuntime().addShutdownHook(Thread {
            server.shutdown()
        })
        server.awaitTermination()
    }
}
