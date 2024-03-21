package com.judjingm.android002.content.domain

import com.judjingm.android002.content.data.models.dto.CastDto
import com.judjingm.android002.content.data.models.dto.CreditsDto
import com.judjingm.android002.content.data.models.dto.GenreDto
import com.judjingm.android002.content.data.models.dto.MovieDetailsDto
import com.judjingm.android002.content.data.models.dto.TvShowDetailDto
import com.judjingm.android002.content.domain.models.Cast
import com.judjingm.android002.content.domain.models.Credits
import com.judjingm.android002.content.domain.models.Genre
import com.judjingm.android002.content.domain.models.MovieDetails
import com.judjingm.android002.content.domain.models.TvShowDetails

class ContentDetailsDomainMapper {
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

    fun toTvShowDetails(tvShowDetailDto: TvShowDetailDto): TvShowDetails {
        return TvShowDetails(
            id = tvShowDetailDto.id,
            name = tvShowDetailDto.name,
            firstAirDate = tvShowDetailDto.firstAirDate,
            genres = tvShowDetailDto.genres.map { toGenre(it) },
            posterPath = tvShowDetailDto.posterPath,
            overview = tvShowDetailDto.overview,
            episodes = tvShowDetailDto.episodes,
            seasons = tvShowDetailDto.seasons,
        )
    }

    fun toCredits(creditsDto: CreditsDto): Credits {
        return Credits(
            id = creditsDto.id,
            cast = creditsDto.cast.map { toCast(it) },
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