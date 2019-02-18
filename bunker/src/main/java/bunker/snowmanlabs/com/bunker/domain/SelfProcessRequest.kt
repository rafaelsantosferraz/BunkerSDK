package bunker.snowmanlabs.com.bunker.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SelfProcessRequest(
    @SerializedName("Identifier")
    val sessionId: String?,
    @SerializedName("CodedImage")
    val codedImage: String?) : Serializable