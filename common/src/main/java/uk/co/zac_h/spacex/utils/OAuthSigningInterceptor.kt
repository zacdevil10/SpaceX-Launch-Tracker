package uk.co.zac_h.spacex.utils

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.ByteString
import uk.co.zac_h.spacex.model.twitter.OAuthKeys
import java.security.GeneralSecurityException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class OAuthSigningInterceptor(private val keys: OAuthKeys) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(signRequest(chain.request()))

    private fun signRequest(request: Request): Request {
        val nonce = UUID.randomUUID()
        val timestamp = System.currentTimeMillis() / 1000L

        val parameters = hashMapOf(
            OAUTH_CONSUMER_KEY to keys.consumerKey,
            OAUTH_TOKEN to keys.accessToken,
            OAUTH_NONCE to nonce,
            OAUTH_SIGNATURE_METHOD to OAUTH_SIGNATURE_METHOD_VALUE,
            OAUTH_TIMESTAMP to timestamp.toString(),
            OAUTH_VERSION to OAUTH_VERSION_VALUE
        )

        val method = request.method().encodeUtf8()
        val baseUrl = request.url().newBuilder().query(null).build().toString().encodeUtf8()
        val url = request.url()
        for (i in 0 until url.querySize()) {
            parameters[url.queryParameterName(i)] = url.queryParameterValue(i)
        }

        val signingKey = "${keys.consumerSecret.encodeUtf8()}&${keys.tokenSecret.encodeUtf8()}"
        val params = parameters.encodeForSignature()
        val base = "$method&$baseUrl&$params"
        parameters[OAUTH_SIGNATURE] = sign(signingKey, base).encodeUtf8()

        val header = "OAuth ${parameters.toHeaderFormat()}"

        return request.newBuilder().header(AUTH_HEADER, header).build()
    }

    @Throws(GeneralSecurityException::class)
    private fun sign(key: String, base: String): String {
        val secret = SecretKeySpec(key.toByteArray(), ALGORITHM)
        val result = Mac.getInstance(ALGORITHM).run {
            init(secret)
            doFinal(base.toByteArray())
        }
        return ByteString.of(*result).base64()
    }

    private fun HashMap<String, Any>.encodeForSignature() =
        toList()
            .sortedBy { (key, _) -> key }
            .toMap().map { "${it.key}=${it.value}" }
            .joinToString("&")
            .encodeUtf8()

    private fun HashMap<String, Any>.toHeaderFormat() =
        filter { it.key in baseKeys }
            .toList()
            .sortedBy { (key, _) -> key }
            .toMap()
            .map { "${it.key}=\"${it.value}\"" }
            .joinToString(", ")

    companion object Builder {

        private const val ALGORITHM = "HmacSHA1"
        private const val AUTH_HEADER = "Authorization"
        private const val OAUTH_CONSUMER_KEY = "oauth_consumer_key"
        private const val OAUTH_NONCE = "oauth_nonce"
        private const val OAUTH_SIGNATURE = "oauth_signature"
        private const val OAUTH_SIGNATURE_METHOD = "oauth_signature_method"
        private const val OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1"
        private const val OAUTH_TIMESTAMP = "oauth_timestamp"
        private const val OAUTH_TOKEN = "oauth_token"
        private const val OAUTH_VERSION = "oauth_version"
        private const val OAUTH_VERSION_VALUE = "1.0"

        private val baseKeys = arrayListOf(
            OAUTH_CONSUMER_KEY,
            OAUTH_NONCE,
            OAUTH_SIGNATURE,
            OAUTH_SIGNATURE_METHOD,
            OAUTH_TIMESTAMP,
            OAUTH_TOKEN,
            OAUTH_VERSION
        )

        private lateinit var keys: OAuthKeys


        fun addKeys(keys: OAuthKeys): Builder {
            this.keys = keys
            return this
        }

        fun build(): OkHttpClient {
            return OkHttpClient.Builder().addInterceptor(OAuthSigningInterceptor(keys)).build()
        }
    }
}