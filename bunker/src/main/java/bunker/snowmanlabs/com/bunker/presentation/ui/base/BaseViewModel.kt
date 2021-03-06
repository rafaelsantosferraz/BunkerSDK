package bunker.snowmanlabs.com.bunker.presentation.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<S, C> : ViewModel() {

    private var disposables = CompositeDisposable()

    val command = SingleLiveEvent<C>()
    val state = MutableLiveData<S>()

    init {
        newState(initialState())
    }

    abstract fun initialState(): S

    protected fun newState(state: S) {
        this.state.value = state
    }

    fun currentState(): S {
        return state.value!!
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun add(disposable: Disposable) {
        disposables.add(disposable)
    }

}

