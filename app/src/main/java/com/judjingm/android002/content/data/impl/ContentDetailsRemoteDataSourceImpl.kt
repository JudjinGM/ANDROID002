package com.judjingm.android002.content.data.impl

import com.judjingm.android002.common.utill.exceptions.NetworkException
import com.judjingm.android002.content.data.ContentDetailDataMapper
import com.judjingm.android002.content.data.NetworkToContentDetailExceptionMapper
import com.judjingm.android002.content.data.api.ContentDetailsApiService
import com.judjingm.android002.content.data.api.ContentDetailsRemoteDataSource
import com.judjingm.android002.content.data.models.dto.CreditsDto
import com.judjingm.android002.content.data.models.dto.MovieDetailsDto
import com.judjingm.android002.content.data.models.dto.TvShowDetailDto
import javax.inject.Inject

class ContentDetailsRemoteDataSourceImpl @Inject constructor(
    private val contentDetailsApiService: ContentDetailsApiService,
    private val contentDetailDataMapper: ContentDetailDataMapper,
    private val networkToContentDetailExceptionMapper: NetworkToContentDetailExceptionMapper


) : ContentDetailsRemoteDataSource {
    override fun getMovieDetail(movieId: Int): MovieDetailsDto {
        return try {
            val response = contentDetailsApiService.getMovieDetail(movieId)
            contentDetailDataMapper.toMovieDetailDto(response)
        } catch (exception: NetworkException) {
            throw networkToContentDetailExceptionMapper.handleException(exception)
        }
    }

    override fun getTvShowDetail(seriesId: Int): TvShowDetailDto {
        return try {
            val response = contentDetailsApiService.getTvShowDetail(seriesId)
            contentDetailDataMapper.toTVShowDetailDto(response)
        } catch (exception: NetworkException) {
            throw networkToContentDetailExceptionMapper.handleException(exception)
        }
    }

    override fun getMovieCredits(movieId: Int): CreditsDto {
        return try {
            val response = contentDetailsApiService.getMovieCredits(movieId)
            contentDetailDataMapper.toCreditsDto(response)
        } catch (exception: NetworkException) {
            throw networkToContentDetailExceptionMapper.handleException(exception)
        }
    }

    override fun getTvShowCredits(seriesId: Int): CreditsDto {
        return try {
            val response = contentDetailsApiService.getMovieCredits(seriesId)
            contentDetailDataMapper.toCreditsDto(response)
        } catch (exception: NetworkException) {
            throw networkToContentDetailExceptionMapper.handleException(exception)
        }
    }

}