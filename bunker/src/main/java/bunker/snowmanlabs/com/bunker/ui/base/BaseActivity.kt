package bunker.snowmanlabs.com.bunker.ui.base

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> {
        return dispatchingAndroidInjector
    }

}
