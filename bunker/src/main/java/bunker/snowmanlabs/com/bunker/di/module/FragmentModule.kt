package bunker.snowmanlabs.com.bunker.di.module

import bunker.snowmanlabs.com.bunker.di.scope.FragmentScope
import bunker.snowmanlabs.com.bunker.presentation.ui.*
import bunker.snowmanlabs.com.bunker.presentation.ui.main.MainFragment
import bunker.snowmanlabs.com.bunker.presentation.ui.self.SelfFragment
import bunker.snowmanlabs.com.bunker.presentation.ui.result.ResultFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {


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



    // Self
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment




    // Self
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun selfFragment(): SelfFragment

}