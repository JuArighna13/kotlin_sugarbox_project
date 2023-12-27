package dto

/**
 * Data class representing a user event.
 *
 * This class encapsulates information about an event performed by a user,
 * including the user's unique identifier, the type of event, and the timestamp
 * when the event occurred.
 *
 * @property userId The unique identifier of the user who performed the event.
 * @property eventType The type of event (e.g., post, likeReceived, comment).
 * @property timestamp The timestamp when the event occurred, represented in milliseconds.
 */
data class UserEvent(
    val userId: Int,
    val eventType: String,
    val timestamp: Long
)
