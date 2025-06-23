package com.ksp.vasool.ui.settings

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ksp.vasool.MainNavigationActivity
import com.ksp.vasool.R
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.constants.SessionVariable
import com.ksp.vasool.constants.StringConstants
import com.ksp.vasool.database.AppDatabase
import com.ksp.vasool.databinding.FragmentSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**
 * Created by sathya-6501 on 22/12/23.
 */
class SettingsFragment : BaseFragment()
{
    private lateinit var mBinding: FragmentSettingsBinding

    private val pickBackupFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val restored = restoreDatabaseFromUri(requireContext(), uri)
            if (restored) {
                Toast.makeText(requireContext(), "Database restored! Restarting app...", Toast.LENGTH_SHORT).show()
                restartApp(requireContext())
            } else {
                Toast.makeText(requireContext(), "Restore failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentSettingsBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActionBar(mBinding.toolbar, "Settings")

        setUpOnCLickListeners()
    }

    private fun setUpOnCLickListeners() {

        mBinding.organizationProfile.setOnClickListener{

            val action = SettingsFragmentDirections.actionSettingsToCompanyDetails()
            findNavController().navigate(action)
        }

        mBinding.privacyLayout.setOnClickListener{
            try
            {
                val intent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
                startActivity(intent)
            }
            catch (e: ActivityNotFoundException)
            {
            }
        }

        mBinding.feedbackLayout.setOnClickListener{
            sendFeedback()
        }

        mBinding.backupLayout.setOnClickListener{
            backupAndShareDatabase(requireContext())
        }

        mBinding.restureBackupLayout.setOnClickListener{
            pickBackupFileLauncher.launch(arrayOf("*/*"))
        }

        mBinding.shareAppLayout.setOnClickListener {
            shareApp()
        }

        mBinding.rateUsLayout.setOnClickListener {
            rateUsClicked()
        }

        mBinding.appVersion.text = "Version: ${requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0).versionName}"
    }



    fun backupAndShareDatabase(context: Context) {
        try {
            AppDatabase.closeDatabase()

            val dbFile = context.getDatabasePath("Vasool.db")

            val db = AppDatabase.getInstance(context)


            lifecycleScope.launch {

                val contactDao = db.collectionDao()
                val allContacts = contactDao.getAllLine()
                Log.d("Backup_data", "Contact count before backup: ${allContacts.size}")

            }
// Trigger actual DB use before backup

            if (!dbFile.exists()) {
                Toast.makeText(context, "Database not found", Toast.LENGTH_SHORT).show()
                return
            }

            // Log and check file size
            val dbSize = dbFile.length()
            Log.d("Backup", "DB Path: ${dbFile.absolutePath}, Size: $dbSize bytes")

            if (dbSize < 1024) {
                Toast.makeText(context, "Database is too small or empty", Toast.LENGTH_SHORT).show()
                return
            }

            // Create a backup file with timestamp
            val timestamp = System.currentTimeMillis()
            val backupDir = context.getExternalFilesDir(null)
            val backupFile = File(backupDir, "Vasool_backup_$timestamp.db")

            // Copy original DB to backup file
            dbFile.copyTo(backupFile, overwrite = true)

            // Generate URI with FileProvider
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                backupFile
            )

            // Create email intent
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/octet-stream"
                putExtra(Intent.EXTRA_SUBJECT, "Vasool DB Backup")
                putExtra(Intent.EXTRA_TEXT, "Attached is the backup of Vasool.db")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // Launch email chooser
            context.startActivity(Intent.createChooser(emailIntent, "Send Backup via Email"))

        } catch (e: Exception) {
            Log.e("Backup", "Failed to share backup", e)
            Toast.makeText(context, "Failed to share backup", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateAndBackupDatabase(context: Context) {
        val db = AppDatabase.getInstance(context)
        val lineDao = db.collectionDao()
        val contactDao = db.contactDao()

        lifecycleScope.launch {
            val lines = withContext(Dispatchers.IO) { lineDao.getAllLine() }

            Log.d("Backup", "Line rows: ${lines.size}")

            if (lines.isEmpty()) {
                Toast.makeText(context, "No data to backup", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Wait a moment to ensure Room commits writes
            delay(300)
            backupAndDownloadDatabase(context)
        }
    }


    // Function to backup Room database
    fun backupRoomDatabase(context: Context) {
        // Replace 'YourDatabaseName' with your actual Room database class
        val databaseName = "Vasool"

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    // Path to the current Room database file
                    val currentDBPath = context.getDatabasePath("$databaseName.db")

                    // Directory for storing backup
                    val backupDir = context.getExternalFilesDir(null)
//                    if (!backupDir.exists()) {
//                        backupDir.mkdirs()
//                    }

                    // Backup file name (can be customized as per your requirement)
                    val backupFileName = "backup_${System.currentTimeMillis()}.db"

                    // Path to the backup file
                    val backupDBPath = File(backupDir, backupFileName)

                    // Copying the current database file to the backup location
                    currentDBPath.copyTo(backupDBPath, overwrite = true)

                    Log.d("BackupRoomDatabase", "Database backed up successfully to: ${backupDBPath.absolutePath}")

                } catch (e: IOException) {
                    Log.e("BackupRoomDatabase", "Error backing up database: ${e.message}")
                }
            }
        }
    }


    fun backupAndDownloadDatabase(context: Context) {
        try {
            lifecycleScope.launch {
                val db = AppDatabase.getInstance(context)

                AppDatabase.closeDatabase()

                val lineCount = db.collectionDao().getAllLine().size

                Log.d("DB_Content", "Lines: $lineCount")
            }

            val dbFile = context.getDatabasePath("Vasool.db")
            if (!dbFile.exists()) {
                Toast.makeText(context, "Database file not found", Toast.LENGTH_SHORT).show()
                return
            }

            val dbSize = dbFile.length()
            if (dbSize < 1024) {
                Toast.makeText(context, "Database is too small or empty", Toast.LENGTH_SHORT).show()
                return
            }

            // Create backup file in Downloads directory with timestamp
            val timestamp = System.currentTimeMillis()
            val backupFileName = "Vasool_backup_$timestamp.db"
            val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()
            val backupFile = java.io.File(downloadsDir, backupFileName)
            dbFile.copyTo(backupFile, overwrite = true)

            Toast.makeText(context, "Backup saved to Downloads as $backupFileName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            android.util.Log.e("Backup", "Failed to backup database", e)
            Toast.makeText(context, "Failed to backup database: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }



    fun restoreDatabaseFromUri(context: Context, uri: Uri): Boolean {
        return try {
            AppDatabase.closeDatabase()  // ✅ IMPORTANT

            val dbFile = context.getDatabasePath("Vasool.db")

            context.contentResolver.openInputStream(uri)?.use { input ->
                dbFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val restoredSize = dbFile.length()
            Log.d("Restore", "Restored DB size: $restoredSize")

            restoredSize > 0  // ✅ Return true only if DB file is valid
        } catch (e: Exception) {
            Log.e("Restore", "Failed to restore DB", e)
            false
        }
    }

    fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finishAffinity()
        }
        Runtime.getRuntime().exit(0)
    }

    private fun rateUsClicked()
    {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.data = Uri.parse(StringConstants.app_uri)
        try
        {
            startActivity(intent)
        }
        catch (e: ActivityNotFoundException)
        {
//            Toast.makeText(context, resources.getString(R.string.em_no), Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareApp() {
        try {
            val appPackageName = requireActivity().getPackageName()
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))

            //app playstore url
            val appUrl = StringConstants.app_url

            val shortDescription = getString(R.string.short_description)

            shareIntent.putExtra(Intent.EXTRA_TEXT, "$shortDescription\n\nCheck out Vasool Book Android App at:\n$appUrl")
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        }
        catch (e: Exception)
        {

        }
    }

    private fun sendFeedback()
    {
        val mailto = arrayOf(resources.getString(R.string.app_support_email))
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        sendIntent.setData(Uri.parse("mailto:"))
        sendIntent.putExtra(Intent.EXTRA_EMAIL, mailto)
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.feedback_subject_from, SessionVariable.companyName))
        try
        {
            sendIntent.putExtra(Intent.EXTRA_TEXT, feedbackDetails)
            try
            {
                startActivity(Intent.createChooser(sendIntent, "Send email via"))
            }
            catch (e: ActivityNotFoundException)
            {
                val ab = AlertDialog.Builder(requireActivity().applicationContext)
                ab.setMessage("Please send us an email to developer.nativeapps@gmail.com")
                ab.setPositiveButton("Okay", null)
                ab.show()
            }

        }
        catch (e: PackageManager.NameNotFoundException)
        {

        }
    }

    val feedbackDetails: String
        @Throws(PackageManager.NameNotFoundException::class) get()
        {
            val details = StringBuilder()
            details.append("\n\n\n")
            details.append("===== Details =====")
            details.append("\n")
            details.append(requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0).versionName)
            details.append("/")
            details.append(SessionVariable.companyName)
            details.append("\n")
            details.append("    ")
            details.append(Build.MANUFACTURER)
            details.append("/")
            details.append(Build.MODEL)
            details.append("/")
            details.append(android.os.Build.VERSION.SDK_INT)
            details.append("\n")
            var appstore = requireActivity().packageManager.getInstallerPackageName(requireActivity().packageName)
            if (TextUtils.isEmpty(appstore))
            {
                appstore = "UnKnown"
            }
            details.append(appstore)
            details.append("\n")
            details.append("======================")
            return details.toString()
        }
}