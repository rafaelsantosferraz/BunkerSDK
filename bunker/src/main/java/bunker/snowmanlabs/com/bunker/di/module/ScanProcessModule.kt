package bunker.snowmanlabs.com.bunker.di.module

import android.arch.lifecycle.ViewModel
import bunker.snowmanlabs.com.bunker.di.ViewModelKey
import bunker.snowmanlabs.com.bunker.di.scope.ActivityScope
import bunker.snowmanlabs.com.bunker.di.scope.FragmentScope
import bunker.snowmanlabs.com.bunker.ui.*
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ScanProcessModule {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun scanActivity(): ScanProcessActivity

    @Binds
    @IntoMap
    @ViewModelKey(ScanProcessViewModel::class)
    abstract fun scanViewModel(viewModel: ScanProcessViewModel): ViewModel

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun scanCnhFragment(): ScanCnhFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun scanSelfFragment(): ScanSelfFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun scanResultFragment(): ResultFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun verificationFragmentFragment(): VerificationCompleteFragment

    @Binds
    @IntoMap
    @ViewModelKey(ScanCnhViewModel::class)
    abstract fun scanCnhViewModel(viewModel: ScanCnhViewModel): ViewModel

}