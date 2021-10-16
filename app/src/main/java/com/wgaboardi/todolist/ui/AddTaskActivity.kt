package br.com.dio.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wgaboardi.todolist.extensions.format
import com.wgaboardi.todolist.extensions.text
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.wgaboardi.todolist.databinding.ActivityAddTaskBinding
import com.wgaboardi.todolist.datasource.TaskDataSource
import com.wgaboardi.todolist.extensions.format
import com.wgaboardi.todolist.extensions.text
import com.wgaboardi.todolist.model.Task
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
            }
        }
        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate?.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.mbCancel.setOnClickListener {
            finish()
        }

        binding.mbCreate.setOnClickListener {
            val task = Task (
                title = binding.tilTitle.text,
                date = binding.tilDate.text ,
                hour = binding.tilHour.text,
               id = intent.getIntExtra(TASK_ID, 0)
                    )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    companion object {
        const val TASK_ID = "task_id"
    }

}