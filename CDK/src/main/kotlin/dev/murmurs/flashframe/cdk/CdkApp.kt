package dev.murmurs.flashframe.cdk

import dev.murmurs.flashframe.cdk.backend.CognitoStack
import dev.murmurs.flashframe.cdk.backend.RestApiLambdaStack
import dev.murmurs.flashframe.cdk.backend.RestApiStack
import dev.murmurs.flashframe.cdk.backend.SesStack
import dev.murmurs.flashframe.cdk.base.DefaultRegionCertificateStack
import dev.murmurs.flashframe.cdk.base.IADCertificateStack
import dev.murmurs.flashframe.cdk.base.Route53Stack
import dev.murmurs.flashframe.cdk.common.Config
import dev.murmurs.flashframe.cdk.common.Constants
import dev.murmurs.flashframe.cdk.frontend.CloudfrontStack
import dev.murmurs.flashframe.cdk.frontend.WebAppS3Stack
import software.amazon.awscdk.App
import software.amazon.awscdk.Environment
import software.amazon.awscdk.StackProps

fun main() {
    val stackNamePrefix = "${Config.APP_NAME}-"
    val app = App()

    val defaultRegionStackProps = StackProps.builder()
        .env(
            Environment.builder()
                .account(Config.AWS_ACCOUNT)
                .region(Config.AWS_REGION)
                .build()
        )
        .crossRegionReferences(true)
        .build()

    val iadRegionStackProps = StackProps.builder()
        .env(
            Environment.builder()
                .account(Config.AWS_ACCOUNT)
                .region(Constants.AWS_REGION_IAD)
                .build()
        )
        .crossRegionReferences(true)
        .build()

    val route53Stack = Route53Stack(app, "${stackNamePrefix}Route53Stack", defaultRegionStackProps)

    val iadCertificateStack = IADCertificateStack(
        app,
        "${stackNamePrefix}IADCertificate",
        iadRegionStackProps,
        route53Stack.publicHostedZone
    )

    val defaultRegionCertificateStack = DefaultRegionCertificateStack(
        app,
        "${stackNamePrefix}DefaultRegionCertificate",
        defaultRegionStackProps,
        route53Stack.publicHostedZone
    )

    val sesStack = SesStack(
        app,
        "${stackNamePrefix}SesStack",
        defaultRegionStackProps,
        route53Stack.publicHostedZone
    )

    val webAppS3Stack = WebAppS3Stack(
        app,
        "${stackNamePrefix}WebAppS3Stack",
        defaultRegionStackProps
    )

    val cloudfrontStack = CloudfrontStack(
        app,
        "${stackNamePrefix}CloudfrontStack",
        defaultRegionStackProps,
        webAppS3Stack,
        iadCertificateStack.rootIadCertificate,
        route53Stack.publicHostedZone,
    )

    val cognitoStack = CognitoStack(
        app,
        "${stackNamePrefix}CognitoStack",
        defaultRegionStackProps,
        iadCertificateStack.authIadCertificate,
        route53Stack.publicHostedZone
    )
    cognitoStack.addDependency(sesStack)
    cognitoStack.addDependency(
        cloudfrontStack,
        "Cognito custom domain requires A record of roo domain which is created by cloudfront"
    )

    val restApiLambdaStack = RestApiLambdaStack(
        app,
        "${stackNamePrefix}RestApiLambdaStack",
        defaultRegionStackProps
    )

    val restApiStack = RestApiStack(
        app,
        "${stackNamePrefix}RestApiStack",
        defaultRegionStackProps,
        defaultRegionCertificateStack.certificate,
        route53Stack.publicHostedZone,
        restApiLambdaStack.prodAlias
    )

    app.synth()
}
