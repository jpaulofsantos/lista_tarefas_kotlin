package com.example.listatarefas.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.listatarefas.databinding.ActivityMainBinding
import com.example.listatarefas.datasource.TaskDataSource
import com.example.listatarefas.ui.AddTaskActivity.Companion.TASK_ID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.fabAdd.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)

        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)

        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        if(list.isEmpty()) {
            binding.includeEmptyState.emptyState.visibility = View.VISIBLE
        } else {
            binding.includeEmptyState.emptyState.visibility = View.GONE
        }
        adapter.submitList(list)

    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}