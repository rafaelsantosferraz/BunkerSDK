package bunker.snowmanlabs.com.bunker.di.module

import bunker.snowmanlabs.com.bunker.di.scope.ActivityScope
import bunker.snowmanlabs.com.bunker.presentation.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    // Main
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}