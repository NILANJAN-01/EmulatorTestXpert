package com.testfw

import android.car.Car
import android.car.drivingstate.CarUxRestrictions
import android.car.drivingstate.CarUxRestrictionsManager
import android.car.hardware.property.CarPropertyManager
import android.car.hardware.property.VehiclePropertyIds
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class DriverRestrictionsTest {

    private lateinit var car: Car
    private lateinit var uxManager: CarUxRestrictionsManager
    private lateinit var propManager: CarPropertyManager

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        car = Car.createCar(ctx)
        uxManager = car.getCarManager(Car.CAR_UX_RESTRICTION_SERVICE) as CarUxRestrictionsManager
        propManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
    }

    @Test
    fun testUxRestrictionsWhenDriving() {
        // Simulate vehicle moving (speed > 10 km/h)
        propManager.setProperty(
            VehiclePropertyIds.PERF_VEHICLE_SPEED,
            0,
            15.0f // 15 km/h
        )

        // Get current restrictions
        val restrictions: CarUxRestrictions = uxManager.currentCarUxRestrictions
        assertNotNull(restrictions, "❌ No UX restrictions reported")

        assertTrue(restrictions.isRequiresDistractionOptimization,
            "❌ Restrictions not applied while driving")
    }

    @Test
    fun testUxRestrictionsWhenStationary() {
        // Simulate vehicle stopped
        propManager.setProperty(
            VehiclePropertyIds.PERF_VEHICLE_SPEED,
            0,
            0.0f
        )

        val restrictions: CarUxRestrictions = uxManager.currentCarUxRestrictions
        assertNotNull(restrictions, "❌ No UX restrictions reported")

        assertEquals(false, restrictions.isRequiresDistractionOptimization,
            "❌ Restrictions applied incorrectly when stationary")
    }

    @After
    fun tearDown() {
        car.disconnect()
    }
}
