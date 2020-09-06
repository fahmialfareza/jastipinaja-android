package com.dinokeylas.jastipinaja.model


data class ObjectStorageResponse(
    val message: String = "",
    val data: Data = Data()
)

data class Data(
    val bucketName: String = "",
    val name: String = ""
)