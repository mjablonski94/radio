package com.tunein.components.time

class TimeProviderImpl: TimeProvider {
    override fun now(): Long = System.currentTimeMillis()
}