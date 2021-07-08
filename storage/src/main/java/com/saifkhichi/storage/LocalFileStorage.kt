package com.saifkhichi.storage

import android.content.Context
import java.io.File

class LocalFileStorage(context: Context, folder: String = "") {

    private val root: File = File(context.filesDir, folder)

    fun createEmptyFile(filename: String): Result<File> {
        return try {
            val file = File(root, filename)
            if (!file.exists()) {
                file.parent?.let { File(it).mkdirs() }
                file.createNewFile()
            }
            Result.success(file)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    /**
     * Download a file from the local storage.
     *
     * @param fn The fully-qualified name of the file.
     * @return A [Result] containing the requested file if it exists, or an error message.
     */
    fun download(fn: String): Result<File> {
        return try {
            val file = File(root, fn)
            if (file.exists()) Result.success(file) else throw NullPointerException()
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    fun contains(filename: String): Boolean = download(filename).isSuccess

}