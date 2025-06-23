package com.ksp.vasool.ui.dashboard.view

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import com.ksp.vasool.MainNavigationActivity
import com.ksp.vasool.R
import com.ksp.vasool.base.BaseFragment
import com.ksp.vasool.base.BaseViewModelFactory
import com.ksp.vasool.constants.SessionVariable
import com.ksp.vasool.database.AppDatabase
import com.ksp.vasool.databinding.DashboardDoubleButtonLayoutBinding
import com.ksp.vasool.databinding.DashboardSingleButtonLayoutBinding
import com.ksp.vasool.databinding.FragmentDashboardBinding
import com.ksp.vasool.ui.accounts.CompanyDetails
import com.ksp.vasool.ui.collection.data.CollectionRepository
import com.ksp.vasool.ui.collection.model.Line
import com.ksp.vasool.ui.collection.view.CreateNewLineDialogFragment
import com.ksp.vasool.ui.collection.viewmodel.CollectionViewModel
import com.ksp.vasool.ui.loan.data.LoanRepository
import com.ksp.vasool.ui.loan.model.Installment
import com.ksp.vasool.ui.loan.viewmodel.LoanViewModel
import com.ksp.vasool.util.sharedpreference.PreferenceUtil.getCompanyDetailsFromSP
import com.ksp.vasool.util.sharedpreference.PreferenceUtil.getSharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DashboardFragment : BaseFragment() {

    lateinit var mBinding : FragmentDashboardBinding
    private lateinit var collectionViewModel : CollectionViewModel

    private var buttonIconsArray = listOf<Int>(R.drawable.ic_button4,R.drawable.ic_button1, R.drawable.ic_button2,
    R.drawable.ic_button3)

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        initializeVM()
    }

    private fun initializeViewPager(lineList : List<Line>)
    {
        mBinding.dashboardCards.viewpager.adapter = DashboardFragmentAdapter(childFragmentManager, lineList)
        mBinding.dashboardCards.dashboardTabIndicator.setupWithViewPager(mBinding.dashboardCards.viewpager, true)
        mBinding.dashboardCards.viewpager.clipToPadding = false
        mBinding.dashboardCards.viewpager.setPadding(50, 0, 50, 0)
        mBinding.dashboardCards.viewpager.pageMargin = 0
    }

    class DashboardFragmentAdapter(fragmentManager: FragmentManager, private val lineList: List<Line>)
        : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return lineList.size
        }

        override fun getItem(position: Int): Fragment {
            // Create a DashboardCardFragment for each line item
            return DashboardCardFragment.newInstance(lineList[position])
        }

        override fun getPageTitle(position: Int): CharSequence? {
            // You can set titles for each page if needed
            // For example, return lineList[position].title
            return super.getPageTitle(position)
        }
    }

    private fun initializeVM()
    {
        val collectionRepository = CollectionRepository(AppDatabase.getInstance(requireContext()).collectionDao())
        val collectionViewModelFactory = BaseViewModelFactory(collectionRepository)
        collectionViewModel = ViewModelProvider(this, collectionViewModelFactory)[CollectionViewModel::class.java]

    }

    override fun onCreateView(inflater:LayoutInflater , container:ViewGroup? , savedInstanceState:Bundle?):View? {
        mBinding = FragmentDashboardBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view:View , savedInstanceState:Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        mBinding.title.text = SessionVariable.companyName
        setUpOnClickListeners()
        setUpObservers()
        loadDashboardCards()
    }

    private fun loadDashboardCards() {

    }

    private fun loadQuickActions(lineList : List<Line>) {

        mBinding.dashboardQuickActionsLayout.removeAllViews()

        var i = 0
        while(i < lineList.size)
        {
            // for the last item to be added as a single button
            if(lineList.size % 2 == 1  && i == lineList.size-1 )
            {
                val binding = DashboardSingleButtonLayoutBinding.inflate(layoutInflater)

                binding.buttonName.text = lineList[i].lineName
                binding.button.tag = lineList[i].lineId
                binding.buttonIcon.setBackgroundResource(buttonIconsArray[i%buttonIconsArray.size])
                binding.button.setOnClickListener {
                    openCollectionFragment(binding.button.tag as String)
                }

                binding.button.setOnLongClickListener {

                    showDeleteCollectionDialog(binding.button.tag as String)

                    true
                }


                mBinding.dashboardQuickActionsLayout.addView(binding.root)
            }
            else
            {
                val binding = DashboardDoubleButtonLayoutBinding.inflate(layoutInflater)
                binding.firstButtonName.text = lineList[i].lineName
                binding.button1.tag = lineList[i].lineId
                binding.buttonIcon1.setBackgroundResource(buttonIconsArray[i%buttonIconsArray.size])
                binding.button1.setOnClickListener {
                    openCollectionFragment(binding.button1.tag as String)
                }

                binding.button1.setOnLongClickListener {

                    showDeleteCollectionDialog(binding.button1.tag as String)

                    true
                }

                i++

                binding.secondButtonName.text = lineList[i].lineName
                binding.button2.tag = lineList[i].lineId
                binding.buttonIcon2.setBackgroundResource(buttonIconsArray[i%buttonIconsArray.size])
                binding.button2.setOnClickListener {
                    openCollectionFragment(binding.button2.tag as String)
                }

                binding.button2.setOnLongClickListener {

                    showDeleteCollectionDialog(binding.button2.tag as String)

                    true
                }

                mBinding.dashboardQuickActionsLayout.addView(binding.root)
            }

            i++
        }
    }

    private fun showDeleteCollectionDialog(id : String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Delete Line")
        builder.setMessage("Are you sure, you want to delete this Line?")

        builder.setPositiveButton("OK") { dialog, which ->
            lifecycleScope.launch(Dispatchers.IO) {
                deleteCollection(id)
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun deleteCollection(lineId: String) {

        lifecycleScope.launch {
            collectionViewModel.deleteLine(lineId)
        }
    }

    private fun openCollectionFragment(lineId: String?)
    {
        val action = DashboardFragmentDirections.actionDashboardToDailyCollectionFragment(lineId!!)
        findNavController().navigate(action)
    }

    private fun setUpOnClickListeners()
    {
        mBinding.addNewLineFab.setOnClickListener {
            openNewLineWindow()
        }

        mBinding.emptyStateNewLineBtn.setOnClickListener {
            openNewLineWindow()
        }

        mBinding.settings.setOnClickListener{
            val action = DashboardFragmentDirections.actionDashboardToSettingsFragment()
            findNavController().navigate(action)
        }
    }

    private fun openNewLineWindow() {
        val fragment = CreateNewLineDialogFragment(collectionViewModel).apply {
            isCancelable = false
        }
        fragment.show(requireActivity().supportFragmentManager, "LineNameDialogFragment")

    }

    private fun setUpObservers()
    {
        collectionViewModel.allLines.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadQuickActions(it)
                initializeViewPager(it)
            }

            if(it.isNullOrEmpty())
            {
                mBinding.emptyView.visibility = View.VISIBLE
            }
            else
            {
                mBinding.emptyView.visibility = View.GONE
            }
        })
    }
}