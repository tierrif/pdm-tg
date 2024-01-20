package com.example.pdm_tg.ui.todos

import android.os.Bundle
import android.text.format.DateUtils
import android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import android.text.format.DateUtils.WEEK_IN_MILLIS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pdm_tg.R
import com.example.pdm_tg.databinding.FragmentNewTodoBinding
import com.example.pdm_tg.db.TaskList
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

open class NewTodoFragment : Fragment() {
    private lateinit var binding: FragmentNewTodoBinding
    private val viewModel: NewTodoViewModel by viewModels()
    private lateinit var taskListsEditText: AutoCompleteTextView

    protected var pickedDate: Date? = null
        set(pickedDate) {
            pickedDate ?: return let { field = null }

            // Set the text preview when a date is set.
            datePreview.text = DateUtils.getRelativeDateTimeString(
                requireContext(),
                pickedDate.time,
                MINUTE_IN_MILLIS,
                WEEK_IN_MILLIS,
                FORMAT_ABBREV_RELATIVE,
            )
            field = pickedDate
        }
    protected var selectedTaskList: TaskList? = null
        set(taskList) {
            taskList ?: return

            // Set the selection when setting this attribute.
            taskListsEditText.setText(taskList.listName, false)
            field = taskList
        }

    private lateinit var datePreview: TextView
    protected lateinit var taskNameEditText: TextInputEditText

    // If this is set to true by an inherited class, insertion won't happen but a method call.
    protected var isDetails = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentNewTodoBinding.inflate(inflater).also {
        binding = it
    }.root

    /**
     * To inherit by the details class to fill
     * fields with data already stored in the
     * db for this task in specific.
     */
    protected open fun fillFields() = Unit

    /**
     * Called when the user saves successfully.
     * This won't be called if there is an input error.
     */
    protected open fun onSave(
        taskName: String, taskList: TaskList?, dueDate: Date
    ): Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the task lists dropdown.
        taskListsEditText = requireActivity().findViewById(R.id.taskListEditText)

        // Set the adapter with the content through a coroutine (db access).
        lifecycleScope.launch {
            taskListsEditText.setAdapter(
                ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_dropdown_item_1line,
                    viewModel.getTaskLists().await()
                )
            )

            // If this class is inherited, the field values can be filled.
            fillFields()
        }

        // Listen to changes on the dropdown so they're stored in memory.
        taskListsEditText.setOnItemClickListener { _, _, position, _ ->
            selectedTaskList = taskListsEditText.adapter.getItem(position) as TaskList
        }

        // Build a date picker.
        val datePicker =
            MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.selectDate))
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
        datePreview = requireActivity().findViewById(R.id.datePreview)

        // We'll only need a listener for the positive button click. Cancel closes it by default.
        datePicker.addOnPositiveButtonClickListener {
            // Get today but at midnight.
            val now = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Check if the date isn't before today.
            if (it < now.time.time) {
                Toast.makeText(
                    requireContext(), getString(R.string.badDate), Toast.LENGTH_SHORT
                ).show()

                return@addOnPositiveButtonClickListener
            }

            // Save the current state of the date, so it's updated later by the time picker.
            pickedDate = Date(it)

            // Show the time picker.
            timePicker.show(parentFragmentManager, "time")
        }

        timePicker.addOnPositiveButtonClickListener {
            // Use a calendar instance to set the time in the existing date.
            val calendar = Calendar.getInstance().apply {
                time = pickedDate!!
                set(Calendar.HOUR_OF_DAY, timePicker.hour)
                set(Calendar.MINUTE, timePicker.minute)
            }

            // Check if the time isn't before the current time.
            if (calendar.time.time < Calendar.getInstance().time.time) {
                Toast.makeText(
                    requireContext(), getString(R.string.badDate), Toast.LENGTH_SHORT
                ).show()

                return@addOnPositiveButtonClickListener
            }

            // Update pickedDate.
            pickedDate = calendar.time
        }

        // Handle the task list clear button so that it may be empty on submit.
        val clearButton = requireActivity().findViewById<MaterialButton>(R.id.clearTaskList)
        clearButton.setOnClickListener {
            taskListsEditText.apply {
                text = null
                isFocusable = false
            }
        }

        // Get the task name EditText.
        taskNameEditText =
            requireActivity().findViewById(R.id.taskNameEditText)

        // Add a listener for the save button and verify if all data is filled.
        val saveButton = requireActivity().findViewById<MaterialButton>(R.id.save)
        saveButton.setOnClickListener {
            if (taskNameEditText.text.isNullOrEmpty() || pickedDate === null) {
                return@setOnClickListener Toast.makeText(
                    requireContext(), R.string.newTodoMissingData, Toast.LENGTH_SHORT
                ).show()
            }

            lifecycleScope.launch {
                if (isDetails) {
                    onSave(taskNameEditText.text.toString(), selectedTaskList, pickedDate!!)?.join()
                } else {
                    viewModel.newTask(
                        taskNameEditText.text.toString(), selectedTaskList, pickedDate!!
                    ).join()
                }

                findNavController().popBackStack()
            }
        }
    }
}
