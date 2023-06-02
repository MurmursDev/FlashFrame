package dev.murmurs.flashframe.cdk.backend

import dev.murmurs.flashframe.cdk.common.Config
import dev.murmurs.flashframe.cdk.common.Constants
import software.amazon.awscdk.*
import software.amazon.awscdk.services.certificatemanager.ICertificate
import software.amazon.awscdk.services.cognito.*
import software.amazon.awscdk.services.route53.ARecord
import software.amazon.awscdk.services.route53.PublicHostedZone
import software.amazon.awscdk.services.route53.RecordTarget
import software.amazon.awscdk.services.route53.targets.UserPoolDomainTarget
import software.constructs.Construct

class CognitoStack(
    scope: Construct, id: String, props: StackProps,
    certificate: ICertificate, publicHostedZone: PublicHostedZone
) : Stack(scope, id, props) {
    private val userPool: UserPool = UserPool(
        this, "UserPool", UserPoolProps.builder()
            .userPoolName("${Config.APP_NAME}UserPool")
            .removalPolicy(RemovalPolicy.RETAIN)
            .selfSignUpEnabled(true)
            .autoVerify(AutoVerifiedAttrs.builder().email(true).build())
            .accountRecovery(AccountRecovery.EMAIL_ONLY)
            .keepOriginal(KeepOriginalAttrs.builder().email(true).build())
            .signInAliases(SignInAliases.builder().email(true).build())
            .email(
                UserPoolEmail.withSES(
                    UserPoolSESOptions.builder()
                        .fromName(Config.APP_NAME)
                        .fromEmail(Constants.EMAIL_NO_REPLY)
                        .replyTo(Constants.EMAIL_SUPPORT)
                        .sesRegion(Constants.AWS_REGION_PDX)
                        .sesVerifiedDomain(Constants.DOMAIN_ROOT)
                        .build()
                )
            )
            .standardAttributes(
                StandardAttributes.builder()
                    .email(
                        StandardAttribute.builder()
                            .required(true)
                            .mutable(true)
                            .build()
                    )
                    .build()
            )
            .build()
    )

    private val userPoolDomain: UserPoolDomain

    private val userPoolClient: UserPoolClient

    init {
        CfnOutput.Builder.create(this, "${Config.APP_NAME}UserPoolArnOutput")
            .exportName("${Config.APP_NAME}UserPoolArn")
            .value(userPool.userPoolArn)
            .build()
        val customDomainOptions = CustomDomainOptions.builder()
            .domainName(Constants.DOMAIN_SUB_AUTH)
            .certificate(certificate)
            .build()
        val userPoolDomainOptions = UserPoolDomainOptions.builder()
            .customDomain(customDomainOptions)
            .build()
        userPoolDomain = userPool.addDomain("UserPoolDomain", userPoolDomainOptions)

        ARecord.Builder.create(this, "AuthARecord")
            .recordName("auth")
            .target(RecordTarget.fromAlias(UserPoolDomainTarget(userPoolDomain)))
            .zone(publicHostedZone)
            .build()

        val domains = listOf(Constants.URL_ROOT, Constants.URL_DEV_LOCAL)
        val callbackUrls = domains.stream().map { url: String -> "$url/callback" }.toList()
        val logoutUrls = domains.stream().map { url: String -> "$url/logout" }.toList()
        val oAuthSettings = OAuthSettings.builder()
            .scopes(listOf(OAuthScope.OPENID))
            .callbackUrls(callbackUrls)
            .logoutUrls(logoutUrls)
            .build()

        userPoolClient = UserPoolClient.Builder
            .create(this, "WebAppClient")
            .idTokenValidity(Duration.days(1))
            .accessTokenValidity(Duration.days(1))
            .oAuth(oAuthSettings)
            .userPoolClientName("${Config.APP_NAME}WebApp")
            .userPool(userPool)
            .build()
    }
}

