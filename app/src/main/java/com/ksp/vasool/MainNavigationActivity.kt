package com.ksp.vasool

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ksp.vasool.base.BaseViewModelFactory
import com.ksp.vasool.database.AppDatabase
import com.ksp.vasool.databinding.ActivityMainBinding
import com.ksp.vasool.ui.collection.data.CollectionRepository
import com.ksp.vasool.ui.collection.viewmodel.CollectionViewModel
import com.ksp.vasool.ui.contact.data.ContactRepository
import com.ksp.vasool.ui.contact.viewmodel.ContactViewModel
import com.ksp.vasool.ui.loan.data.LoanRepository
import com.ksp.vasool.ui.loan.viewmodel.LoanViewModel


class MainNavigationActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mBinding : ActivityMainBinding

    private lateinit var contactViewModel :ContactViewModel
    private lateinit var loanViewModel:LoanViewModel
    private lateinit var collectionViewModel:CollectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initializeVM()
        initializeNavigation()
//        setUpNavigation()

        startActivity(intent)

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//
//            when(destination.id)
//            {
//                R.id.dashboardFragment, R.id.dailyCollectionFragment, R.id.weeklyCollectionFragment ->
//                {
//                    bottomNavigationView.visibility = View.VISIBLE
//                }
//                else ->
//                {
//                    bottomNavigationView.visibility = View.GONE
//                }
//            }
//        }
    }


    private fun initializeNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
//        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
//        bottomNavigationView.setupWithNavController(navController)
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

//    private fun setUpNavigation() {
//        bottomNavigationView.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.navigation_home -> navController.navigate(R.id.dashboardFragment)
//                R.id.navigation_weekly -> navController.navigate(R.id.weeklyCollectionFragment)
//            }
//            true
//        }
//    }

    override fun onBackPressed() {

        when(navController.currentDestination?.id)
        {
            R.id.dashboardFragment, R.id.weeklyCollectionFragment ->
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