package com.ksp.vasool.ui.collection.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ksp.vasool.R
import com.ksp.vasool.constants.StringConstants
import com.ksp.vasool.databinding.CreateNewLineDialogBinding
import com.ksp.vasool.ui.collection.model.Line
import com.ksp.vasool.ui.collection.viewmodel.CollectionViewModel

/**
 * Created by sathya-6501 on 07/11/23.
 */

class CreateNewLineDialogFragment(private val collectionViewModel: CollectionViewModel) : DialogFragment()
{
    private lateinit var binding: CreateNewLineDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CreateNewLineDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeBtn.setOnClickListener{
            dialog?.dismiss()
        }

        binding.buttonCancel.setOnClickListener{
            dialog?.dismiss()
        }

        binding.buttonAdd.setOnClickListener {
            addNewLine()
        }
    }

    private fun addNewLine() {
        val lineName = binding.editTextLineName.text.toString()
        if (!TextUtils.isEmpty(lineName)) {
            createNewLine(lineName)
            dialog?.dismiss()
        } else {
            binding.editTextLineName.error = "Please enter the line name."
        }
    }

    private fun createNewLine(lineName : String)
    {
        val line = Line().apply {
            this.lineName = lineName
            this.lineId = StringConstants.lineIdPrefix+System.currentTimeMillis()
        }

        collectionViewModel.insertNewLine(line)
    }
}