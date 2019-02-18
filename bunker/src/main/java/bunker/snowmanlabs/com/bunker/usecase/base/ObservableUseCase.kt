package com.snowmanlabs.appify.model.usecase.base

import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution time_unity for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each SingleUseCase implementation will return the result using a [DisposableObserver]
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
abstract class ObservableUseCase<T, Params> {

    /**
     * Builds an [Observable] which will be used when executing the current [CompletableUseCase].
     */
    internal abstract fun buildObservable(params: Params?): Observable<T>

}
