package testmodule.snowmanlabs.com.testmodule

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import bunker.snowmanlabs.com.bunker.ui.StartActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, StartActivity::class.java))
    }
}
