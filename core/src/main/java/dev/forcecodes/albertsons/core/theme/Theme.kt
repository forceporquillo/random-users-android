package dev.forcecodes.albertsons.core.theme

/**
 * Represents the available UI themes for the application
 */
enum class Theme(val storageKey: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system"),
    BATTERY_SAVER("battery_saver")
}

/**
 * Returns the matching [Theme] for the given [storageKey] value.
 */
fun themeFromStorageKey(storageKey: String): Theme? {
    return Theme.entries.firstOrNull { it.storageKey == storageKey }
}