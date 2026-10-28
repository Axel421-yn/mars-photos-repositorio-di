package com.example.marsphotos

import com.example.marsphotos.data.MarsPhotosRepository
import com.example.marsphotos.fake.FakeNetworkMarsPhotosRepository
import com.example.marsphotos.fake.FakeDataSource
import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.ui.screens.MarsUiState
import com.example.marsphotos.ui.screens.MarsViewModel
import java.io.IOException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class MarsViewModelTest {
    @get:Rule val dispatcherRule = TestDispatcherRule()

    @Test fun marsViewModel_getMarsPhotos_verifyMarsUiStateSuccess() = runTest {
        val model = MarsViewModel(FakeNetworkMarsPhotosRepository())
        assertEquals(MarsUiState.Success("Success: 2 Mars photos retrieved"), model.marsUiState)
    }
    @Test fun loadingIsVisibleUntilRepositoryCompletes() = runTest {
        val pending = CompletableDeferred<List<MarsPhoto>>()
        val model = MarsViewModel(object : MarsPhotosRepository {
            override suspend fun getMarsPhotos() = pending.await()
        })
        assertEquals(MarsUiState.Loading, model.marsUiState)
        pending.complete(FakeDataSource.photosList)
        assertEquals(MarsUiState.Success("Success: 2 Mars photos retrieved"), model.marsUiState)
    }
    @Test fun ioFailureBecomesError() = runTest {
        val model = MarsViewModel(object : MarsPhotosRepository {
            override suspend fun getMarsPhotos(): List<MarsPhoto> = throw IOException("Sin conexion")
        })
        assertEquals(MarsUiState.Error, model.marsUiState)
    }
    @Test fun httpFailureBecomesError() = runTest {
        val model = MarsViewModel(object : MarsPhotosRepository {
            override suspend fun getMarsPhotos(): List<MarsPhoto> =
                throw HttpException(Response.error<List<MarsPhoto>>(503, "".toResponseBody()))
        })
        assertEquals(MarsUiState.Error, model.marsUiState)
    }
    @Test fun requestCanRecoverAfterFailure() = runTest {
        var fails = true
        val model = MarsViewModel(object : MarsPhotosRepository {
            override suspend fun getMarsPhotos(): List<MarsPhoto> {
                if (fails) throw IOException("Sin conexion")
                return FakeDataSource.photosList
            }
        })
        assertEquals(MarsUiState.Error, model.marsUiState)
        fails = false
        model.getMarsPhotos()
        assertEquals(MarsUiState.Success("Success: 2 Mars photos retrieved"), model.marsUiState)
    }
}
