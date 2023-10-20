package com.app.horsegame

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var cellSelectedX = 0
    private var cellSelectedY = 0
    private var moves = 64;
    private var movesRequired = 4
    private var options = 0
    private var bonus = 0
    private var widhtBonus = 0
    private var levelMoves = 64

    private var nameColorBlack = "black_cell"
    private var nameColorWhite = "white_cell"
    private lateinit var board: Array<IntArray>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initScreenGame()
        resetBoard()
        setFirstPosition()
    }
    fun checkCellCliked(view: View) {
        var name = view.tag.toString()
        var x = name.subSequence(1,2).toString().toInt()
        var y = name.subSequence(2,3).toString().toInt()

        checkCell(x,y)
    }

    private fun checkCell(x: Int, y: Int) {
        var difX = x - cellSelectedX
        var difY = y - cellSelectedY
        var checkTrue = false

        if(difX == 1 && difY == 2) checkTrue = true
        if(difX == 1 && difY == -2) checkTrue = true
        if(difX == 2 && difY == 1) checkTrue = true
        if(difX == 2 && difY == -1) checkTrue = true
        if(difX == -1 && difY == 2) checkTrue = true
        if(difX == -1 && difY == -2) checkTrue = true
        if(difX == -2 && difY == 1) checkTrue = true
        if(difX == -2 && difY == -1) checkTrue = true

        if(board[x][y] ==1) checkTrue = false
        if(checkTrue) selectCell(x, y)
    }

    private fun resetBoard() {
        // 0 esta libre
        // 1 casilla marcada
        // 2 es un bonus
        // 9  es una opcion del movimiento actual
        board = arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        )
    }

    private fun setFirstPosition() {
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()

        cellSelectedX = x
        cellSelectedY = y
        selectCell(x, y)
    }
    private fun growProgressBonus(){
        var movesDone = levelMoves - moves
        var bonusDone = movesDone / movesRequired
        var movesRest = movesRequired * (bonusDone)
        var bonusGrow = movesDone - movesRest
        var v = findViewById<View>(R.id.vNesbonus)
        var widthBonus = ((widhtBonus/movesRequired) * bonusGrow).toFloat()
        var height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics()).toInt()
        var width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthBonus, getResources().getDisplayMetrics()).toInt()
        v.setLayoutParams(TableRow.LayoutParams(width, height))
    }
    private fun selectCell(x: Int, y: Int) {
        moves--
        var tvMovesData = findViewById<TextView>(R.id.tvMovesData)
        tvMovesData.text = moves.toString()

        growProgressBonus()
        if(board[x][y] == 2){
            bonus++
            var tvBonusData = findViewById<TextView>(R.id.tvBonusData)
            //tvBonusData.text = " + $bonus"
        }

        board [x][y] = 1
        paintHorseCell(cellSelectedX, cellSelectedY, "previous_cell")
        cellSelectedX = x
        cellSelectedY = y

        clearOptions()
        paintHorseCell(x, y, "selected_cell")
        checkOption(x,y)
        if(moves > 0){
            checkNewBonus()
            //checkGameOver()
        } else{
            //checkSuccesFullEnd()
        }
    }

    private fun checkNewBonus() {
        if(moves % movesRequired == 0){
            var bonusCellX = 0
            var bonusCellY = 0
            var bonusCell = false
            while(bonusCell == false){
                bonusCellX = (0..7).random()
                bonusCellY = (0..7).random()
                if(board[bonusCellX][bonusCellY] == 0) bonusCell = true
            }
            board[bonusCellX][bonusCellY] = 2
            paintBonusCell(bonusCellX, bonusCellY)
        }
    }

    private fun paintBonusCell(x: Int, y: Int) {
        var iv: ImageView = findViewById(resources.getIdentifier("c$x$y", "id", packageName))
        iv.setImageResource(R.drawable.bonus)
    }

    private fun clearOptions() {
        for( i in 0..7){
            for( j in 0..7){
                if(board[i][j] == 9 || board[i][j] == 2){
                    if(board[i][j] == 9) board[i][j] = 0
                    clearOption(i, j)
                }
            }
        }
    }

    private fun clearOption(x: Int, y: Int) {
        var iv: ImageView =  findViewById(resources.getIdentifier("c$x$y","id", packageName))
        if(checkColorCell(x,y) == "black"){
            iv.setBackgroundColor(ContextCompat.getColor(this,
                resources.getIdentifier(nameColorBlack, "color", packageName)))
        }else {
            iv.setBackgroundColor(ContextCompat.getColor(this,
                resources.getIdentifier(nameColorWhite, "color", packageName)))
        }
        if(board[x][y] == 1){
            iv.setBackgroundColor(ContextCompat.getColor(this,
                resources.getIdentifier("previus_cell", "color", packageName)))
        }
    }

    private fun checkOption(x: Int, y: Int) {
        options = 0

        checkMove(x,y,1,2)
        checkMove(x,y,2,1)
        checkMove(x,y,1,-2)
        checkMove(x,y,2,-1)
        checkMove(x,y,-1,2)
        checkMove(x,y,-2,1)
        checkMove(x,y,-1,-2)
        checkMove(x,y,-2,-1)

        var tvOptionsData = findViewById<TextView>(R.id.tvOptionsData)
        tvOptionsData.text = options.toString()
    }

    private fun checkMove(x: Int, y: Int, movX: Int, movY: Int) {
        var optionX = x + movX
        var optionY = y + movY
        if(optionX < 8 && optionY < 8 && optionX >= 0 && optionY >= 0){
            if(board[optionX][optionY] == 0 || board[optionX][optionY] == 2){
                options++
                paintOptionss(optionX, optionY)
                if(board[optionX][optionY] == 0) board[optionX][optionY] = 9
            }
        }
    }

    private fun paintOptionss(x: Int, y: Int) {
        var iv: ImageView =  findViewById(resources.getIdentifier("c$x$y","id", packageName))
        if(checkColorCell(x,y) == "black") iv.setBackgroundResource(R.drawable.option_black)
        else iv.setBackgroundResource(R.drawable.option_white)
    }

    private fun checkColorCell(x: Int, y: Int): String {
        var color = ""
        var blackColomnX = arrayOf(0,2,4,6)
        var blackRowX = arrayOf(1,3,5,7)
        if((blackColomnX.contains(x) && blackColomnX.contains(y))
            || blackRowX.contains(x) && blackRowX.contains(y)){
            color = "black"
        }
        else{
            color = "white"
        }
        return color
    }

    private fun paintHorseCell(x: Int, y: Int, color: String) {
        var iv: ImageView =  findViewById(resources.getIdentifier("c$x$y","id", packageName))
        iv.setBackgroundColor(ContextCompat.getColor(this, resources.getIdentifier(color, "color", packageName)))
        iv.setImageResource(R.drawable.horse)
    }

    private fun initScreenGame(){
        setSizeboard()
        hideMesage()
    }

    private fun hideMesage() {
        var lyMessage = findViewById<LinearLayout>(R.id.lyMessage)
        lyMessage.visibility = View.INVISIBLE
    }

    private fun setSizeboard() {
        var iv: ImageView
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        var widthDP = (width / getResources().getDisplayMetrics().density)
        var lateralMarginsDP = 0
        val widthCell = (widthDP - lateralMarginsDP)/8
        val heigthCell = widthCell

        widhtBonus = 2 * widthCell.toInt()
        for(i in 0..7){
            for(j in 0..7){
                iv = findViewById(resources.getIdentifier("c$i$j","id", packageName))
                var height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heigthCell, getResources().getDisplayMetrics()).toInt()
                var width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthCell, getResources().getDisplayMetrics()).toInt()
                iv.setLayoutParams(TableRow.LayoutParams(width, height))
            }
        }
    }


}