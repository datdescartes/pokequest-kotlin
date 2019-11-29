package com.arya21.pokequest.domain
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import se.ansman.kotshi.JsonSerializable


@JsonSerializable
data class Page<T>(
    val offset: Int,
    val count: Int,
    val total: Int,
    val `data`: List<T>
)

fun <T> Page<T>.hasMore(): Boolean = offset + count < total

@Parcelize
@JsonSerializable
data class Monster(
    val id: Int,
    val name: String,
    @Json(name = "full_image_url")
    val imageUrl: String
): Parcelable

@JsonSerializable
data class MonsterDetail(
    val id: Int,
    val name: String,
    val species: String,
    val height: String,
    val weight: String,
    @Json(name = "ev_yield")
    val evYield: String,
    @Json(name = "catch_rate")
    val catchRate: Int,
    val happiness: Int,
    val exp: Int,
    @Json(name = "growth_rate")
    val growthRate: String,
    @Json(name = "male_female_ratio")
    val maleFemaleRatio: String,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    @Json(name = "sp_atk")
    val spAtk: Int,
    @Json(name = "sp_def")
    val spDef: Int,
    val speed: Int,
    val total: Int,
    @Json(name = "egg_cycles")
    val eggCycles: Int,
    val abilities: List<String>,
    @Json(name = "full_image_url")
    val imageUrl: String,
    @Json(name = "evol_from")
    val evolFrom: List<Evolution> = listOf(),
    @Json(name = "evol_to")
    val evolTo: List<Evolution> = listOf()
)

@JsonSerializable
data class Evolution(
    val id: Int,
    @Json(name = "from_poke")
    val fromPoke: Monster?,
    @Json(name = "to_poke")
    val toPoke: Monster?,
    val level: Int,
    val method: String
)