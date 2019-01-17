package bunker.snowmanlabs.com.bunker.di.module

import androidx.lifecycle.ViewModelProvider
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
