package com.just.machine.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

object SingLiveDataBus {

    private val bus: MutableMap<String, BusMutableLiveData<Any>> = HashMap()

    fun <T> with(key: String, type: Class<T>): MutableLiveData<T> {
        if (!bus.containsKey(key)) {
            bus[key] = BusMutableLiveData()
        }
        return bus[key] as MutableLiveData<T>
    }

    fun with(key: String): MutableLiveData<Any> {
        return with(key, Any::class.java)
    }

    private class ObserverWrapper<T>(private val observer: Observer<T>) : Observer<T> {
        private var isObserving = false

        override fun onChanged(t: T?) {
            if (isObserving) return
            observer.onChanged(t)
        }

        fun observe() {
            isObserving = true
        }

        fun clearObserver() {
            isObserving = false
        }
    }

    private class BusMutableLiveData<T> : MutableLiveData<T>() {
        private val observerMap = HashMap<Observer<in T>, Observer<in T>>()

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            try {
                hook(observer as Observer<T>)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun observeForever(observer: Observer<in T>) {
            if (!observerMap.containsKey(observer)) {
                observerMap[observer] = ObserverWrapper(observer)
            }
            observerMap[observer]?.let { super.observeForever(it) }
        }

        override fun removeObserver(observer: Observer<in T>) {
            val realObserver = observerMap.remove(observer)
            if (realObserver != null) {
                super.removeObserver(realObserver)
            } else {
                super.removeObserver(observer)
            }
        }

        private fun hook(observer: Observer<T>) {
            try {
                val liveDataClass = LiveData::class.java
                val fieldObservers = liveDataClass.getDeclaredField("mObservers")
                fieldObservers.isAccessible = true
                val objectObservers = fieldObservers.get(this)
                val classObservers = objectObservers.javaClass
                val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
                methodGet.isAccessible = true
                val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
                var objectWrapper: Any? = null
                if (objectWrapperEntry is Map.Entry<*, *>) {
                    objectWrapper = objectWrapperEntry.value
                }
                if (objectWrapper == null) {
                    throw NullPointerException("Wrapper can not be null!")
                }
                val classObserverWrapper = objectWrapper.javaClass.superclass
                val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
                fieldLastVersion.isAccessible = true
                val fieldVersion = liveDataClass.getDeclaredField("mVersion")
                fieldVersion.isAccessible = true
                val objectVersion = fieldVersion.get(this)
                fieldLastVersion.set(objectWrapper, objectVersion)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

