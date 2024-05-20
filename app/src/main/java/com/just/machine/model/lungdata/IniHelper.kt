package com.just.machine.model.lungdata

import java.io.File

object IniHelper {
    fun readKeys(section: String, filePath: String): List<String> {
        val file = File(filePath)
        if (!file.exists()) return emptyList()

        val keys = mutableListOf<String>()
        var currentSection = ""

        file.forEachLine { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.startsWith("[") && trimmedLine.endsWith("]")) {
                currentSection = trimmedLine.substring(1, trimmedLine.length - 1)
            } else if (currentSection == section && trimmedLine.isNotBlank() && '=' in trimmedLine) {
                val key = trimmedLine.substringBefore('=').trim()
                keys.add(key)
            }
        }

        return keys
    }

    fun getValue(section: String, key: String, filePath: String): String {
        val file = File(filePath)
        if (!file.exists()) return ""

        var currentSection = ""
        var value = ""

        file.forEachLine { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.startsWith("[") && trimmedLine.endsWith("]")) {
                currentSection = trimmedLine.substring(1, trimmedLine.length - 1)
            } else if (currentSection == section && trimmedLine.startsWith("$key=")) {
                value = trimmedLine.substringAfter('=').trim()
            }
        }

        return value
    }
}
