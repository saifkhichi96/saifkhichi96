package com.saifkhichi.storage

import android.content.Context
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File

/**
 * Manages files stored remotely on the cloud.
 *
 * This class provides an interface for performing I/O operations on files
 * in a cloud-based storage.
 *
 * @param context The application context.
 * @param scope Path of a subdirectory in remote storage.
 *
 * @property remote A folder in the remote file storage.
 * @property cache A local folder where files from [remote] are cached.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class CloudFileStorage(context: Context, scope: String) {

    private val remote: StorageReference = Firebase.storage.getReference(scope)
    private val cache: LocalFileStorage = LocalFileStorage(context, scope)

    /**
     * Download a file from the remote storage.
     *
     * Downloaded files are stored in the [cache]. On subsequent requests for the same
     * file, a cached copy might be returned unless explicitly asked for a fresh copy.
     *
     * @param fn The fully-qualified name of the file.
     * @param invalidate If true, fresh copy of file is downloaded from [remote]. Default is false.
     * @return A [Result] containing the requested file if it exists, or an error message.
     */
    suspend fun download(fn: String, invalidate: Boolean = false): Result<File> {
        return when (val cachedFile = cache.download(fn).getOrNull()) {
            null -> {
                val result = cache.createEmptyFile(fn)
                val tempFile = result.getOrNull()

                if (tempFile == null) result
                else downloadFromRemote(fn, tempFile)
            }
            else -> {
                // Invalidate cache if flag set or more than a week since last update
                if (cachedFile.requiresInvalidation() || invalidate) downloadFromRemote(fn, cachedFile)
                else Result.success(cachedFile)
            }
        }
    }

    /**
     * Lists all files at current location in the remote storage.
     *
     * @return A list of [StorageReference]s to all files at current location.
     */
    suspend fun listFiles(): List<StorageReference> {
        return kotlin.runCatching { remote.listAll().await().items }.getOrElse { emptyList() }
    }

    /**
     * Lists all folders at current location in the remote storage.
     *
     * @return A list of [StorageReference]s to all folders at current location.
     */
    suspend fun listFolders(): List<StorageReference> {
        return kotlin.runCatching { remote.listAll().await().prefixes }.getOrElse { emptyList() }
    }

    /**
     * Uploads a file to the remote storage.
     *
     * @param fn The fully-qualified name of the file.
     * @param data The contents of the file as a [ByteArray].
     * @return A [Result] containing the [StorageMetadata] if upload successful or error message on failure.
     */
    suspend fun upload(fn: String, data: ByteArray): Result<StorageMetadata> {
        return try {
            val snapshot = remote.child(fn).putBytes(data).await()
            Result.success(snapshot.metadata ?: throw snapshot.error ?: RuntimeException())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    /**
     * Uploads a file to the remote storage.
     *
     * @param fn The fully-qualified name of the file.
     * @param data The contents of the file as a [Uri].
     * @return A [Result] containing the [StorageMetadata] if upload successful or error message on failure.
     */
    suspend fun upload(fn: String, data: Uri): Result<StorageMetadata> {
        return try {
            val snapshot = remote.child(fn).putFile(data).await()
            Result.success(snapshot.metadata ?: throw snapshot.error ?: RuntimeException())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    /**
     * Downloads a file from the remote storage.
     *
     * @param fn The fully-qualified name of the file.
     * @param outfile The [File] where downloaded file contents will be saved.
     * @return A [Result] containing the requested file if it exists, or an error message.
     */
    private suspend fun downloadFromRemote(fn: String, outfile: File): Result<File> {
        return try {
            remote.child(fn).getFile(outfile).await()
            Result.success(outfile)
        } catch (ex: Exception) {
            outfile.delete()
            Result.failure(ex)
        }
    }

    private fun File.requiresInvalidation(): Boolean {
        val modified = this.lastModified()
        val now = System.currentTimeMillis()

        val days = (now - modified) / (1000 * 60 * 60 * 24L)
        return days > 7
    }

}