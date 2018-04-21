import android.util.Log
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException

object MoshiSerializer {

    private val moshiSerializer: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Throws(IOException::class)
    fun serialize(modelClass: Class<*>, json: String): Any? {
        try {
            return moshiSerializer.adapter<Any?>(modelClass)
                    .lenient()
                    .nullSafe()
                    .fromJson(json)
        } catch (e: JsonDataException) {
            Log.e("Erroneous Model : ", modelClass.canonicalName)
            throw e
        } catch (e: IllegalArgumentException) {
            Log.e("Erroneous Model : ", modelClass.canonicalName)
            throw e
        }
    }

    @Throws(IOException::class)
    fun serializeList(modelClass: Class<*>, json: String): Any? {
        try {
            return moshiSerializer.adapter<Any?>(Types.newParameterizedType(List::class.java, modelClass))
                    .lenient()
                    .nullSafe()
                    .fromJson(json)
        } catch (e: JsonDataException) {
            Log.e("Erroneous Model : ", modelClass.canonicalName)
            throw e
        } catch (e: IllegalArgumentException) {
            Log.e("Erroneous Model : ", modelClass.canonicalName)
            throw e
        }
    }
}
