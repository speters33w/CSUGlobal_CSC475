package edu.csuglobal.csc475.todolite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView

class TodoAdapter(context: Context, todos: List<ToDo>) : ArrayAdapter<ToDo>(context, 0, todos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false)

        val todo = getItem(position)
        val checkBox = view.findViewById<CheckBox>(R.id.todoCheckBox)
        val taskText = view.findViewById<TextView>(R.id.todoText)

        todo?.let {
            checkBox.isChecked = it.isCompleted
            taskText.text = it.task

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                it.isCompleted = isChecked
                notifyDataSetChanged()
            }
        }

        return view
    }
}