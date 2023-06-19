package com.mobile.taipeitour.model.attraction

data class AttractionsModel (
    val total: Int,
    val `data`: List<AttractionData>
)

data class AttractionData(
    val id: Int,
    val name: String,
    val name_zh: String,
    val open_status: Int,
    val introduction: String,
    val open_time: String,
    val zipcode: String,
    val distric: String,
    val address: String,
    val tel: String,
    val fax: String,
    val email: String,
    val months: String,
    val nlat: Double,
    val elong: Double,
    val official_site: String,
    val facebook: String,
    val ticket: String,
    val remind: String,
    val staytime: String,
    val modified: String,
    val url: String,
    val category: List<CategotyData>,
    val target: List<TargetData>,
    val service: List<ServiceData>,
    val friendly: List<FriendlyData>,
    val images: List<ImagesData>,
    val files: List<FilesData>,
    val links: List<LinksData>
)

data class CategotyData(
    val id: Int,
    val name: String
)

data class TargetData(
    val id: Int,
    val name: String
)

data class ServiceData(
    val id: Int,
    val name: String
)

data class FriendlyData(
    val id: Int,
    val name: String
)

data class ImagesData(
    val src: String,
    val subject: String,
    val ext: String
)

data class FilesData(
    val src: String,
    val subject: String,
    val ext: String
)

data class LinksData(
    val src: String,
    val subject: String
)

data class Menu(
    val icon: String,
    val text: String,
    val url: String,
    val type: Int
)