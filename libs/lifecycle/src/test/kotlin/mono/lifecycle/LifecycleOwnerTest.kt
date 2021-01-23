package mono.lifecycle

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * A test for [LifecycleOwner].
 */
class LifecycleOwnerTest {
    private val mockLifecycleOwner = MockLifecycleOwner()
    private val mockLifecycleObserver = MockLifecycleObserver()

    @Test
    fun testLifecycle_beforeStarted_fullFlow() {
        mockLifecycleOwner.addObserver(mockLifecycleObserver)

        assertEquals(1, mockLifecycleOwner.lifecycleObservers.size)
        assertTrue(mockLifecycleObserver in mockLifecycleOwner.lifecycleObservers)

        assertEquals(1, mockLifecycleOwner.value)
        assertEquals(10, mockLifecycleObserver.value)

        mockLifecycleOwner.onStart()
        assertEquals(2, mockLifecycleOwner.value)
        assertEquals(11, mockLifecycleObserver.value)

        mockLifecycleOwner.onStop()
        assertEquals(3, mockLifecycleOwner.value)
        assertEquals(12, mockLifecycleObserver.value)

        assertTrue(mockLifecycleOwner.lifecycleObservers.isEmpty())
    }

    @Test
    fun testLifecycle_afterStarted() {
        mockLifecycleOwner.onStart()
        mockLifecycleOwner.addObserver(mockLifecycleObserver)
        assertEquals(11, mockLifecycleObserver.value)
    }

    @Test
    fun testLifecycle_afterStopped() {
        mockLifecycleOwner.onStop()
        mockLifecycleOwner.addObserver(mockLifecycleObserver)
        assertTrue(mockLifecycleOwner.lifecycleObservers.isEmpty())
    }

    private class MockLifecycleOwner : LifecycleOwner() {
        var value = 1
        override fun onStartInternal() {
            value = 2
        }

        override fun onStopInternal() {
            value = 3
        }
    }

    private class MockLifecycleObserver : LifecycleObserver {
        var value = 10
        override fun onStart() {
            value = 11
        }

        override fun onStop() {
            value = 12
        }
    }
}
