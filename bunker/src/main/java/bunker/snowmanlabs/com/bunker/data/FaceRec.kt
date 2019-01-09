package bunker.snowmanlabs.com.bunker.data

import com.google.gson.annotations.SerializedName

data class FaceRec(@SerializedName("Mode")
                   val mode: String = "",
                   @SerializedName("Name")
                   val name: String = ""){

    companion object {
        fun modeSelf(): FaceRec{
            return FaceRec(mode = "check", name = "#")
        }
    }
}