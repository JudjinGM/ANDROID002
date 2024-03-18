package com.judjingm.android002.home.domain

import com.judjingm.android002.common.domain.PagedList
import com.judjingm.android002.home.data.models.dto.MovieDto
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVShowsQueryDto
import com.judjingm.android002.home.data.models.dto.TvShowDto
import com.judjingm.android002.home.domain.models.Movie
import com.judjingm.android002.home.domain.models.PopularMoviesQuery
import com.judjingm.android002.home.domain.models.PopularTVShowsQuery
import com.judjingm.android002.home.domain.models.TVShow

class PopularContentDomainMapper {
    fun <Data, Domain> toPagedList(
        pagedList: PagedList<Data>,
        convertFunc: (Data) -> Domain
    ): PagedList<Domain> = with(pagedList) {
        PagedList(
            totalElements = totalElements,
            totalPages = totalPages,
            currentPage = currentPage,
            content = content.map(convertFunc)
        )
    }

    fun toPopularMoviesQueryDto(popularMoviesQuery: PopularMoviesQuery): PopularMoviesQueryDto {
        return PopularMoviesQueryDto(
            page = popularMoviesQuery.page,
            language = popularMoviesQuery.language,
            region = popularMoviesQuery.region
        )
    }

    fun toPopularTVShowsQueryDto(popularTVShowsQuery: PopularTVShowsQuery): PopularTVShowsQueryDto {
        return PopularTVShowsQueryDto(
            page = popularTVShowsQuery.page,
            language = popularTVShowsQuery.language,
        )
    }

    fun toMovie(movieDto: MovieDto): Movie {
        return Movie(
            id = movieDto.id,
            title = movieDto.title,
            releaseDate = movieDto.releaseDate,
            posterPath = movieDto.posterPath,
        )
    }

    fun toTVShow(tvShowDto: TvShowDto): TVShow {
        return TVShow(
            id = tvShowDto.id,
            name = tvShowDto.name,
            firstAirDate = tvShowDto.firstAirDate,
            posterPath = tvShowDto.posterPath,
        )
    }

}