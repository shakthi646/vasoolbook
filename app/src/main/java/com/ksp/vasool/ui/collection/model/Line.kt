package com.ksp.vasool.ui.collection.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Created by sathya-6501 on 31/08/23.
 */

@Entity(tableName = "line", indices = [Index(value = ["line_id"], unique = true)])
class Line
{
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0

    @ColumnInfo(name = "line_name")
    var lineName: String? = null

    @ColumnInfo(name = "line_id")
    var lineId: String? = null
}