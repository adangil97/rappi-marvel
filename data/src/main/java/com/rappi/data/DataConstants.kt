package com.rappi.data

import java.util.concurrent.TimeUnit

/**
 * @author Adán Castillo.
 */
object DataConstants {
    const val PUBLIC_KEY = "94dc07a9cacb7b3d75bb6fbcacf8fb4d"
    const val PRIVATE_KEY = "023ee054b3e188ec7a4ebfe6846fb43ffd0275bd"
    const val PAGE_SIZE = 50
    val CACHED_TIME = TimeUnit.MINUTES.toMillis(5)
}