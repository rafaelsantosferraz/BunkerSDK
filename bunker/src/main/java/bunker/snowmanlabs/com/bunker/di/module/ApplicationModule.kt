package bunker.snowmanlabs.com.bunker.di.module

import bunker.snowmanlabs.com.bunker.di.qualifiers.IO
import bunker.snowmanlabs.com.bunker.di.qualifiers.UI
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    @IO
    fun providesExecutorThread(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Singleton
    @UI
    fun providesMainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
