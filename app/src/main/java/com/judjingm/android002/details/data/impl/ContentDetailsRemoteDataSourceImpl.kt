package com.judjingm.android002.details.data.impl

import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.details.data.ContentDetailDataMapper
import com.judjingm.android002.details.data.NetworkToContentDetailExceptionMapper
import com.judjingm.android002.details.data.api.ContentDetailsApiService
import com.judjingm.android002.details.data.api.ContentDetailsRemoteDataSource
import com.judjingm.android002.details.data.models.dto.CreditsDto
import com.judjingm.android002.details.data.models.dto.CreditsQueryDto
import com.judjingm.android002.details.data.models.dto.MovieDetailsDto
import com.judjingm.android002.details.data.models.dto.MovieDetailsQueryDto
import com.judjingm.android002.details.data.models.dto.TvShowDetailsDto
import com.judjingm.android002.details.data.models.dto.TvShowDetailsQueryDto
import javax.inject.Inject

class ContentDetailsRemoteDataSourceImpl @Inject constructor(
    private val contentDetailsApiService: ContentDetailsApiService,
    private val contentDetailDataMapper: ContentDetailDataMapper,
    private val networkToContentDetailExceptionMapper: NetworkToContentDetailExceptionMapper
) : ContentDetailsRemoteDataSource {
    override suspend fun getMovieDetail(movieDetailsQueryDto: MovieDetailsQueryDto): MovieDetailsDto {
        return try {
            val response = contentDetailsApiService.getMovieDetail(
                movieDetailsQueryDto.movieId,
                movieDetailsQueryDto.language
            )
            contentDetailDataMapper.toMovieDetailDto(response)
        } catch (exception: NetworkException) {
            throw networkToContentDetailExceptionMapper.handleException(exception)
        }
    }

    override suspend fun getTvShowDetail(tvShowDetailsQueryDto: TvShowDetailsQueryDto): TvShowDetailsDto {
        return try {
            val response = contentDetailsApiService.getTvShowDetail(
                tvShowDetailsQueryDto.seriesId,
                tvShowDetailsQueryDto.language
            )
            contentDetailDataMapper.toTVShowDetailDto(response)
        } catch (exception: NetworkException) {
            throw networkToContentDetailExceptionMapper.handleException(exception)
        }
    }

    override suspend fun getMovieCredits(creditsQueryDto: CreditsQueryDto): CreditsDto {
        return try {
            val response = contentDetailsApiService.getMovieCredits(
                creditsQueryDto.contentId,
                creditsQueryDto.language
            )
            contentDetailDataMapper.toCreditsDto(response)
        } catch (exception: NetworkException) {
            throw networkToContentDetailExceptionMapper.handleException(exception)
        }
    }

    override suspend fun getTvShowCredits(creditsQueryDto: CreditsQueryDto): CreditsDto {
        return try {
            val response = contentDetailsApiService.getTvShowCredits(
                creditsQueryDto.contentId,
                creditsQueryDto.language
            )
            contentDetailDataMapper.toCreditsDto(response)
        } catch (exception: NetworkException) {
            throw networkToContentDetailExceptionMapper.handleException(exception)
        }
    }

}