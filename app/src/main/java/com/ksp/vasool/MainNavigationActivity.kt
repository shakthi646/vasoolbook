package com.ksp.vasool

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricPrompt
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ksp.vasool.base.BaseViewModelFactory
import com.ksp.vasool.database.AppDatabase
import com.ksp.vasool.databinding.ActivityMainBinding
import com.ksp.vasool.ui.accounts.CompanyDetails
import com.ksp.vasool.ui.collection.data.CollectionRepository
import com.ksp.vasool.ui.collection.viewmodel.CollectionViewModel
import com.ksp.vasool.ui.contact.data.ContactRepository
import com.ksp.vasool.ui.contact.viewmodel.ContactViewModel
import com.ksp.vasool.ui.loan.data.LoanRepository
import com.ksp.vasool.ui.loan.viewmodel.LoanViewModel
import com.ksp.vasool.util.sharedpreference.PreferenceUtil.getCompanyDetailsFromSP


class MainNavigationActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mBinding : ActivityMainBinding

    private lateinit var contactViewModel :ContactViewModel
    private lateinit var loanViewModel:LoanViewModel
    private lateinit var collectionViewModel:CollectionViewModel

    lateinit var companyDetails: CompanyDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true  // or false based on your theme
            isAppearanceLightNavigationBars = true
        }

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        companyDetails = this.getCompanyDetailsFromSP()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initializeVM()
        initializeNavigation()

        startActivity(intent)
    }


    private fun initializeNavigation() {

        navController = findNavController(R.id.nav_host_fragment)

        val startDestination = if(companyDetails.companyName.isNullOrBlank())
        {
            R.id.companyDetailsFragment
        }
        else
        {
            R.id.dashboardFragment
        }

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.setStartDestination(startDestination)
        navController.graph = navGraph
    }

    private fun initializeVM() {

        val collectionRepository = CollectionRepository(AppDatabase.getInstance(applicationContext).collectionDao())
        val collectionViewModelFactory = BaseViewModelFactory(collectionRepository)
        collectionViewModel = ViewModelProvider(this, collectionViewModelFactory)[CollectionViewModel::class.java]

        val contactRepository = ContactRepository(AppDatabase.getInstance(applicationContext).contactDao())
        val contactViewModelFactory = BaseViewModelFactory(contactRepository)
        contactViewModel = ViewModelProvider(this, contactViewModelFactory)[ContactViewModel::class.java]

        val loanRepository = LoanRepository(AppDatabase.getInstance(applicationContext).loanDao())
        val loanViewModelFactory = BaseViewModelFactory(loanRepository)
        loanViewModel = ViewModelProvider(this, loanViewModelFactory)[LoanViewModel::class.java]

    }

    fun getContactViewModel() : ContactViewModel
    {
        return contactViewModel
    }

    fun getLoanViewModel() : LoanViewModel
    {
        return loanViewModel
    }

    fun getCollectionViewModel() : CollectionViewModel
    {
        return collectionViewModel
    }

    private fun setUpNavigation() {

//        bottomNavigationView.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.navigation_home -> navController.navigate(R.id.dashboardFragment)
//                R.id.navigation_weekly -> navController.navigate(R.id.weeklyCollectionFragment)
//            }
//            true
//        }
    }

    override fun onBackPressed() {

        when(navController.currentDestination?.id)
        {
            R.id.dashboardFragment ->
            {
                this.finish()
            }
            else ->
            {
                navController.navigateUp()
            }
        }
    }
}