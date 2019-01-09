package bunker.snowmanlabs.com.bunker.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun providesCacheDir(application: Application): File {
        return application.filesDir
    }

}
