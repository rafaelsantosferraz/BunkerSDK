package testmodule.snowmanlabs.com.testmodule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bunker.snowmanlabs.com.bunker.Bunker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Bunker.startValidation(this)


    }
}
