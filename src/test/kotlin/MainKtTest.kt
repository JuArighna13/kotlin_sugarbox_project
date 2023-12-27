import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MainKtTest {

    @Test
    fun `main should print usage when insufficient arguments provided`() {
        // Arrange
        val args = arrayOf("-i", "input.json")

        // Act & Assert
        assertPrintsUsage(args)
    }

    @Test
    fun `main should print usage when incorrect argument format provided`() {
        // Arrange
        val args = arrayOf("-i", "input.json", "-o")

        // Act & Assert
        assertPrintsUsage(args)
    }

    @Test
    fun `main should call aggregateEvents when correct arguments provided`() {
        // Arrange
        val args = arrayOf("-i", "input.json", "-o", "output.json")

        // Act & Assert
        assertDoesNotThrow { main(args) }
    }

    @Test
    fun `main should call aggregateEvents with update when correct arguments provided`() {
        // Arrange
        val args = arrayOf("-i", "input.json", "-o", "output.json", "--update")

        // Act & Assert
        assertDoesNotThrow { main(args) }
    }

    private fun assertPrintsUsage(args: Array<String>) {
        // Redirect System.out to capture println output
        val originalOut = System.out
        val outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))

        // Act
        main(args)

        // Reset System.out
        System.setOut(originalOut)

        // Assert
        assertTrue(outContent.toString().startsWith("Usage:"))
    }
}
