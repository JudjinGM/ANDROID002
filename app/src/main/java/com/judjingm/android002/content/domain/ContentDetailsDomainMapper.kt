package com.judjingm.android002.content.domain

import com.judjingm.android002.content.data.models.dto.CastDto
import com.judjingm.android002.content.data.models.dto.CreditsDto
import com.judjingm.android002.content.data.models.dto.CreditsQueryDto
import com.judjingm.android002.content.data.models.dto.GenreDto
import com.judjingm.android002.content.data.models.dto.MovieDetailsDto
import com.judjingm.android002.content.data.models.dto.MovieDetailsQueryDto
import com.judjingm.android002.content.data.models.dto.TvShowDetailsDto
import com.judjingm.android002.content.data.models.dto.TvShowDetailsQueryDto
import com.judjingm.android002.content.domain.models.Cast
import com.judjingm.android002.content.domain.models.Credits
import com.judjingm.android002.content.domain.models.CreditsQuery
import com.judjingm.android002.content.domain.models.Genre
import com.judjingm.android002.content.domain.models.MovieDetails
import com.judjingm.android002.content.domain.models.MovieDetailsQuery
import com.judjingm.android002.content.domain.models.TvShowDetails
import com.judjingm.android002.content.domain.models.TvShowDetailsQuery
import javax.inject.Inject

class ContentDetailsDomainMapper @Inject constructor() {
    fun toMovieDetails(movieDetailsDto: MovieDetailsDto): MovieDetails {
        return MovieDetails(
            id = movieDetailsDto.id ?: DEFAULT_ID,
            title = movieDetailsDto.title ?: BLANC_STRING,
            releaseDate = movieDetailsDto.releaseDate ?: BLANC_STRING,
            runtime = movieDetailsDto.runtime ?: DEFAULT_RUNTIME,
            genres = movieDetailsDto.genres?.map { toGenre(it) } ?: emptyList(),
            posterPath = movieDetailsDto.posterPath ?: BLANC_STRING,
            overview = movieDetailsDto.overview ?: BLANC_STRING,
        )
    }

    fun toTvShowDetails(tvShowDetailsDto: TvShowDetailsDto): TvShowDetails {
        return TvShowDetails(
            id = tvShowDetailsDto.id ?: DEFAULT_ID,
            name = tvShowDetailsDto.name ?: BLANC_STRING,
            firstAirDate = tvShowDetailsDto.firstAirDate ?: BLANC_STRING,
            genres = tvShowDetailsDto.genres?.map { toGenre(it) } ?: emptyList(),
            posterPath = tvShowDetailsDto.posterPath ?: BLANC_STRING,
            overview = tvShowDetailsDto.overview ?: BLANC_STRING,
            episodes = tvShowDetailsDto.episodes ?: DEFAULT_EPISODES,
            seasons = tvShowDetailsDto.seasons ?: DEFAULT_SEASON,
        )
    }

    fun toCredits(creditsDto: CreditsDto): Credits {
        return Credits(
            id = creditsDto.id ?: DEFAULT_ID,
            cast = creditsDto.cast?.map { toCast(it) } ?: emptyList(),
        )
    }

    fun toMovieDetailsQueryDto(movieDetailsQuery: MovieDetailsQuery): MovieDetailsQueryDto {
        return MovieDetailsQueryDto(
            movieId = movieDetailsQuery.movieId,
            language = movieDetailsQuery.language,
        )
    }

    fun toTvShowDetailsQueryDto(tvShowDetailsQuery: TvShowDetailsQuery): TvShowDetailsQueryDto {
        return TvShowDetailsQueryDto(
            seriesId = tvShowDetailsQuery.seriesId,
            language = tvShowDetailsQuery.language,
        )
    }

    fun toCreditsQueryDto(creditsQuery: CreditsQuery): CreditsQueryDto {
        return CreditsQueryDto(
            contentId = creditsQuery.contentId,
            language = creditsQuery.language,
        )
    }


    private fun toGenre(genreDto: GenreDto): Genre {
        return Genre(
            id = genreDto.id ?: DEFAULT_ID,
            name = genreDto.name ?: BLANC_STRING,
        )
    }

    private fun toCast(cast: CastDto): Cast {
        return Cast(
            name = cast.name ?: BLANC_STRING,
        )
    }

    companion object {
        const val DEFAULT_ID = -1
        const val BLANC_STRING = ""
        const val DEFAULT_RUNTIME = 0
        const val DEFAULT_EPISODES = 0
        const val DEFAULT_SEASON = 0
    }
}