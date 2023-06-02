package dev.murmurs.flashframe.cdk.frontend

import dev.murmurs.flashframe.cdk.common.Config
import dev.murmurs.flashframe.cdk.common.Constants
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.cloudfront.OriginAccessIdentity
import software.amazon.awscdk.services.s3.BlockPublicAccess
import software.amazon.awscdk.services.s3.Bucket
import software.amazon.awscdk.services.s3.deployment.BucketDeployment
import software.amazon.awscdk.services.s3.deployment.Source
import software.constructs.Construct

class WebAppS3Stack(
    scope: Construct, id: String, props: StackProps,
) :
    Stack(scope, id, props) {
    val bucket: Bucket = Bucket.Builder.create(this, "WebAppBucket")
        .bucketName(Constants.DOMAIN_ROOT + "-website")
        .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
        .build()


    val originAccessIdentity: OriginAccessIdentity = OriginAccessIdentity.Builder.create(this, "OriginAccessIdentity")
        .build()

    init {
        bucket.grantRead(originAccessIdentity)
        BucketDeployment.Builder.create(this, "WebsiteDeployment")
            .destinationBucket(bucket)
            .sources(listOf(Source.asset(Config.FRONT_END_PATH)))
            .build()
    }
}
