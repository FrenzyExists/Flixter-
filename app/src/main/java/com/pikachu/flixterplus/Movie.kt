package com.pikachu.flixterplus

import com.google.gson.annotations.SerializedName

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
class GenericMovieListResponse (
    @SerializedName("result")
    val response: List<MovieGeneric>?
)


class MovieGeneric(
    @SerializedName("title")
     var title: String,

    @SerializedName("overview")
     var overview: String?,

    @SerializedName("id")
     var id: String?,

    @SerializedName("release_date")
     var releaseDate: String?,

    @SerializedName("vote_average")
     var voteAverage: String?,

    @SerializedName("vote_count")
     var voteCount: String?,

    @SerializedName("poster_path")
    var posterPath: String?,

    @SerializedName("backdrop_path")
    var backdropPath: String?
)

class Categories(
    @SerializedName("id")
    var id: String?,

    @SerializedName("name")
    var name: String?,
)


class MovieDetails(
    @SerializedName("budget")
    val budget: String?,

    @SerializedName("revenue")
    val revenue: String?,

    @SerializedName("runtime")
    val runtime: String?,
) : IMovie()

