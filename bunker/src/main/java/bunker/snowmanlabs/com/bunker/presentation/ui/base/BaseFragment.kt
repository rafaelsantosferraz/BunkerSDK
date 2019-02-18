package bunker.snowmanlabs.com.bunker.presentation.ui.base

import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import bunker.snowmanlabs.com.bunker.R
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.security.InvalidKeyException

open class BaseFragment : Fragment() {

    private val TAG = "BaseFragment"




    protected fun setMessageContent(message: String) {
        context?.let {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private val updated = HashMap<String, Any?>()

    private var progressDialog: AlertDialog? = null

    fun <T> part(name: String, newValue: T, updater: (T) -> Unit) {
        if (!updated.containsKey(name) || updated[name] != newValue) {
            updater(newValue)
            updated.put(name, newValue)
        }
    }



//    private fun logout() {
//        (activity!! as BaseActivity).logout()
//    }


    // region Handle Error -------------------------------------------------------------------------
    open fun handleError(throwable: Throwable) {
        when(throwable) {
            is ConnectException -> showDialog(R.string.error_no_internet)
            is SocketTimeoutException -> showDialog(R.string.error_bad_connection)
            is HttpException ->
                when(throwable.code()){
                    HttpURLConnection.HTTP_UNAUTHORIZED -> showDialog(R.string.error_session_expired, false)
                    HttpURLConnection.HTTP_PRECON_FAILED -> showDialog(R.string.error_precon_failed)
                }
            else -> showDialog(R.string.error_unknown)
        }
        Log.e(TAG, "$throwable")
    }
    // endregion


    // region Dialog -------------------------------------------------------------------------------
    private fun showDialog(
        @StringRes messageId: Int,
        isCancelable: Boolean = true,
        @StringRes textId: Int = R.string.ok,
        functionBlock : (() -> Unit)? = null){

        AlertDialog.Builder(activity!!)
            .setMessage(messageId)
            .setCancelable(isCancelable)
            .setPositiveButton(textId){ _,_ ->
                functionBlock?.invoke()
            }
            .show()
    }
    // end region
}
