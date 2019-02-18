package bunker.snowmanlabs.com.bunker.domain

import com.google.gson.annotations.SerializedName

data class SelfResult(@SerializedName("data")
                      val data: Data,
                      @SerializedName("status")
                      val status: String = "")