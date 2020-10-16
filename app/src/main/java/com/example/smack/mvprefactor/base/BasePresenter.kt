package com.example.smack.mvprefactor.base

abstract class BasePresenter<V : MvpView> : MvpPresenter<V> {
    var mView: V? = null
    private val isViewAttached: Boolean get() = mView != null

    override fun onAttach(view: V?) {
        mView = view
    }

    override fun getView(): V? {
        return mView
    }

    override fun onDetach() {
        mView = null
    }

}