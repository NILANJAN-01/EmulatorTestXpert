package com.testfw


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class StorageTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val testFile = File(context.filesDir, "sanity_test.txt")

    @Test
    fun testStorageWriteReadDelete() {
        val content = "AAOS Storage Test OK"

        // Write
        testFile.writeText(content)
        assertTrue(testFile.exists(), "❌ File not created")

        // Read
        val read = testFile.readText()
        assertEquals(content, read, "❌ File content mismatch")

        // Delete
        val deleted = testFile.delete()
        assertTrue(deleted, "❌ File not deleted")
    }

    @After
    fun tearDown() {
        if (testFile.exists()) testFile.delete()
    }
}
