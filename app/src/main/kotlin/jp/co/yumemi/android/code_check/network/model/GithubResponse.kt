import jp.co.yumemi.android.code_check.network.model.Item
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubResponse (
    @SerialName(value = "total_count")
    val totalCount: Int,
    @SerialName("incomplete_results")
    val incompleteResults: Boolean,
    val items: List<Item>
)