package dev.murmurs.flashframe.cdk.backend

import dev.murmurs.flashframe.cdk.common.Constants
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.route53.PublicHostedZone
import software.amazon.awscdk.services.ses.EmailIdentity
import software.amazon.awscdk.services.ses.Identity
import software.amazon.awscdk.services.ses.ReceiptRuleOptions
import software.amazon.awscdk.services.ses.ReceiptRuleSet
import software.amazon.awscdk.services.ses.actions.Sns
import software.amazon.awscdk.services.sns.Subscription
import software.amazon.awscdk.services.sns.SubscriptionProtocol
import software.amazon.awscdk.services.sns.Topic
import software.amazon.awscdk.services.sqs.Queue
import software.constructs.Construct

class SesStack(
    scope: Construct, id: String, props: StackProps,
    publicHostedZone: PublicHostedZone
) : Stack(scope, id, props) {

    init {
        val emailIdentity = EmailIdentity.Builder.create(this, "EmailIdentity")
            .identity(Identity.publicHostedZone(publicHostedZone))
            .mailFromDomain(Constants.DOMAIN_SUB_EMAIL)
            .build()

        val emailReceiveTopic = Topic.Builder.create(this, "EmailReceiveTopic")
            .topicName("EmailReceive")
            .build()

        val emailReceiveQueue = Queue.Builder.create(this, "EmailReceiveQueue")
            .queueName("EmailReceive")
            .build()

        Subscription.Builder.create(this, "ToEmailSubscription")
            .protocol(SubscriptionProtocol.SQS)
            .topic(emailReceiveTopic)
            .endpoint(emailReceiveQueue.queueArn)
            .build()

        val ruleSet = ReceiptRuleSet.Builder.create(this, "ReceiveSupportEmailRuleSet")
            .receiptRuleSetName("ReceiveSupportEmail")
            .rules(
                listOf(
                    ReceiptRuleOptions.builder()
                        .receiptRuleName("TestRule")
                        .recipients(listOf(Constants.DOMAIN_ROOT))
                        .actions(listOf(Sns.Builder.create().topic(emailReceiveTopic).build()))
                        .enabled(true)
                        .build()
                )
            )
            .build()
    }
}
