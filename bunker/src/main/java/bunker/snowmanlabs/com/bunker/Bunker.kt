package bunker.snowmanlabs.com.bunker

import android.content.Context
import android.content.Intent
import bunker.snowmanlabs.com.bunker.ui.StartActivity

class Bunker {

    companion object {

        fun startValidation(context: Context) {
            context.startActivity(Intent(context, StartActivity::class.java))
        }

    }
}