package com.zoho.payslipgenerator.util.sharedpreference

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by sathya-6501 on 30/11/23.
 */
object PreferenceAccessor
{
    fun Context.getPrefs(name: String): SharedPreferences = getSharedPreferences(name, Context.MODE_PRIVATE)

    fun SharedPreferences.Editor.put(key: String, value: Any?)
    {
        when(value)
        {
            is String? -> this.putString(key, value)

            is Int -> this.putInt(key, value)

            is Boolean -> this.putBoolean(key, value)

            is Float -> this.putFloat(key, value)

            is Long -> this.putLong(key, value)

            is Set<Any?> -> this.putStringSet(key, value as MutableSet<String>?)

            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T
    {
        return when (T::class)
        {
            String::class -> getString(key, defaultValue as? String ?: "") as T

            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T

            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T

            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T

            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T

            Set::class -> getStringSet(key, defaultValue as? Set<String> ?: setOf() ) as T

            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}