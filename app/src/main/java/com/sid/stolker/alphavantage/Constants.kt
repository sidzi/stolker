package com.sid.stolker.alphavantage

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Constants {
    val ALPHA_VANTAGE_API_KEY = DXDecryptorW1nEm2Do.decode("uIWqzS2PrtRwDPS9bexgqw==")
}

internal object DXDecryptorW1nEm2Do {
    var algo = "ARCFOUR"
    var kp = "W6aJzkaS4WGyqP6Z"

    fun decode(s: String): String {
        val str: String
        val key = "w+1eFwnKDsiJyabzhdui8A=="
        str = try {
            val rc4 = Cipher.getInstance(algo)
            val kpk = SecretKeySpec(kp.toByteArray(), algo)
            rc4.init(Cipher.DECRYPT_MODE, kpk)
            val bck = Base64.decode(key, Base64.DEFAULT)
            val bdk = rc4.doFinal(bck)
            val dk = SecretKeySpec(bdk, algo)
            rc4.init(Cipher.DECRYPT_MODE, dk)
            val bcs = Base64.decode(s, Base64.DEFAULT)
            val byteDecryptedString = rc4.doFinal(bcs)
            String(byteDecryptedString)
        } catch (e: Exception) {
            ""
        }

        return str
    }

}