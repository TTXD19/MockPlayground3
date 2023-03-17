import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*

class MainTest {

    @Test
    fun wantMoney() {
        // Given
        val kid = spyk<Kid>()

        // When
        kid.wantMoney()
        // Then
        verify(atLeast = 1) { kid.testSuccess() }
    }

    @Test
    fun newTest() {
        // Arrange
        val mockKid = mockk<Kid>(relaxed = true)
        every { mockKid.testSuccess() } just runs
        every { mockKid.testFailure() } just runs

        val callback = object : ResultOutsideHandler() {
            override fun onSuccess() {
                mockKid.testSuccess()
            }

            override fun onFailure() {
                mockKid.testFailure()
            }
        }

        // Act
        callback.onFailure()

        // Assert
        verify { mockKid.testFailure() }
    }

    @Test
    fun testForDataApiHandler() {
        // Arrange
        val mockHandler = mockk<ResultOutsideHandler>()
        val dataApiHolder = DataApiHolder(mockHandler)
        every { mockHandler.onSuccess() } just runs
        every { mockHandler.onFailure() } just runs

        // Act
        dataApiHolder.sendApiRequest()

        // Assert
        verify { mockHandler.onSuccess() }
    }

    @Test
    fun `onSuccess() calls testSuccess() in Kid 3`() {
        // Arrange
        val mockKid = mockk<Kid>()
        val spyHolder = spyk(DataApiHolder(object : ResultOutsideHandler() {
            override fun onSuccess() {
                mockKid.testSuccess()
            }

            override fun onFailure() {
                mockKid.testFailure()
            }
        }))

        every { mockKid.testSuccess() } just runs
        every { mockKid.testFailure() } just runs
        every { mockKid.wantMoney() } answers { spyHolder.sendApiRequest() }
        every { spyHolder.sendApiRequest() } answers { spyHolder.resultListener.onSuccess() }

        // Act
        mockKid.wantMoney()

        // Assert
        verify { mockKid.testSuccess() }
    }

    /**
     * ----------------------------------------------------------------- Failed Test -----------------------------------------------------------------
     */

    @Test
    fun `test testSuccess is called on onSuccess`() {
        // Arrange
        val kidSpy = spyk(Kid(), recordPrivateCalls = true)

        // Mock the constructor of DataApiHolder
        mockkConstructor(DataApiHolder::class)

        every {
            DataApiHolder(any())
        } answers {
            val handler = firstArg<ResultOutsideHandler>()
            val dataApiHolderMock = mockk<DataApiHolder>(relaxed = true)
            every { dataApiHolderMock.sendApiRequest() } just Runs
            handler.onSuccess()
            dataApiHolderMock
        }

        // Act
        kidSpy.wantMoney()

        // Assert
        verify(exactly = 1) { kidSpy.testSuccess() }
        confirmVerified(kidSpy)
    }

}