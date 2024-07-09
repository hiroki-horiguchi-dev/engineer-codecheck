package jp.co.yumemi.android.code_check.network.model

import android.os.Parcel
import android.os.Parcelable

data class FilteredItem(
    val name: String,
    val owner: Owner?,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
    var isFavorite: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(Owner::class.java.classLoader),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(language)
        parcel.writeLong(stargazersCount)
        parcel.writeLong(watchersCount)
        parcel.writeLong(forksCount)
        parcel.writeLong(openIssuesCount)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilteredItem> {
        override fun createFromParcel(parcel: Parcel): FilteredItem {
            return FilteredItem(parcel)
        }

        override fun newArray(size: Int): Array<FilteredItem?> {
            return arrayOfNulls(size)
        }
    }
}
