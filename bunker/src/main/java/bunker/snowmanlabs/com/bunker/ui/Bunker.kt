package bunker.snowmanlabs.com.bunker.ui

import android.content.Context
import android.content.Intent

class Bunker {

    companion object {

        fun startValidation(context: Context) {
            context.startActivity(Intent(context, StartActivity::class.java))
        }
    }
}