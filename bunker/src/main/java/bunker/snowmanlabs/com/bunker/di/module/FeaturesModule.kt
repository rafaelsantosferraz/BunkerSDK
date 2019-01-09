package bunker.snowmanlabs.com.bunker.di.module

import android.arch.lifecycle.ViewModelProvider
import bunker.snowmanlabs.com.bunker.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module(includes = [
    ScanProcessModule::class
])
abstract class FeaturesModule {

    @Binds
    abstract fun bindViewMainModuleModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
