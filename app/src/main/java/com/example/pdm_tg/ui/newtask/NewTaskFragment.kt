package com.example.pdm_tg.ui.newtask

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pdm_tg.InheritableFragment
import com.example.pdm_tg.MainActivity
import com.example.pdm_tg.R
import com.example.pdm_tg.ReminderReceiver
import com.example.pdm_tg.databinding.FragmentNewTaskBinding
import com.example.pdm_tg.db.Task
import com.example.pdm_tg.db.TaskList
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

open class NewTaskFragment : InheritableFragment<Task>() {
    private lateinit var binding: FragmentNewTaskBinding
    private val viewModel: NewTaskViewModel by viewModels()
    private lateinit var taskListsEditText: AutoCompleteTextView
    private val args: NewTaskFragmentArgs by navArgs()

    protected var pickedDate: Date? = null
        set(pickedDate) {
            if (pickedDate === null || !::datePreview.isInitialized) return let {
                // Reset if it's null.
                datePreview.text = resources.getText(R.string.pick)
                field = null
            }

            // Set the text preview with a relative date when a date is set.
            datePreview.text = DateUtils.getRelativeDateTimeString(
                requireContext(),
                pickedDate.time,
                MINUTE_IN_MILLIS,
                WEEK_IN_MILLIS,
                FORMAT_ABBREV_RELATIVE,
            )
            field = pickedDate
        }

    protected var pickedReminder: Date? = null
        set(pickedReminder) {
            if (pickedReminder === null || !::reminderPreview.isInitialized) return let {
                reminderPreview.text = resources.getText(R.string.pick)
                field = null
            }

            // Set the text preview when a date is set.
            reminderPreview.text = DateUtils.getRelativeDateTimeString(
                requireContext(),
                pickedReminder.time,
                MINUTE_IN_MILLIS,
                WEEK_IN_MILLIS,
                FORMAT_ABBREV_RELATIVE,
            )
            field = pickedReminder
        }
    protected var selectedTaskList: TaskList? = null
        set(taskList) {
            taskList ?: return

            // Set the selection when setting this attribute.
            taskListsEditText.setText(taskList.listName, false)
            field = taskList
        }

    private lateinit var datePreview: TextView
    private lateinit var reminderPreview: TextView
    protected lateinit var taskNameEditText: TextInputEditText
    protected lateinit var taskNotesEditText: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentNewTaskBinding.inflate(inflater).also {
        binding = it
    }.root

    /**
     * To inherit by the details class to fill
     * fields with data already stored in the
     * db for this task in specific.
     */
    override fun fillFields() = Unit

    /**
     * Called when the user saves successfully.
     * This won't be called if there is an input error.
     *
     * @param t The task to save.
     */
    override fun onSave(t: Task): Deferred<Long> = CompletableDeferred()

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

            // Set the task list if the user comes from a task list fragment.
            if (args.taskListId != -1L) {
                selectedTaskList = viewModel.getTaskListById(args.taskListId).await()
            }
        }

        // Listen to changes on the dropdown so they're stored in memory.
        taskListsEditText.setOnItemClickListener { _, _, position, _ ->
            selectedTaskList = taskListsEditText.adapter.getItem(position) as TaskList
        }

        // Build the date pickers.
        val datePicker =
            MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.selectDate))
                .build()
        val reminderPicker =
            MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.selectReminder))
                .build()

        // Build the time pickers.
        val timePicker = MaterialTimePicker.Builder().build()
        val timePickerReminder = MaterialTimePicker.Builder().build()

        // Get the date preview TextViews so they're edited on date/reminder select.
        datePreview = requireActivity().findViewById(R.id.datePreview)
        reminderPreview = requireActivity().findViewById(R.id.reminderPreview)

        // Get the buttons that open the date pickers.
        val datePickerButton = requireActivity().findViewById<MaterialButton>(R.id.selectDate)
        val reminderPickerButton =
            requireActivity().findViewById<MaterialButton>(R.id.selectReminder)

        // Handle the date and time pickers for the due date.
        handleDatePicker(datePickerButton, datePicker, timePicker, SelectableDateType.DUE_DATE)
        handleTimePicker(timePicker, SelectableDateType.DUE_DATE)

        // Handle the date and time pickers for the reminder.
        handleDatePicker(reminderPickerButton, reminderPicker,
            timePickerReminder, SelectableDateType.REMINDER)
        handleTimePicker(timePickerReminder, SelectableDateType.REMINDER)


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

        // Get task notes.
        taskNotesEditText = requireActivity().findViewById(R.id.taskNotes)

        // Add a listener for the save button and verify if all data is filled.
        val saveButton = requireActivity().findViewById<MaterialButton>(R.id.save)
        saveButton.setOnClickListener {
            if (taskNameEditText.text.isNullOrEmpty() || pickedDate === null) {
                return@setOnClickListener Toast.makeText(
                    requireContext(), R.string.newTodoMissingData, Toast.LENGTH_SHORT
                ).show()
            }

            lifecycleScope.launch {
                // Capture the ID to set the notification ID to the task ID.
                val id: Long
                if (isDetails) {
                    id = onSave(
                        Task(
                            taskNameEditText.text.toString(),
                            selectedTaskList?.id,
                            pickedDate!!,
                            pickedReminder,
                            false,
                            taskNotesEditText.text.toString(),
                        )
                    ).await()
                } else {
                    id = viewModel.newTask(
                        taskNameEditText.text.toString(),
                        selectedTaskList,
                        pickedDate!!,
                        pickedReminder,
                        taskNotesEditText.text.toString(),
                    ).await()
                }

                // Set the reminder if a date was provided.
                if (pickedReminder !== null) {
                    // Retrieve the Alarm Manager service.
                    val alarmManager =
                        requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    // Create the intent that will contain the task name which is being sent.
                    val intent = Intent(requireContext(), ReminderReceiver::class.java)
                    intent.putExtra("taskName", taskNameEditText.text.toString())

                    // Create the pending intent for the broadcast.
                    val pendingIntent = PendingIntent.getBroadcast(
                        requireContext(),
                        id.toInt(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                    )

                    // Create another intent but for the clock icon that displays in the status bar.
                    val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
                    val basicPendingIntent = PendingIntent.getActivity(
                        requireContext(),
                        id.toInt(),
                        mainActivityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                    )

                    // Set the alarm clock info and the pending intent for the alarm manager.
                    val clockInfo =
                        AlarmManager.AlarmClockInfo(pickedReminder!!.time, basicPendingIntent)
                    alarmManager.setAlarmClock(clockInfo, pendingIntent)
                }

                findNavController().popBackStack()
            }
        }
    }

    /**
     * Handle a date picker for this view.
     *
     * @param datePickerButton The button responsible for opening the date picker.
     * @param datePicker The date picker to handle.
     * @param timePicker The time picker that corresponds to the date type being handled.
     * @param dateType The type of date being handled (reminder, due date).
     */
    private fun handleDatePicker(
        datePickerButton: MaterialButton,
        datePicker: MaterialDatePicker<Long>,
        timePicker: MaterialTimePicker,
        dateType: SelectableDateType
    ) {
        datePickerButton.setOnClickListener {
            // Show the date picker dialog using the parent fragment manager.
            try {
                datePicker.show(parentFragmentManager, when (dateType) {
                    SelectableDateType.DUE_DATE -> "time"
                    SelectableDateType.REMINDER -> "reminder"
                })
            } catch (ignore: Exception) {
            }
        }

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
            when (dateType) {
                SelectableDateType.DUE_DATE -> pickedDate = Date(it)
                SelectableDateType.REMINDER -> pickedReminder = Date(it)
            }

            // Show the time picker.
            timePicker.show(parentFragmentManager, when (dateType) {
                SelectableDateType.DUE_DATE -> "time"
                SelectableDateType.REMINDER -> "remindertime"
            })
        }
    }

    /**
     * Handle a time picker for this view.
     *
     * @param timePicker The time picker to handle.
     * @param dateType The type of date being handled (reminder, due date).
     */
    private fun handleTimePicker(timePicker: MaterialTimePicker, dateType: SelectableDateType) {
        timePicker.addOnPositiveButtonClickListener {
            // Use a calendar instance to set the time in the existing date.
            val calendar = Calendar.getInstance().apply {
                time = when (dateType) {
                    SelectableDateType.DUE_DATE -> pickedDate!!
                    SelectableDateType.REMINDER -> pickedReminder!!
                }

                set(Calendar.HOUR_OF_DAY, timePicker.hour)
                set(Calendar.MINUTE, timePicker.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Check if the time isn't before the current time.
            if (calendar.time.time < Calendar.getInstance().time.time) {
                Toast.makeText(
                    requireContext(), getString(R.string.badDate), Toast.LENGTH_SHORT
                ).show()

                when (dateType) {
                    SelectableDateType.DUE_DATE -> pickedDate = null
                    SelectableDateType.REMINDER -> pickedReminder = null
                }

                return@addOnPositiveButtonClickListener
            }

            // Update the picked date.
            when (dateType) {
                SelectableDateType.DUE_DATE -> pickedDate = calendar.time
                SelectableDateType.REMINDER -> pickedReminder = calendar.time
            }
        }

        // Reset the selected date/reminder on close.
        timePicker.addOnNegativeButtonClickListener {
            when (dateType) {
                SelectableDateType.DUE_DATE -> pickedDate = null
                SelectableDateType.REMINDER -> pickedReminder = null
            }
        }
    }

    /**
     * Date type being handled in a
     * determined date picker.
     */
    private enum class SelectableDateType { DUE_DATE, REMINDER }
}
