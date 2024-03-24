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
            id = movieDetailsDto.id,
            title = movieDetailsDto.title,
            releaseDate = movieDetailsDto.releaseDate,
            runtime = movieDetailsDto.runtime,
            genres = movieDetailsDto.genres.map { toGenre(it) },
            posterPath = movieDetailsDto.posterPath,
            overview = movieDetailsDto.overview,
        )
    }

    fun toTvShowDetails(tvShowDetailsDto: TvShowDetailsDto): TvShowDetails {
        return TvShowDetails(
            id = tvShowDetailsDto.id,
            name = tvShowDetailsDto.name,
            firstAirDate = tvShowDetailsDto.firstAirDate,
            genres = tvShowDetailsDto.genres.map { toGenre(it) },
            posterPath = tvShowDetailsDto.posterPath,
            overview = tvShowDetailsDto.overview,
            episodes = tvShowDetailsDto.episodes,
            seasons = tvShowDetailsDto.seasons,
        )
    }

    fun toCredits(creditsDto: CreditsDto): Credits {
        return Credits(
            id = creditsDto.id,
            cast = creditsDto.cast.map { toCast(it) },
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
            id = genreDto.id,
            name = genreDto.name,
        )
    }

    private fun toCast(cast: CastDto): Cast {
        return Cast(name = cast.name)
    }
}