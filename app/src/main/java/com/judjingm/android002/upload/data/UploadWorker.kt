package com.judjingm.android002.upload.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import app.cashadvisor.common.utill.extensions.logDebugMessage
import com.judjingm.android002.R
import com.judjingm.android002.upload.data.api.FileApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@HiltWorker
class UploadWorker @AssistedInject constructor(
    private val fileRemoteDataSource: FileApiService,
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun doWork(): Result {
        return try {
            val uri = inputData.getString(URI) ?: return Result.failure()
            val fileName = inputData.getString(FILE_NAME) ?: return Result.failure()

            setForeground(
                createForegroundInfo(
                    appContext.getString(
                        R.string.uploading_content,
                        fileName
                    )
                )
            )
            uploadFile(uri = uri.toUri(), fileName = fileName)
            Result.success()
        } catch (exception: Exception) {
            logDebugMessage("worker crash $exception")
            val errorMessage = prepareOutputData(exception.message.orEmpty())
            Result.failure(errorMessage)
        }
    }

    private suspend fun uploadFile(uri: Uri, fileName: String) {
        val file = File(uri.path)
        val name = fileName.ifBlank { file.name }
        fileRemoteDataSource.uploadDocument(
            MultipartBody.Part.createFormData(
                "file", name, file.asRequestBody()
            )
        )
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val channelId = appContext.getString(R.string.channel_id_main)
        val title = appContext.getString(R.string.uploading_pdf_to_server)
        val cancel = appContext.getString(R.string.cancel)
        val notificationId = NOTIFICATION_ID
        val intent = WorkManager.getInstance(appContext)
            .createCancelPendingIntent(id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(R.drawable.ic_movies_film)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setProgress(0, 0, true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(notificationId, notification, FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else ForegroundInfo(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val existingChannel =
            notificationManager.getNotificationChannel(appContext.getString(R.string.channel_id_main))
        if (existingChannel == null) {
            val name = appContext.getString(R.string.default_chanel)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                appContext.getString(R.string.channel_id_main),
                name,
                importance
            )
            mChannel.description = appContext.getString(R.string.notificationDescription)
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun prepareOutputData(message: String): Data {
        val dataBuilder = Data.Builder()
        dataBuilder.putString(ERROR_MESSAGE, message).build()
        return dataBuilder.build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val URI = "uri"
        private const val FILE_NAME = "file_name"
        private const val ERROR_MESSAGE = "error_message"
    }
}