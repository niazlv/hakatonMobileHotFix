package com.travels.searchtravels

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.preview.planner.prefs.AppPreferences.setToken
import com.travels.searchtravels.activity.ChipActivity
import com.travels.searchtravels.activity.DetailsActivity
import com.travels.searchtravels.activity.MainActivity
import com.travels.searchtravels.utils.Constants
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    private val API_KEY_VISION = "AIzaSyCXCHtkw5Kw7q3-BM3QIbIQgNEWPrXTf6w"

    companion object {
        const val MIN_TIMEOUT = 120 * 1000L
    }

    @Test(timeout = MIN_TIMEOUT)
    fun biarritzCity() {
        val scenario = launch(MainActivity::class.java)
        ActivityLifecycleMonitorRegistry.getInstance()
            .addLifecycleCallback { activity, stage ->
                if (activity is MainActivity && stage == Stage.RESUMED) {
                    (activity as MainActivity).uploadImage(
                        Uri.fromFile(
                            File(
                                "file://android_asset/ocean_biarric.jpg"
                            )
                        )
                    )
                } else if (activity is DetailsActivity && stage == Stage.RESUMED) {
                    Assert.assertEquals(
                        "Biarritz",
                        Constants.PICKED_CITY_EN
                    )
                }
            }
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.moveToState(Lifecycle.State.CREATED)
        setToken(
            ApplicationProvider.getApplicationContext(),
            API_KEY_VISION
        )
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test(timeout = MIN_TIMEOUT)
    fun miamiCity() {
        val scenario = launch(MainActivity::class.java)
        ActivityLifecycleMonitorRegistry.getInstance()
            .addLifecycleCallback { activity, stage ->
                if (activity is MainActivity && stage == Stage.RESUMED) {
                    (activity as MainActivity).uploadImage(
                        Uri.fromFile(
                            File(
                                "file://android_asset/ocean.jpg"
                            )
                        )
                    )
                } else if (activity is DetailsActivity && stage == Stage.RESUMED) {
                    Assert.assertEquals(
                        "Rimini",
                        Constants.PICKED_CITY_EN
                    )
                }
            }
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.moveToState(Lifecycle.State.CREATED)
        setToken(
            ApplicationProvider.getApplicationContext(),
            API_KEY_VISION
        )
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test(timeout = MIN_TIMEOUT)
    fun sochiCity() {
        val scenario = launch(MainActivity::class.java)
        ActivityLifecycleMonitorRegistry.getInstance()
            .addLifecycleCallback { activity, stage ->
                if (activity is MainActivity && stage == Stage.RESUMED) {
                    (activity as MainActivity).uploadImage(
                        Uri.fromFile(
                            File(
                                "file://android_asset/sochi.jpg"
                            )
                        )
                    )
                } else if (activity is DetailsActivity && stage == Stage.RESUMED) {
                    Assert.assertEquals(
                        "Sochi",
                        Constants.PICKED_CITY_EN
                    )
                }
            }
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.moveToState(Lifecycle.State.CREATED)
        setToken(
            ApplicationProvider.getApplicationContext(),
            API_KEY_VISION
        )
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test(timeout = MIN_TIMEOUT)
    fun snow() {
        val scenario = launch(MainActivity::class.java)
        ActivityLifecycleMonitorRegistry.getInstance()
            .addLifecycleCallback { activity, stage ->
                if (activity is MainActivity && stage == Stage.RESUMED) {
                    (activity as MainActivity).uploadImage(
                        Uri.fromFile(
                            File(
                                "file://android_asset/snow.jpg"
                            )
                        )
                    )
                } else if (activity is DetailsActivity && stage == Stage.RESUMED) {
                    Assert.assertEquals(
                        "хельсинки",
                        Constants.PICKED_CITY_RU
                    )
                }
            }
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.moveToState(Lifecycle.State.CREATED)
        setToken(
            ApplicationProvider.getApplicationContext(),
            API_KEY_VISION
        )
        scenario.moveToState(Lifecycle.State.RESUMED)
    }


    @Test(timeout = MIN_TIMEOUT)
    fun checkMoscowCityPrice() {
        Constants.PICKED_CITY_EN = "Moscow"
        ActivityLifecycleMonitorRegistry.getInstance()
            .addLifecycleCallback { activity, stage ->
                if (activity is DetailsActivity && stage == Stage.RESUMED) {
                    activity.findViewById<TextView>(R.id.airticketTV)
                        .addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {}

                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                Assert.assertEquals(
                                    s.toString(),
                                    activity.data["Билеты на самолет"].toString()
                                )
                            }

                        })
                }
                if (activity is ChipActivity && stage == Stage.CREATED) {
                    Assert.fail("Didn't show price. ChipActivity has started.")
                }
            }
        val scenario = launch(DetailsActivity::class.java)
    }

    @Test(timeout = MIN_TIMEOUT)
    fun checkMoscowCityPriceSecondaryActivity() {
        Constants.PICKED_CITY_EN = "Moscow"
        ActivityLifecycleMonitorRegistry.getInstance()
            .addLifecycleCallback { activity, stage ->
                if (activity is ChipActivity && stage == Stage.RESUMED) {
                    activity.findViewById<TextView>(R.id.airticketTV)
                        .addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {}

                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                Assert.assertTrue(s.toString().matches(Regex(".*\\d.*")))
                            }

                        })
                }
            }
        val scenario = launch(ChipActivity::class.java)
    }
}