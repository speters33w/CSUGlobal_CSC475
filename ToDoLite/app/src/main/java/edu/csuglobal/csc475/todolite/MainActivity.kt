package edu.csuglobal.csc475.todolite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.widget.CheckBox
import android.view.KeyEvent
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var todoList: ListView
    private lateinit var adapter: TodoAdapter
    private lateinit var todos: MutableList<ToDo>
    private lateinit var taskInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        todoList = findViewById(R.id.todoList)
        val addButton: Button = findViewById(R.id.addButton)
        taskInput = findViewById(R.id.taskInput)
        val deleteCompletedButton: Button = findViewById(R.id.deleteCompletedButton)

        todos = dbHelper.getAllToDos().toMutableList()
        adapter = TodoAdapter(this, todos)
        todoList.adapter = adapter

        // Hide keyboard on initial launch
        taskInput.visibility = View.GONE

        addButton.setOnClickListener {
            taskInput.visibility = View.VISIBLE
            taskInput.requestFocus()
            showKeyboard(taskInput)
        }

        taskInput.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                addTask()
                return@setOnKeyListener true
            }
            false
        }

        todoList.setOnItemClickListener { _, view, position, _ ->
            val checkBox = view.findViewById<CheckBox>(R.id.todoCheckBox)
            checkBox.isChecked = !checkBox.isChecked
            val todo = todos[position]
            todo.isCompleted = checkBox.isChecked
            dbHelper.updateToDo(todo)
            adapter.notifyDataSetChanged()
        }

        todoList.setOnItemLongClickListener { _, _, position, _ ->
            val todo = todos[position]
            dbHelper.deleteToDo(todo.id)
            todos.removeAt(position)
            adapter.notifyDataSetChanged()
            true
        }

        deleteCompletedButton.setOnClickListener {
            deleteCompletedTasks()
        }
    }

    private fun addTask() {
        val task = taskInput.text.toString().trim()
        if (task.isNotEmpty()) {
            val newTodo = ToDo(task = task)
            val id = dbHelper.addToDo(newTodo)
            newTodo.id = id
            todos.add(newTodo)
            adapter.notifyDataSetChanged()
            taskInput.text.clear()
            hideKeyboard(taskInput)
            taskInput.visibility = View.GONE
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun deleteCompletedTasks() {
        val completedTasks = todos.filter { it.isCompleted }
        for (task in completedTasks) {
            dbHelper.deleteToDo(task.id)
        }
        todos.removeAll { it.isCompleted }
        adapter.notifyDataSetChanged()
    }
}