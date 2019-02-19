package bunker.snowmanlabs.com.bunker.presentation.ui.main

import android.os.Bundle
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.di.Injectable
import bunker.snowmanlabs.com.bunker.presentation.ui.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}
