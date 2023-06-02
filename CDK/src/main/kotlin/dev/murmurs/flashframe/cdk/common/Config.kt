package dev.murmurs.flashframe.cdk.common

class Config {
    companion object {
        const val DOMAIN = "murmurs.dev"
        const val PACKAGE_NAME = "dev.murmurs.flashframe.api."
        val AWS_ACCOUNT = System.getenv("AWS_ACCOUNT_ID") ?: throw NullPointerException()
        val AWS_REGION = System.getenv("AWS_REGION") ?: "us-west-2"
        const val APP_NAME = "FlashFrame"
        const val FRONT_END_PATH = "../Webapp/dist/flash-frame"
        const val LAMBDA_CODE_PATH = "../Server/build/distributions/FlashFrameServer.zip"
        const val API_DEFINITION_PATH = "../OpenApi/src/main/resources/api.yaml"
    }
}
