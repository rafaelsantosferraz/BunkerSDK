package bunker.snowmanlabs.com.bunker.di.module

import androidx.lifecycle.ViewModel
import bunker.snowmanlabs.com.bunker.di.ViewModelKey
import bunker.snowmanlabs.com.bunker.di.scope.ActivityScope
import bunker.snowmanlabs.com.bunker.di.scope.FragmentScope
import bunker.snowmanlabs.com.bunker.presentation.ui.*
import bunker.snowmanlabs.com.bunker.presentation.ui.main.MainActivity
import bunker.snowmanlabs.com.bunker.presentation.ui.self.SelfFragment
import bunker.snowmanlabs.com.bunker.presentation.ui.self.SelfViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ActivityModule {


    @ActivityScope
    @ContributesAndroidInjector
    abstract fun scanActivity(): ScanProcessActivity


    // Main
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity



}