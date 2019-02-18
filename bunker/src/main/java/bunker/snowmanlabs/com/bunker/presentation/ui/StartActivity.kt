package bunker.snowmanlabs.com.bunker.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bunker.snowmanlabs.com.bunker.R
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start)

        bt_ready.setOnClickListener {
            startActivity(Intent(this, ScanProcessActivity::class.java))
            finishAffinity()
        }
    }
}