package dto

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserEventTest {

    @Test
    fun `UserEvent should have correct properties`() {
        // Arrange
        val userEvent = UserEvent(userId = 1, eventType = "post", timestamp = 1672444800)

        // Act
        val userId = userEvent.userId
        val eventType = userEvent.eventType
        val timestamp = userEvent.timestamp

        // Assert
        assertEquals(1, userId)
        assertEquals("post", eventType)
        assertEquals(1672444800, timestamp)
    }
}
