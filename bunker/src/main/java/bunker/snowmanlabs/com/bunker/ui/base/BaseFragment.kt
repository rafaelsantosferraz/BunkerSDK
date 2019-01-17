package bunker.snowmanlabs.com.bunker.ui.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import bunker.snowmanlabs.com.bunker.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.security.InvalidKeyException
import javax.inject.Inject

open class BaseFragment : Fragment() {

    fun handleError(error: Throwable) {
        if (error is InvalidKeyException){
            showError(InvalidKeyException(getString(R.string.result_invalid)))
        }else{
            showError(Throwable(getString(R.string.result_invalid)))
        }
    }

    private fun showError(error: Throwable) {
        Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
    }
}
