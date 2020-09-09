package com.travels.searchtravels

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.api.services.vision.v1.model.LatLng
import com.travels.searchtravels.api.OnVisionApiListener
import com.travels.searchtravels.api.VisionApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.io.InputStream


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {
    private val API_KEY_VISION = "AIzaSyCXCHtkw5Kw7q3-BM3QIbIQgNEWPrXTf6w"

    companion object {
        const val MIN_TIMEOUT = 120 * 1000L
    }

    @Test(timeout = MIN_TIMEOUT)
    fun visionApiOceanIsCorrect() {
        VisionApi.findLocation(getBitmapFromAsset(
            ApplicationProvider.getApplicationContext(),
            "ocean"
        ),
            API_KEY_VISION, object : OnVisionApiListener {
                override fun onSuccess(latLng: LatLng?) {
                    Assert.assertTrue(true)
                }

                override fun onErrorPlace(category: String?) {
                    Assert.assertTrue(
                        category.equals("ocean") || category.equals("sea") || category.equals(
                            "beach"
                        )
                    )
                }

                override fun onError() {
                    Assert.assertTrue(false)
                }
            })
    }

    @Test(timeout = MIN_TIMEOUT)
    fun visionApiShowMountainIsCorrect() {
        VisionApi.findLocation(getBitmapFromAsset(
            ApplicationProvider.getApplicationContext(),
            "mountain"
        ),
            API_KEY_VISION, object : OnVisionApiListener {
                override fun onSuccess(latLng: LatLng?) {
                    Assert.assertTrue(true)
                }

                override fun onErrorPlace(category: String?) {
                    Assert.assertTrue(category.equals("mountain") || category.equals("snow"))
                }

                override fun onError() {
                    Assert.assertTrue(false)
                }
            })
    }

    fun getBitmapFromAsset(context: Context, filePath: String?): Bitmap? {
        val assetManager: AssetManager = context.getAssets()
        val istr: InputStream
        var bitmap: Bitmap? = null
        try {
            istr = assetManager.open(filePath!!)
            bitmap = BitmapFactory.decodeStream(istr)
        } catch (e: IOException) {
            // handle exception
        }
        return bitmap
    }
}