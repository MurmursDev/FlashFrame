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


class DefaultRegionCertificateStack(
    scope: Construct,
    id: String,
    props: StackProps,
    publicHostedZone: PublicHostedZone
) : Stack(scope, id, props) {
    val certificate: ICertificate = Certificate.Builder.create(this, "${Config.APP_NAME}WildcardCertificate")
        .domainName(Constants.DOMAIN_ROOT)
        .subjectAlternativeNames(
            listOf(
                Constants.DOMAIN_SUB_API,
                Constants.DOMAIN_SUB_WWW,
                Constants.DOMAIN_SUB_AUTH,
            )
        )
        .validation(
            CertificateValidation.fromDnsMultiZone(
                mapOf(
                    Constants.DOMAIN_ROOT to publicHostedZone,
                    Constants.DOMAIN_SUB_WWW to publicHostedZone,
                    Constants.DOMAIN_SUB_API to publicHostedZone,
                    Constants.DOMAIN_SUB_AUTH to publicHostedZone
                )
            )
        )
        .build()
}
