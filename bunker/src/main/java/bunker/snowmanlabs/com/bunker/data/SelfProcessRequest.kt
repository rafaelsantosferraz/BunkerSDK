package bunker.snowmanlabs.com.bunker.data

import com.google.gson.annotations.SerializedName

data class SelfProcessRequest(@SerializedName("SessionID")
                              val sessionID: String = "",
                              @SerializedName("CodedImage")
                              val codedImage: String = "",
                              @SerializedName("FaceRec")
                              val faceRec: FaceRec)