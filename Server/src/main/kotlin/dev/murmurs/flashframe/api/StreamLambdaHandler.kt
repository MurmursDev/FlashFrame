package dev.murmurs.flashframe.api

import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class StreamLambdaHandler : RequestStreamHandler {

    @Throws(IOException::class)
    override fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
        handler.proxyStream(inputStream, outputStream, context)
    }

    companion object {
        private var handler: SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> =
            SpringBootProxyHandlerBuilder<AwsProxyRequest>()
                .defaultProxy()
                .asyncInit()
                .springBootApplication(ApiApplication::class.java)
                .buildAndInitialize()
    }
}
