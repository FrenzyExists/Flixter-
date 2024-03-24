package com.pikachu.flixterplus

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

abstract class IMovie {
    open val title: String? = null
    open val overview: String? = null
    open val id: String? = null
    open val releaseDate: String? = null
    open val voteAverage: String? = null
    open val voteCount: String? = null
    open val posterPath: String? = null
    open val backdropPath: String? = null
}

/**
 * Works for:
 * - Latest Movies
 * - Most Popular Movies
 * - Top Rated Movies
 * - Upcoming
 * - Could also work for People but I'll abstract that later
 */
@Keep
@Serializable
class GenericMovieListResponse (
    @SerialName("result")
    val response: List<MovieGeneric>?
)

@Keep
@Serializable
class MovieGeneric(
    @SerialName("title")
    override val title: String,

    @SerialName("overview")
    override val overview: String?,

    @SerialName("id")
    override val id: String?,

    @SerialName("release_date")
    override val releaseDate: String?,

    @SerialName("vote_average")
    override val voteAverage: String?,

    @SerialName("vote_count")
    override val voteCount: String?,

    @SerialName("poster_path")
    override val posterPath: String?,

    @SerialName("backdrop_path")
    override val backdropPath: String?
) : IMovie(), java.io.Serializable

@Keep
@Serializable
class MovieDetails(
    @SerialName("budget")
    val budget: String?,

    @SerialName("revenue")
    val revenue: String?,

    @SerialName("runtime")
    val runtime: String?,
) : IMovie(), java.io.Serializable

