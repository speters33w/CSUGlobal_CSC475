package edu.csuglobal.csc475.helloandroidcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private var currentExpression = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.app_name)

        resultTextView = findViewById(R.id.resultTextView)

        setupNumberButtons()
        setupOperatorButtons()
        setupSpecialButtons()

    }

    private fun setupNumberButtons() {
        val numberIds = listOf(R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                               R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9)
        
        numberIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                appendToExpression((it as Button).text.toString())
            }
        }
    }

    private fun setupOperatorButtons() {
        val operatorMap = mapOf(
            R.id.buttonAdd to "+",
            R.id.buttonSubtract to "-",
            R.id.buttonMultiply to "*",
            R.id.buttonDivide to "/"
        )
        
        operatorMap.forEach { (id, operator) ->
            findViewById<Button>(id).setOnClickListener {
                appendToExpression(" $operator ")
            }
        }
    }

    private fun setupSpecialButtons() {
        findViewById<Button>(R.id.buttonDecimal).setOnClickListener {
            appendToExpression(".")
        }

        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            currentExpression.clear()
            updateDisplay()
        }

        findViewById<Button>(R.id.buttonEquals).setOnClickListener {
            calculateResult()
        }
    }

    private fun appendToExpression(value: String) {
        currentExpression.append(value)
        updateDisplay()
    }

    private fun updateDisplay() {
        val displayText = currentExpression.toString()
            .replace("*", "×")
            .replace("/", "÷")
        resultTextView.text = displayText
    }

    private fun calculateResult() {
        try {
            val expression = ExpressionBuilder(currentExpression.toString()).build()
            val result = expression.evaluate()
            
            currentExpression.clear()
            currentExpression.append(result)
            updateDisplay()
        } catch (e: Exception) {
            resultTextView.text = getString(R.string.error_message)
        }
    }
}