package dev.murmurs.flashframe.cdk.backend

import dev.murmurs.flashframe.cdk.common.Config
import software.amazon.awscdk.CfnOutput
import software.amazon.awscdk.Duration
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.lambda.*
import software.amazon.awscdk.services.lambda.Function
import software.amazon.awscdk.services.logs.RetentionDays
import software.constructs.Construct

class RestApiLambdaStack(
    scope: Construct, id: String, props: StackProps
) : Stack(scope, id, props) {

    private val lambdaFunction: Function = Function.Builder
        .create(this, "RestApiLambdaFunction")
        .timeout(Duration.minutes(5))
        .memorySize(512)
        .architecture(Architecture.X86_64)
        .runtime(Runtime.JAVA_17)
        .code(Code.fromAsset(Config.LAMBDA_CODE_PATH))
        .handler(Config.PACKAGE_NAME + "StreamLambdaHandler")
        .functionName("${Config.APP_NAME}RestApiLambda")
        .logRetention(RetentionDays.ONE_MONTH).build()

    val prodAlias: Alias

    init {

        val cfnFunction = lambdaFunction.node.defaultChild as CfnFunction
        cfnFunction.addPropertyOverride(
            "SnapStart", mapOf("ApplyOn" to "PublishedVersions")
        )

        prodAlias = Alias(
            this, "RestApiLambdaProdAlias", AliasProps.builder()
                .aliasName("prod")
                .version(lambdaFunction.currentVersion)
                .build()
        )

        CfnOutput.Builder.create(this, "${Config.APP_NAME}RestApiLambdaArnOutput")
            .exportName("${Config.APP_NAME}RestApiLambdaArn")
            .value(prodAlias.functionArn)
            .build()

    }

}
