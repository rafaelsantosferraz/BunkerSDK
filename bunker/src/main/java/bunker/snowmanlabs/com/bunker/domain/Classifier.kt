package bunker.snowmanlabs.com.bunker.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Classifier(@SerializedName("Score")
                      val score: String = "",
                      @SerializedName("IsValid")
                      val isValid: String = ""): Serializable