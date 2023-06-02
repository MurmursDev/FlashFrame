package dev.murmurs.flashframe.cdk.common

class Constants {
    companion object {
        const val AWS_REGION_PDX = "us-west-2"
        const val AWS_REGION_IAD = "us-east-1"
        const val DOMAIN_ROOT = Config.DOMAIN
        const val DOMAIN_SUB_WWW = "www.${DOMAIN_ROOT}"
        const val DOMAIN_SUB_AUTH = "auth.${DOMAIN_ROOT}"
        const val DOMAIN_SUB_STATIC = "static.${DOMAIN_ROOT}"
        const val DOMAIN_SUB_API = "api.${DOMAIN_ROOT}"
        const val DOMAIN_SUB_EMAIL = "email.${DOMAIN_ROOT}"
        const val URL_ROOT = "https://${DOMAIN_ROOT}"
        const val URL_SUB_WWW = "https://www.${DOMAIN_ROOT}"
        const val URL_DEV_LOCAL = "http://localhost:4200"
        const val URL_SUB_AUTH = "https://auth.${DOMAIN_ROOT}"
        const val PORT_HTTPS = 443
        const val PORT_HTTP = 80
        const val PORT_SSH = 22
        const val EMAIL_NO_REPLY = "noreply@$DOMAIN_ROOT"
        const val EMAIL_SUPPORT = "support@$DOMAIN_ROOT"
    }
}
