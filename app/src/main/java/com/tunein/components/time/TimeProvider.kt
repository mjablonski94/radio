package com.tunein.components.time

interface TimeProvider {
    fun now(): Long
}