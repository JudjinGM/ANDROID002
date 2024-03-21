package com.judjingm.android002.content.presentation.ui

import android.content.Context
import com.judjingm.android002.R
import com.judjingm.android002.content.domain.models.Credits
import com.judjingm.android002.content.domain.models.MovieDetails
import com.judjingm.android002.content.domain.models.TvShowDetails
import com.judjingm.android002.content.presentation.models.ContentDetailsUi
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ContentDetailsDomainToUiMapper @Inject constructor(@ApplicationContext private val context: Context) {
    fun toContentDetailsUi(movieDetails: MovieDetails, credits: Credits): ContentDetailsUi {
        return ContentDetailsUi(
            id = movieDetails.id,
            title = movieDetails.title,
            posterPath = movieDetails.posterPath,
            overview = movieDetails.overview,
            releaseDate = movieDetails.releaseDate.substring(0, 4),
            runtime = buildString {
                append(
                    context.resources.getQuantityString(
                        R.plurals.minutes, movieDetails.runtime, movieDetails.runtime
                    )
                )
            },
            genres = movieDetails.genres.joinToString { it.name }
                .replaceFirstChar { it.uppercase() },
            cast = credits.cast.joinToString { it.name }
                .replaceFirstChar { it.uppercase() },
        )
    }

    fun toContentDetailsUi(tvShowDetails: TvShowDetails, credits: Credits): ContentDetailsUi {
        val runtime = buildString {
            append(
                context.resources.getQuantityString(
                    R.plurals.season_plural, tvShowDetails.seasons, tvShowDetails.seasons
                )
            )
            append(", ")
            append(
                context.resources.getQuantityString(
                    R.plurals.episode_plural, tvShowDetails.episodes, tvShowDetails.episodes
                )
            )
        }

        return ContentDetailsUi(
            id = tvShowDetails.id,
            title = tvShowDetails.name,
            posterPath = tvShowDetails.posterPath,
            overview = tvShowDetails.overview,
            releaseDate = tvShowDetails.firstAirDate.substring(0, 4),
            runtime = runtime,
            genres = tvShowDetails.genres.joinToString { it.name }
                .replaceFirstChar { it.uppercase() },
            cast = credits.cast.joinToString { it.name },
        )
    }
}