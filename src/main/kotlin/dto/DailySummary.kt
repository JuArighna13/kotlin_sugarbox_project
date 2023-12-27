package dto

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Data class representing the daily summary information for a user.
 *
 * This class includes aggregated counts of different event types (e.g., post, likeReceived, comment)
 * that occurred on a specific date for a given user.
 *
 * @property userId The unique identifier of the user.
 * @property date The date for which the summary is recorded in the format "yyyy-MM-dd".
 * @property post The count of post events for the user on the specified date (default is 0).
 * @property likeReceived The count of likeReceived events for the user on the specified date (default is 0).
 * @property comment The count of comment events for the user on the specified date (default is 0).
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class DailySummary(
    val userId: Int,
    val date: String,
    var post: Int = 0,
    var likeReceived: Int = 0,
    var comment: Int = 0
)
