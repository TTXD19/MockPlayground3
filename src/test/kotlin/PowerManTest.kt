import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(BaseAPIDataHolder::class)
class PowerManTest {

    @Test
    fun testSuccessShouldBeCalledWhenWantMoneyIsCalled() {
        // Given
        val kid = spyk(Kid())

        val resultInsideListenerMock = mockk<ResultInsideListener>()
        val handlingMock = mockk<BaseAPIDataHolder>()

        every { handlingMock.resultListener } returns resultInsideListenerMock
        every { handlingMock.sendApiRequest() } answers { handlingMock.resultListener.onSuccess() }

        PowerMockito.whenNew(BaseAPIDataHolder::class.java).withArguments(any<ResultOutsideListener>()).thenReturn(handlingMock)

        // When
        kid.wantMoney()

        // Then
        verify { kid.testSuccess() }
    }

}