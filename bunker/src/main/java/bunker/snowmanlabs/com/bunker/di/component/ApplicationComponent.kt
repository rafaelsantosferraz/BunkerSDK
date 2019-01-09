package bunker.snowmanlabs.com.bunker.di.component

import android.app.Application
import bunker.snowmanlabs.com.bunker.App
import bunker.snowmanlabs.com.bunker.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    DataModule::class,
    InterceptorsModule::class,
    NetworkModule::class,
    FeaturesModule::class
])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent

    }

    fun inject(app: App)

}
