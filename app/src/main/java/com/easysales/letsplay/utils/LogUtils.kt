package com.easysales.letsplay.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import org.slf4j.LoggerFactory
import com.easysales.letsplay.utils.android.DeviceUtils
import com.easysales.letsplay.utils.android.FileUtils
import java.io.File
import java.io.IOException

import java.util.*

object LogUtils {
    /**
     * Logging tag used for common UI lifecycle events
     */
    val LOG_UI = "UI"

    /**
     * Logging tag used for any kind of network I/O communication
     */
    val LOG_NET = "NET"

    /**
     * Logging tag used for storage; local files, preferences and databases
     */
    val LOG_DATA = "DATA"

    /**
     * Logging tag used for business logic and app related things not
     * already covered by the other log tags
     */
    val LOG_APP = "APP"

    val FEATURE_JOB = "JOB"
    val FEATURE_GPS = "GPS"
    private val fileLog = LoggerFactory.getLogger(LogNames.FILE)

    fun dFile(tag: String, msg: String, vararg userTags: String) {
        val message = getFullMessage(msg, *userTags)
        Log.d(tag, message)
        fileLog.debug(String.format("%s: %s", tag, message))
    }

    fun d(tag: String, msg: String, vararg userTags: String) {
        val message = getFullMessage(msg, *userTags)
        Log.d(tag, message)
    }

//    fun vFile(tag: String, msg: String, vararg userTags: String) {
//        val message = getFullMessage(msg, *userTags)
//        Log.v(tag, message)
//        fileLog.trace(String.format("%s: %s", tag, message))
//    }

    fun v(tag: String, msg: String, vararg userTags: String) {
        val message = getFullMessage(msg, *userTags)
        Log.v(tag, message)
    }

    fun iFile(tag: String, msg: String, vararg userTags: String) {
        val message = getFullMessage(msg, *userTags)
        Log.i(tag, message)
        fileLog.info(String.format("%s: %s", tag, message))
    }

    fun i(tag: String, msg: String, vararg userTags: String) {
        val message = getFullMessage(msg, *userTags)
        Log.i(tag, message)
    }

    fun wFile(tag: String, msg: String, vararg userTags: String) {
        val message = getFullMessage(msg, *userTags)
        Log.w(tag, message)
        fileLog.warn(String.format("%s: %s", tag, message))
    }

    fun w(tag: String, msg: String, vararg userTags: String) {
        val message = getFullMessage(msg, *userTags)
        Log.w(tag, message)
    }

    fun eFile(tag: String, msg: String, t: Throwable, vararg userTags: String) {
        val message = getFullMessage(msg, *userTags)
        Log.e(tag, message, t)
        fileLog.error(String.format("%s: %s", tag, message), t)
    }

    fun e(tag: String, msg: String, t: Throwable, vararg userTags: String) {
        val message = getFullMessage(msg, *userTags)
        Log.e(tag, message, t)
    }

    class EmailSettings(
        val emailTo: String,
        val emailSubject: String,
        val userName: String,
        val dialogTitle: String
    )

    fun sendLogs(context: Context, options: EmailSettings) {
        val logFilePaths = ArrayList<String>()
        val fileDirectoryPath = context.filesDir.absolutePath
        var appDirectoryPath = context.packageManager.getPackageInfo(context.packageName, 0).applicationInfo.dataDir

        Log.d("log path is ", fileDirectoryPath)

        val logcatLogFilePath = getLogcatLogFile(fileDirectoryPath)
        if (logcatLogFilePath != null) {
            logFilePaths.add(logcatLogFilePath)
        }

        val deviceInfo = String.format(
            Locale.getDefault(),
            "Session metadata: Device: %1\$s, OS: %2\$s, App: %3\$s, User login: %4\$s",
            DeviceUtils.getDeviceName(),
            DeviceUtils.getAndroidVersion(),
            DeviceUtils.getAppVersion(),
            options.userName
        )

        dFile(LOG_APP, deviceInfo)
        val logFile = File("$fileDirectoryPath/app.log")
        if (logFile.exists() && logFile.isFile) {
            logFilePaths.add(logFile.path)
        }

        val appDir = File(appDirectoryPath)
        if(appDir.exists() && appDir.isDirectory) {
            val dbDir = File("$appDirectoryPath/databases")
            if(dbDir.exists() && dbDir.isDirectory) {
                for(dbFile in dbDir.listFiles()) {
                    logFilePaths.add(dbFile.path)
                }
            }

            val prefDir = File("$appDirectoryPath/shared_prefs")
            if(prefDir.exists() && prefDir.isDirectory) {
                for(prefFile in prefDir.listFiles()) {
                    logFilePaths.add(prefFile.path)
                }
            }
        }

        val zipFilePath = "$fileDirectoryPath/logs.zip"
        try {
            FileUtils.zip(logFilePaths, zipFilePath)
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }

        val zipFile = File(zipFilePath)
        val path = FileProvider.getUriForFile(context, "com.easysales.letsplay.fileprovider", zipFile)
        val uris = ArrayList<Uri>()
        uris.add(path)

        val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        emailIntent.type = "vnd.android.cursor.dir/email"
        val to = arrayOf(options.emailTo)

        emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
        emailIntent.putExtra(Intent.EXTRA_STREAM, uris)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, options.emailSubject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, deviceInfo)
        context.startActivity(Intent.createChooser(emailIntent, options.dialogTitle))
    }

    private fun getFullMessage(msg: String, vararg userTags: String): String {
        var message: String? = msg
        if (message == null) {
            message = ""
        }
        val msgBuilder = StringBuilder(message.length)
        msgBuilder.append(message).append(" ")

        for (userTag in userTags) {
            msgBuilder.append(String.format("[%s]", userTag))
        }
        return msgBuilder.toString()
    }

    private fun getLogcatLogFile(dir: String): String? {
        val logcatPath = "$dir/logcat.txt"
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, -1)
        val command = "logcat -v time -t " + calendar.timeInMillis + " -f" + " " + logcatPath
        Log.d("LOG", command)
        try {
            Runtime.getRuntime().exec(command)
            return logcatPath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    object LogNames {
        val FILE = "filelog"
        val LOGCAT = "logcat"
    }
}
