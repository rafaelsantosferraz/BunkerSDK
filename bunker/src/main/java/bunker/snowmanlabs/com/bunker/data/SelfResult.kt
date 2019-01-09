package bunker.snowmanlabs.com.bunker.data

import com.google.gson.annotations.SerializedName

data class SelfResult(@SerializedName("data")
                      val data: Data,
                      @SerializedName("status")
                      val status: String = "")