package com.example.marsphotos

import com.example.marsphotos.data.NetworkMarsPhotosRepository
import com.example.marsphotos.fake.FakeDataSource
import com.example.marsphotos.fake.FakeMarsApiService
import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.network.MarsApiService
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.fail
import org.junit.Test

class NetworkMarsRepositoryTest {
    @Test fun networkMarsPhotosRepository_getMarsPhotos_verifyPhotoList() = runTest {
        assertEquals(FakeDataSource.photosList, NetworkMarsPhotosRepository(FakeMarsApiService()).getMarsPhotos())
    }
    @Test fun emptyResponseIsPreserved() = runTest {
        val api = object : MarsApiService {
            override suspend fun getPhotos() = emptyList<MarsPhoto>()
        }
        assertEquals(emptyList<MarsPhoto>(), NetworkMarsPhotosRepository(api).getMarsPhotos())
    }
    @Test fun networkFailureIsPropagated() = runTest {
        val error = IOException("Sin conexion")
        val api = object : MarsApiService {
            override suspend fun getPhotos(): List<MarsPhoto> = throw error
        }
        try {
            NetworkMarsPhotosRepository(api).getMarsPhotos()
            fail("Se esperaba IOException")
        } catch (actual: IOException) {
            assertSame(error, actual)
        }
    }
}
