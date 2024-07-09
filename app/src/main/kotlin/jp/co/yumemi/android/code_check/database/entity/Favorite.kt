package jp.co.yumemi.android.code_check.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class Favorite(
    @PrimaryKey
    val repositoryName: String
)