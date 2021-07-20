package com.example.listatarefas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listatarefas.R
import com.example.listatarefas.databinding.ItemTaskBinding
import com.example.listatarefas.model.Task

class TaskAdapter : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallBack()) {

    var listenerEdit : (Task) -> Unit = {}
    var listenerDelete : (Task) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Task) {
            binding.tvTitle.text = item?.title
            binding.tvDate.text = "${item?.date} ${item.hour}"
            binding.ivButton.setOnClickListener {
                showPopup(item)
            }
        }

        private fun showPopup(item: Task) {
            val ivButton = binding.ivButton
            val popupMenu = PopupMenu(ivButton.context, ivButton)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_delete -> listenerDelete(item)
                }
                return@setOnMenuItemClickListener true
            }

            popupMenu.show()
        }
    }
}

class DiffCallBack : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id
}