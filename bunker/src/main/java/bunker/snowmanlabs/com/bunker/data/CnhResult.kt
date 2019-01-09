package bunker.snowmanlabs.com.bunker.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CnhResult (@SerializedName("data")
                 val data: CnhData,
                 @SerializedName("status")
                 val status: String = ""): Serializable