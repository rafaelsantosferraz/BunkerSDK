package bunker.snowmanlabs.com.bunker.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Classifier(@SerializedName("Score")
                      val score: String = "",
                      @SerializedName("IsValid")
                      val isValid: String = ""): Serializable