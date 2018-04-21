import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import java.io.IOException

object MoshiDeserializer {

    private val moshiDeserializer: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Throws(IOException::class)
    fun deserialize(modelClass: Class<*>, serializableObject: Any): String? {
        return moshiDeserializer
                .adapter<Any?>(modelClass)
                .lenient()
                .nullSafe()
                .toJson(serializableObject)
                .toString()
    }
}