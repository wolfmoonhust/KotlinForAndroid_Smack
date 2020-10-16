package com.example.smack.mvprefactor.base

interface MvpPresenter<V : MvpView> {
    fun onAttach(view: V?)
    fun onDetach()
    fun getView(): V?

}