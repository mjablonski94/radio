package com.tunein.components.ext

import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * https://betterprogramming.pub/what-is-liveevent-livedata-7270a64736b3
 */
class LiveEvent<T> : MediatorLiveData<T>() {

    private val observers = ArraySet<ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observers.remove(observer as Observer<*>)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.newValue() }
        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private var pending = false

        fun newValue() {
            pending = true
        }

        override fun onChanged(value: T) {
            if (pending) {
                pending = false
                observer.onChanged(value)
            }
        }
    }
}