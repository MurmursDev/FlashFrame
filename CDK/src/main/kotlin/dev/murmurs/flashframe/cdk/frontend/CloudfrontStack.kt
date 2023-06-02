package dev.murmurs.flashframe.cdk.frontend

import dev.murmurs.flashframe.cdk.common.Constants
import software.amazon.awscdk.Duration
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.certificatemanager.ICertificate
import software.amazon.awscdk.services.cloudfront.BehaviorOptions
import software.amazon.awscdk.services.cloudfront.Distribution
import software.amazon.awscdk.services.cloudfront.ErrorResponse
import software.amazon.awscdk.services.cloudfront.origins.S3Origin
import software.amazon.awscdk.services.route53.ARecord
import software.amazon.awscdk.services.route53.ARecordProps
import software.amazon.awscdk.services.route53.PublicHostedZone
import software.amazon.awscdk.services.route53.RecordTarget
import software.amazon.awscdk.services.route53.targets.CloudFrontTarget
import software.constructs.Construct

class CloudfrontStack(
    scope: Construct, id: String, props: StackProps, webAppS3Stack: WebAppS3Stack,
    certificate: ICertificate, publicHostedZone: PublicHostedZone
) :
    Stack(scope, id, props) {
    init {

        val s3Origin = S3Origin.Builder.create(webAppS3Stack.bucket)
            .originAccessIdentity(webAppS3Stack.originAccessIdentity)
            .build()


        val distribution = Distribution.Builder.create(this, "GatewayCDN")
            .defaultRootObject("index.html")
            .defaultBehavior(BehaviorOptions.builder().origin(s3Origin).build())
            .domainNames(listOf(Constants.DOMAIN_ROOT))
            .certificate(certificate)
            .errorResponses(
                listOf(
                    ErrorResponse.builder()
                        .httpStatus(404)
                        .responseHttpStatus(200)
                        .responsePagePath("/index.html")
                        .ttl(Duration.days(1))
                        .build()
                )
            )
            .build()

        val cloudfrontTarget = RecordTarget.fromAlias(CloudFrontTarget(distribution))
        ARecord(this, "RootARecord", ARecordProps.builder()
                .recordName(Constants.DOMAIN_ROOT)
                .zone(publicHostedZone)
                .target(cloudfrontTarget)
                .build()
        )
        ARecord(this, "SubWWWRecord", ARecordProps.builder()
            .recordName(Constants.DOMAIN_SUB_WWW)
            .zone(publicHostedZone)
            .target(cloudfrontTarget)
            .build()
        )
    }
}
