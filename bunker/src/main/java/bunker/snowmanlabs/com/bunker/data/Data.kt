package bunker.snowmanlabs.com.bunker.data

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("isValid")
                val isValid: String = "",
                @SerializedName("distanceScore")
                val distanceScore: String = "")