package com.example.pdm_tg.ui.todos

import android.os.Bundle
import android.text.format.DateUtils
import android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import android.text.format.DateUtils.WEEK_IN_MILLIS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.FragmentNewTodoBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.Calendar
import java.util.Date

class NewTodoFragment : Fragment() {
    private lateinit var binding: FragmentNewTodoBinding
    private var pickedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNewTodoBinding.inflate(inflater).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Build a date picker.
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.selectDate))
            .build()

        // Build a time picker.
        val timePicker = MaterialTimePicker.Builder().build()

        // Handle when the button for picking a date is pressed.
        val datePickerButton = requireActivity().findViewById<MaterialButton>(R.id.selectDate)
        datePickerButton.setOnClickListener {
            // Show the date picker dialog using the parent fragment manager.
            datePicker.show(parentFragmentManager, "date")
        }

        // Get the date preview TextView so it's edited on date select.
        val datePreview = requireActivity().findViewById<TextView>(R.id.datePreview)

        // We'll only need a listener for the positive button click. Cancel closes it by default.
        datePicker.addOnPositiveButtonClickListener {
            // Get today but at midnight.
            val now = Calendar.getInstance()
            now.set(Calendar.HOUR_OF_DAY, 0)
            now.set(Calendar.MINUTE, 0)
            now.set(Calendar.SECOND, 0)
            now.set(Calendar.MILLISECOND, 0)

            // Check if the date isn't before today.
            if (it < now.time.time) {
                Toast.makeText(requireContext(),
                    getString(R.string.badDate), Toast.LENGTH_SHORT).show()

                return@addOnPositiveButtonClickListener
            }

            // Save the current state of the date, so it's updated later by the time picker.
            pickedDate = Date(it)

            // Show the time picker.
            timePicker.show(parentFragmentManager, "time")
        }

        timePicker.addOnPositiveButtonClickListener {
            // Use a calendar instance to set the time in the existing date.
            val calendar = Calendar.getInstance()
            calendar.time = pickedDate!!
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)

            // Check if the time isn't before the current time.
            if (calendar.time.time < Calendar.getInstance().time.time) {
                Toast.makeText(requireContext(),
                    getString(R.string.badDate), Toast.LENGTH_SHORT).show()

                return@addOnPositiveButtonClickListener
            }

            // Display this date.
            datePreview.text = DateUtils.getRelativeDateTimeString(
                requireContext(),
                calendar.time.time,
                MINUTE_IN_MILLIS,
                WEEK_IN_MILLIS,
                FORMAT_ABBREV_RELATIVE,
            )
        }
    }
}
