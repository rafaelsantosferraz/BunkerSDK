package bunker.snowmanlabs.com.bunker.di.module

import bunker.snowmanlabs.com.bunker.di.scope.FragmentScope
import bunker.snowmanlabs.com.bunker.presentation.ui.*
import bunker.snowmanlabs.com.bunker.presentation.ui.main.MainFragment
import bunker.snowmanlabs.com.bunker.presentation.ui.self.SelfFragment
import bunker.snowmanlabs.com.bunker.presentation.ui.result.ResultFragment
import bunker.snowmanlabs.com.bunker.presentation.ui.scan.ScanCnhFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    // Scan
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun scanCnhFragment(): ScanCnhFragment


    // Self
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun selfFragment(): SelfFragment

}