package com.example.marsphotos.fake

import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.network.MarsApiService
import com.example.marsphotos.data.MarsPhotosRepository

object FakeDataSource {
    val photosList = listOf(MarsPhoto("img1", "url.1"), MarsPhoto("img2", "url.2"))
}
class FakeMarsApiService : MarsApiService {
    override suspend fun getPhotos() = FakeDataSource.photosList
}
class FakeNetworkMarsPhotosRepository : MarsPhotosRepository {
    override suspend fun getMarsPhotos() = FakeDataSource.photosList
}
