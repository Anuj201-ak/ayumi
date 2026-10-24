package com.ayumi.app.data

import androidx.room.TypeConverter
import org.json.JSONArray
import org.json.JSONObject

class Converters {
    @TypeConverter
    fun fromSlotList(slots: List<OverrideSlot>): String {
        val array = JSONArray()
        slots.forEach { slot ->
            array.put(JSONObject().apply {
                put("id", slot.id)
                put("title", slot.title)
                put("startTime", slot.startTime)
                put("endTime", slot.endTime)
                put("important", slot.important)
                put("description", slot.description)
                put("reminderOn", slot.reminderOn)
            })
        }
        return array.toString()
    }

    @TypeConverter
    fun toSlotList(value: String): List<OverrideSlot> {
        val array = JSONArray(value)
        return List(array.length()) { i ->
            val obj = array.getJSONObject(i)
            OverrideSlot(
                id = obj.getLong("id"),
                title = obj.getString("title"),
                startTime = obj.getString("startTime"),
                endTime = obj.getString("endTime"),
                important = obj.getBoolean("important"),
                description = obj.getString("description"),
                reminderOn = obj.getBoolean("reminderOn")
            )
        }
    }
}
