package bunker.snowmanlabs.com.bunker.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProcessRequest(
        @SerializedName("SessionID")
        val sessionId: String?,
        @SerializedName("CodedImage")
        val codedImage: String?) : Serializable