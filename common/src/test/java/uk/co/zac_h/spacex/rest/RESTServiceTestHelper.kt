package uk.co.zac_h.spacex.rest

import java.io.File

class RESTServiceTestHelper {

    fun getJSON(fileName: String): String {
        val uri = this.javaClass.classLoader?.getResource(fileName)
        val file = uri?.path?.let {
            File(it)
        } ?: File(javaClass.classLoader!!.getResource("error.json").path)
        return String(file.readBytes())
    }

}
