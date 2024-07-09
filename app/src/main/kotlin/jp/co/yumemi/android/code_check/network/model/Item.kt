package jp.co.yumemi.android.code_check.network.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName(value = "full_name")
    val name: String?,
    val owner: Owner?,
    val language: String?,
    @SerialName("stargazers_count")
    val stargazersCount: Long?,
    @SerialName(value = "watchers_count")
    val watchersCount: Long?,
    @SerialName(value = "forks_count")
    val forksCount: Long?,
    @SerialName(value = "open_issues_count")
    val openIssuesCount: Long?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(Owner::class.java.classLoader),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(language)
        stargazersCount?.let { parcel.writeLong(it) }
        watchersCount?.let { parcel.writeLong(it) }
        parcel.writeValue(forksCount)
        openIssuesCount?.let { parcel.writeLong(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}

@Serializable
data class Owner(
    @SerialName(value = "avatar_url")
    val avatarUrl: String
)