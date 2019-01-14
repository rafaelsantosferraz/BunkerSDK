package bunker.snowmanlabs.com.bunker.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CnhData(@SerializedName("result")
                   val result: Result,
                   @SerializedName("classifier")
                   val classifier: Classifier): Serializable