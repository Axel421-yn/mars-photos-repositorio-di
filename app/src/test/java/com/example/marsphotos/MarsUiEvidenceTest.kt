package com.example.marsphotos

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText

import com.example.marsphotos.data.AppContainer
import com.example.marsphotos.data.MarsPhotosRepository
import com.example.marsphotos.fake.FakeNetworkMarsPhotosRepository
import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.ui.MarsPhotosApp
import com.example.marsphotos.ui.theme.MarsPhotosTheme
import java.io.File
import java.io.IOException
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Runs the real Compose UI on the local JVM, not on an Android emulator. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp-mdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class MarsUiEvidenceTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private fun showApp(repository: MarsPhotosRepository) {
        (RuntimeEnvironment.getApplication() as MarsPhotosApplication).container =
            object : AppContainer {
                override val marsPhotosRepository = repository
            }
        compose.setContent { MarsPhotosTheme { MarsPhotosApp() } }
    }

    private fun saveEvidence(name: String) {
        val file = File("build/evidence/$name.png")
        file.parentFile.mkdirs()
        compose.runOnIdle {
            val view = compose.activity.window.decorView
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            view.draw(Canvas(bitmap))
            file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
    }

    @Test fun successScreenUsesInjectedRepository() {
        showApp(FakeNetworkMarsPhotosRepository())
        compose.onNodeWithText("Success: 2 Mars photos retrieved").assertIsDisplayed()
        saveEvidence("ui-success-local-jvm")
    }

    @Test fun errorScreenShowsNetworkFailure() {
        showApp(object : MarsPhotosRepository {
            override suspend fun getMarsPhotos(): List<MarsPhoto> =
                throw IOException("Controlled network failure")
        })
        compose.onNodeWithText("Failed to load").assertIsDisplayed()
        saveEvidence("ui-error-local-jvm")
    }
}


