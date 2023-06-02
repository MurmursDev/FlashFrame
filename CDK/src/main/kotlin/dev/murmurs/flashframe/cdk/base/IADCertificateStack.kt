package dev.murmurs.flashframe.cdk.base

import dev.murmurs.flashframe.cdk.common.Config
import dev.murmurs.flashframe.cdk.common.Constants
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.certificatemanager.Certificate
import software.amazon.awscdk.services.certificatemanager.CertificateValidation
import software.amazon.awscdk.services.certificatemanager.ICertificate
import software.amazon.awscdk.services.route53.PublicHostedZone
import software.constructs.Construct


class IADCertificateStack(scope: Construct, id: String, props: StackProps, publicHostedZone: PublicHostedZone) :
    Stack(scope, id, props) {
    val rootIadCertificate: ICertificate
    val staticIadCertificate: ICertificate
    val authIadCertificate: ICertificate

    init {
        rootIadCertificate = Certificate.Builder.create(this, "${Config.APP_NAME}RootCertificate")
            .domainName(Constants.DOMAIN_ROOT)
            .subjectAlternativeNames(listOf(Constants.DOMAIN_SUB_WWW))
            .validation(
                CertificateValidation.fromDnsMultiZone(
                    mapOf(
                        Constants.DOMAIN_ROOT to publicHostedZone,
                        Constants.DOMAIN_SUB_WWW to publicHostedZone
                    )
                )
            )
            .build()

        staticIadCertificate = Certificate.Builder.create(this, "${Config.APP_NAME}StaticCertificate")
            .domainName(Constants.DOMAIN_SUB_STATIC)
            .validation(
                CertificateValidation.fromDnsMultiZone(
                    mapOf(
                        Constants.DOMAIN_SUB_STATIC to publicHostedZone
                    )
                )
            )
            .build()

        authIadCertificate = Certificate.Builder.create(this, "${Config.APP_NAME}AuthCertificate")
            .domainName(Constants.DOMAIN_SUB_AUTH)
            .validation(
                CertificateValidation.fromDnsMultiZone(
                    mapOf(
                        Constants.DOMAIN_SUB_AUTH to publicHostedZone
                    )
                )
            )
            .build()
    }
}
