package bunker.snowmanlabs.com.bunker.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ScanResult (@SerializedName("data")
                 val data: CnhData,
                       @SerializedName("status")
                 val status: String = ""): Serializable