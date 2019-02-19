package bunker.snowmanlabs.com.bunker.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bunker.snowmanlabs.com.bunker.di.ViewModelKey
import bunker.snowmanlabs.com.bunker.presentation.ui.scan.ScanCnhViewModel
import bunker.snowmanlabs.com.bunker.presentation.ui.self.SelfViewModel
import bunker.snowmanlabs.com.bunker.presentation.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewMainModuleModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(SelfViewModel::class)
    abstract fun selfViewModel(viewModel: SelfViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ScanCnhViewModel::class)
    abstract fun scanCnhViewModel(viewModel: ScanCnhViewModel): ViewModel

}