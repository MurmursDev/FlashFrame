package dev.murmurs.flashframe.cdk.backend

import dev.murmurs.flashframe.cdk.common.Config
import dev.murmurs.flashframe.cdk.common.Constants
import software.amazon.awscdk.Fn
import software.amazon.awscdk.IResolvable
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.apigateway.*
import software.amazon.awscdk.services.certificatemanager.ICertificate
import software.amazon.awscdk.services.iam.ServicePrincipal
import software.amazon.awscdk.services.lambda.IFunction
import software.amazon.awscdk.services.lambda.Permission
import software.amazon.awscdk.services.route53.ARecord
import software.amazon.awscdk.services.route53.PublicHostedZone
import software.amazon.awscdk.services.route53.RecordTarget
import software.amazon.awscdk.services.route53.targets.ApiGateway
import software.amazon.awscdk.services.s3.assets.Asset
import software.constructs.Construct

class RestApiStack(
    scope: Construct, id: String, props: StackProps,
    certificate: ICertificate, publicHostedZone: PublicHostedZone,
    restApiLambda: IFunction
) : Stack(scope, id, props) {


    init {
        val lambdaAPIGatewayPermission: Permission = Permission.builder()
            .principal(ServicePrincipal("apigateway.amazonaws.com"))
            .build()
        restApiLambda.addPermission("API GW Permission", lambdaAPIGatewayPermission)

        val openAPIAsset: Asset = Asset.Builder.create(this, "OpenApiAsset")
            .path(Config.API_DEFINITION_PATH).build()

        val transformMap = mapOf("Location" to openAPIAsset.s3ObjectUrl)
        // Include the OpenAPI template as part of the API Definition supplied to API Gateway
        val data: IResolvable = Fn.transform("AWS::Include", transformMap)

        val apiDefinition = ApiDefinition.fromInline(data)

        val domainNameProps = DomainNameProps.builder()
            .domainName(Constants.DOMAIN_SUB_API)
            .certificate(certificate)
            .endpointType(EndpointType.REGIONAL)
            .build()

        val restApi: SpecRestApi = SpecRestApi.Builder.create(this, "${Config.APP_NAME}RestAPIGateway")
            .disableExecuteApiEndpoint(true)
            .domainName(domainNameProps)
            .apiDefinition(apiDefinition)
            .endpointTypes(listOf(EndpointType.REGIONAL))
            .restApiName("${Config.APP_NAME}RestAPI")
            .deployOptions(
                StageOptions.builder()
                    .throttlingRateLimit(100)
                    .throttlingBurstLimit(100)
                    .stageName("dev")
                    .dataTraceEnabled(true)
                    .loggingLevel(MethodLoggingLevel.INFO)
                    .tracingEnabled(true)
                    .build()
            )
            .deploy(true)
            .build()


        ARecord.Builder
            .create(this, "APIARecord")
            .recordName("api")
            .target(RecordTarget.fromAlias(ApiGateway(restApi)))
            .zone(publicHostedZone)
            .build()
    }
}
