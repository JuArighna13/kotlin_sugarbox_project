package dto

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DailySummaryTest {

    @Test
    fun `DailySummary should have correct properties`() {
        // Arrange
        val dailySummary = DailySummary(userId = 1, date = "2023-01-01", post = 2, likeReceived = 1, comment = 3)

        // Act
        val userId = dailySummary.userId
        val date = dailySummary.date
        val post = dailySummary.post
        val likeReceived = dailySummary.likeReceived
        val comment = dailySummary.comment

        // Assert
        assertEquals(1, userId)
        assertEquals("2023-01-01", date)
        assertEquals(2, post)
        assertEquals(1, likeReceived)
        assertEquals(3, comment)
    }
}
