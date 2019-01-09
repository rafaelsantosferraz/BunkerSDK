package bunker.snowmanlabs.com.bunker.data.datasource.helper

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

class InternetConnectionDataSource @Inject constructor(val application: Application) {

    fun hasInternetConnection(): Boolean {
        return (application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnected
                ?: false
    }
}
