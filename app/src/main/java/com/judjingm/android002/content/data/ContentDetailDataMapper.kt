package com.judjingm.android002.content.data

import com.judjingm.android002.content.data.models.dto.CastDto
import com.judjingm.android002.content.data.models.dto.CreditsDto
import com.judjingm.android002.content.data.models.dto.GenreDto
import com.judjingm.android002.content.data.models.dto.MovieDetailsDto
import com.judjingm.android002.content.data.models.dto.TvShowDetailDto
import com.judjingm.android002.content.data.models.response.CastResponse
import com.judjingm.android002.content.data.models.response.CreditsResponse
import com.judjingm.android002.content.data.models.response.GenresResponse
import com.judjingm.android002.content.data.models.response.MovieDetailResponse
import com.judjingm.android002.content.data.models.response.TvShowDetailResponse
import javax.inject.Inject

class ContentDetailDataMapper @Inject constructor() {
    fun toMovieDetailDto(movie: MovieDetailResponse): MovieDetailsDto {
        return MovieDetailsDto(
            id = movie.id,
            title = movie.title,
            releaseDate = movie.releaseDate,
            runtime = movie.runtime,
            genres = movie.genres.map { toGenresDto(it) },
            posterPath = movie.posterPath,
            overview = movie.overview,
        )
    }

    fun toTVShowDetailDto(tvShow: TvShowDetailResponse): TvShowDetailDto {
        return TvShowDetailDto(
            id = tvShow.id,
            name = tvShow.name,
            firstAirDate = tvShow.firstAirDate,
            genres = tvShow.genres.map { toGenresDto(it) },
            posterPath = tvShow.posterPath,
            overview = tvShow.overview,
            episodes = tvShow.episodes,
            seasons = tvShow.seasons,
        )
    }

    fun toCreditsDto(credits: CreditsResponse): CreditsDto {
        return CreditsDto(
            id = credits.id,
            cast = credits.cast.map { toCastDto(it) },
        )
    }

    private fun toGenresDto(genres: GenresResponse): GenreDto {
        return GenreDto(
            id = genres.id,
            name = genres.name,
        )
    }

    private fun toCastDto(cast: CastResponse): CastDto {
        return CastDto(
            name = cast.name,
        )
    }
}