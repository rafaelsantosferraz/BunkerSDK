package testmodule.snowmanlabs.com.testmodule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import bunker.snowmanlabs.com.bunker.ui.Bunker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Bunker.startValidation(this)
    }
}
