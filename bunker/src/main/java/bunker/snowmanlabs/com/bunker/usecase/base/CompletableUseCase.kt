package com.snowmanlabs.appify.model.usecase.base

import io.reactivex.Completable

abstract class CompletableUseCase<Params> {

    internal abstract fun buildObservable(params: Params?): Completable
}