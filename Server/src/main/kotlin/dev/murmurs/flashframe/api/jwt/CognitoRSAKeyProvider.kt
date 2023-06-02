package dev.murmurs.flashframe.api.jwt

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.interfaces.RSAKeyProvider
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.TimeUnit


class CognitoRSAKeyProvider(private val region: String, private val poolId: String) : RSAKeyProvider {
    private val provider: JwkProvider =
        JwkProviderBuilder("https://cognito-idp.${region}.amazonaws.com/${poolId}/.well-known/jwks.json") // up to 10 JWKs will be cached for up to 24 hours
            .cached(10, 24, TimeUnit.HOURS) // up to 10 JWKs can be retrieved within one minute
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()


    override fun getPublicKeyById(keyId: String?): RSAPublicKey {
        return provider.get(keyId).publicKey as RSAPublicKey
    }

    override fun getPrivateKey(): RSAPrivateKey {
        throw UnsupportedOperationException()
    }

    override fun getPrivateKeyId(): String {
        throw UnsupportedOperationException()
    }
}
