package dev.murmurs.flashframe.cdk.base

import dev.murmurs.flashframe.cdk.common.Config
import dev.murmurs.flashframe.cdk.common.Constants
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.route53.PublicHostedZone
import software.constructs.Construct


class Route53Stack(scope: Construct, id: String, props: StackProps) : Stack(scope, id, props) {
    val publicHostedZone: PublicHostedZone =
        PublicHostedZone.Builder.create(this, "${Config.APP_NAME}Zone")
            .zoneName(Constants.DOMAIN_ROOT)
            .build()
}
