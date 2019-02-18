package bunker.snowmanlabs.com.bunker.domain

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("isValid")
                val isValid: String = "",
                @SerializedName("distanceScore")
                val distanceScore: String = "")