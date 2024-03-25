package com.judjingm.android002.common.domain

import com.judjingm.android002.common.data.models.MovieDto
import com.judjingm.android002.common.data.models.TvShowDto
import com.judjingm.android002.common.domain.models.Content
import com.judjingm.android002.home.data.models.dto.PopularMoviesQueryDto
import com.judjingm.android002.home.data.models.dto.PopularTVShowsQueryDto
import com.judjingm.android002.home.domain.models.PopularMoviesQuery
import com.judjingm.android002.home.domain.models.PopularTVShowsQuery
import com.judjingm.android002.search.data.models.dto.SearchMoviesQueryDto
import com.judjingm.android002.search.data.models.dto.SearchTvShowsQueryDto
import com.judjingm.android002.search.domain.models.SearchMoviesQuery
import com.judjingm.android002.search.domain.models.SearchTvShowsQuery
import javax.inject.Inject

class CommonContentDomainMapper @Inject constructor() {
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
    fun toSearchMoviesQueryDto(searchMoviesQuery: SearchMoviesQuery): SearchMoviesQueryDto {
        return SearchMoviesQueryDto(
            query = searchMoviesQuery.query,
            page = searchMoviesQuery.page,
            language = searchMoviesQuery.language,
            region = searchMoviesQuery.region
        )
    }

    fun toSearchTvShowsQueryDto(searchTVShowsQuery: SearchTvShowsQuery): SearchTvShowsQueryDto {
        return SearchTvShowsQueryDto(
            query = searchTVShowsQuery.query,
            page = searchTVShowsQuery.page,
            language = searchTVShowsQuery.language,
        )
    }


    fun toMovie(movieDto: MovieDto): Content.Movie {
        return Content.Movie(
            id = movieDto.id,
            title = movieDto.title,
            releaseDate = movieDto.releaseDate,
            posterPath = movieDto.posterPath,
            voteAverage = movieDto.voteAverage,
        )
    }

    fun toTVShow(tvShowDto: TvShowDto): Content.TVShow {
        return Content.TVShow(
            id = tvShowDto.id,
            name = tvShowDto.name,
            firstAirDate = tvShowDto.firstAirDate,
            posterPath = tvShowDto.posterPath,
            voteAverage = tvShowDto.voteAverage,
        )
    }

}