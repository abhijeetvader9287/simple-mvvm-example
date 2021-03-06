package com.azabost.simplemvvm.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import com.azabost.simplemvvm.di.ViewModelFactory
import com.azabost.simplemvvm.utils.HasLifecycleScopeProvider
import com.uber.autodispose.LifecycleScopeProvider
import com.uber.autodispose.android.lifecycle.scope
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseFragment :
    Fragment(),
    HasSupportFragmentInjector,
    HasLifecycleScopeProvider {

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override val scopeProvider: LifecycleScopeProvider<*> by lazy { scope() }
    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector

    inline fun <reified T : ViewModel> ViewModelFactory<T>.get(): T =
        ViewModelProviders.of(this@BaseFragment, this)[T::class.java]

    inline fun <reified T : ViewModel> ViewModelFactory<T>.getForActivity(): T =
        ViewModelProviders.of(activity!!, this)[T::class.java]
}