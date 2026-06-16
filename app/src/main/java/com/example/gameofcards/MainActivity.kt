package com.example.gameofcards

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // keeping track of scores
    var myScore = 0
    var botScore = 0

    // round counter
    var roundNumber = 1
    val totalRounds = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // show the welcome screen first
        showWelcomeScreen()
    }

    fun showWelcomeScreen() {
        val welcomeLayout = findViewById<LinearLayout>(R.id.welcomeLayout)
        val gameLayout = findViewById<LinearLayout>(R.id.gameLayout)
        val resultLayout = findViewById<LinearLayout>(R.id.resultLayout)
        val finalLayout = findViewById<LinearLayout>(R.id.finalLayout)

        welcomeLayout.visibility = View.VISIBLE
        gameLayout.visibility = View.GONE
        resultLayout.visibility = View.GONE
        finalLayout.visibility = View.GONE

        val startBtn = findViewById<Button>(R.id.startButton)
        startBtn.setOnClickListener {
            // reset everything when starting
            myScore = 0
            botScore = 0
            roundNumber = 1
            showGameScreen()
        }
    }

    fun showGameScreen() {
        val welcomeLayout = findViewById<LinearLayout>(R.id.welcomeLayout)
        val gameLayout = findViewById<LinearLayout>(R.id.gameLayout)
        val resultLayout = findViewById<LinearLayout>(R.id.resultLayout)
        val finalLayout = findViewById<LinearLayout>(R.id.finalLayout)

        welcomeLayout.visibility = View.GONE
        gameLayout.visibility = View.VISIBLE
        resultLayout.visibility = View.GONE
        finalLayout.visibility = View.GONE

        // update round text
        val roundText = findViewById<TextView>(R.id.roundText)
        roundText.text = "Round $roundNumber of $totalRounds"

        // update score display
        val scoreText = findViewById<TextView>(R.id.scoreText)
        scoreText.text = "You: $myScore  |  Bot: $botScore"

        // clear the input box
        val numberInput = findViewById<EditText>(R.id.numberInput)
        numberInput.setText("")

        val submitBtn = findViewById<Button>(R.id.submitButton)
        submitBtn.setOnClickListener {
            val inputText = numberInput.text.toString()

            // basic check if user typed something
            if (inputText.isEmpty()) {
                numberInput.error = "Please enter a number!"
                return@setOnClickListener
            }

            val userNumber = inputText.toIntOrNull()

            // check if it's actually a number
            if (userNumber == null) {
                numberInput.error = "That's not a number!"
                return@setOnClickListener
            }

            // check range
            if (userNumber < 0 || userNumber > 100) {
                numberInput.error = "Number must be between 0 and 100!"
                return@setOnClickListener
            }

            // bot picks a random number
            val botNumber = (0..100).random()

            // do the math
            val average = (userNumber + botNumber) / 2.0
            val spiderNumber = average * 0.8

            // figure out who is closer
            val myDistance = Math.abs(userNumber - spiderNumber)
            val botDistance = Math.abs(botNumber - spiderNumber)

            var whoWon = ""
            if (myDistance < botDistance) {
                myScore++
                whoWon = "You"
            } else if (botDistance < myDistance) {
                botScore++
                whoWon = "Bot"
            } else {
                // tie - nobody gets a point
                whoWon = "Tie"
            }

            showResultScreen(userNumber, botNumber, average, spiderNumber, myDistance, botDistance, whoWon)
        }
    }

    fun showResultScreen(
        userNum: Int,
        botNum: Int,
        avg: Double,
        spiderNum: Double,
        myDist: Double,
        botDist: Double,
        winner: String
    ) {
        val welcomeLayout = findViewById<LinearLayout>(R.id.welcomeLayout)
        val gameLayout = findViewById<LinearLayout>(R.id.gameLayout)
        val resultLayout = findViewById<LinearLayout>(R.id.resultLayout)
        val finalLayout = findViewById<LinearLayout>(R.id.finalLayout)

        welcomeLayout.visibility = View.GONE
        gameLayout.visibility = View.GONE
        resultLayout.visibility = View.VISIBLE
        finalLayout.visibility = View.GONE

        // show what happened this round
        val resultTitle = findViewById<TextView>(R.id.resultTitle)
        val yourNumberText = findViewById<TextView>(R.id.yourNumberText)
        val botNumberText = findViewById<TextView>(R.id.botNumberText)
        val avgText = findViewById<TextView>(R.id.avgText)
        val spiderText = findViewById<TextView>(R.id.spiderText)
        val winnerText = findViewById<TextView>(R.id.winnerText)
        val roundScoreText = findViewById<TextView>(R.id.roundScoreText)

        yourNumberText.text = "Your number: $userNum"
        botNumberText.text = "Bot's number: $botNum"

        // rounding to 2 decimal places manually
        val avgRounded = Math.round(avg * 100.0) / 100.0
        val spiderRounded = Math.round(spiderNum * 100.0) / 100.0

        avgText.text = "Average: $avgRounded"
        spiderText.text = "Spider Number (avg x 0.8): $spiderRounded"

        if (winner == "Tie") {
            resultTitle.text = "It's a Tie! 🕷️"
            winnerText.text = "Both players were equally close!"
        } else if (winner == "You") {
            resultTitle.text = "You Win This Round! ♦️"
            winnerText.text = "Your distance: ${"%.2f".format(myDist)} vs Bot: ${"%.2f".format(botDist)}"
        } else {
            resultTitle.text = "Bot Wins This Round! 🤖"
            winnerText.text = "Bot distance: ${"%.2f".format(botDist)} vs Yours: ${"%.2f".format(myDist)}"
        }

        roundScoreText.text = "Score → You: $myScore  |  Bot: $botScore"

        val nextBtn = findViewById<Button>(R.id.nextButton)

        // check if game is over
        if (roundNumber >= totalRounds) {
            nextBtn.text = "See Final Results"
        } else {
            nextBtn.text = "Next Round"
        }

        nextBtn.setOnClickListener {
            if (roundNumber >= totalRounds) {
                showFinalScreen()
            } else {
                roundNumber++
                showGameScreen()
            }
        }
    }

    fun showFinalScreen() {
        val welcomeLayout = findViewById<LinearLayout>(R.id.welcomeLayout)
        val gameLayout = findViewById<LinearLayout>(R.id.gameLayout)
        val resultLayout = findViewById<LinearLayout>(R.id.resultLayout)
        val finalLayout = findViewById<LinearLayout>(R.id.finalLayout)

        welcomeLayout.visibility = View.GONE
        gameLayout.visibility = View.GONE
        resultLayout.visibility = View.GONE
        finalLayout.visibility = View.VISIBLE

        val finalTitle = findViewById<TextView>(R.id.finalTitle)
        val finalScoreText = findViewById<TextView>(R.id.finalScoreText)
        val finalMessageText = findViewById<TextView>(R.id.finalMessageText)
        val playAgainBtn = findViewById<Button>(R.id.playAgainButton)

        finalScoreText.text = "Final Score\nYou: $myScore  |  Bot: $botScore"

        if (myScore > botScore) {
            finalTitle.text = "YOU ARE THE\nKING OF DIAMONDS! ♦️"
            finalMessageText.text = "You defeated the spider and escaped the arena!"
        } else if (botScore > myScore) {
            finalTitle.text = "THE BOT WINS... 🕷️"
            finalMessageText.text = "The spider claimed you. Better luck next time!"
        } else {
            finalTitle.text = "IT'S A DRAW! 🤝"
            finalMessageText.text = "Neither the spider nor you could claim victory."
        }

        playAgainBtn.setOnClickListener {
            showWelcomeScreen()
        }
    }
}
