package bunker.snowmanlabs.com.bunker

import android.content.Context
import android.content.Intent
import bunker.snowmanlabs.com.bunker.presentation.ui.StartActivity
import bunker.snowmanlabs.com.bunker.presentation.ui.main.MainActivity

class Bunker {

    companion object {

        fun startValidation(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }

    }
}