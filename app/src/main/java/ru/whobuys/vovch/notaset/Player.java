package ru.whobuys.vovch.notaset;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class Player {
    private int id;
    private PlayerButton button;
    private TextView textView;
    private int score;
    private boolean isSpoiled;

    public Player(int id){
        this.id = id;
        this.score = 0;
        this.isSpoiled = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setButton(PlayerButton button) {
        this.button = button;
    }

    public PlayerButton getButton() {
        return button;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore(){
        this.score++;
        if(textView != null){
            textView.setText(String.valueOf(score));
        }
    }

    public void decreaseScore(){
        this.score--;
        if(textView != null){
            textView.setText(String.valueOf(score));
        }
    }

    public int getColor(){
        int result;
        switch (id){
            case 1:
                result =  Color.CYAN;
                break;
            case 2:
                result =  Color.YELLOW;
                break;
            case 3:
                result =  Color.MAGENTA;
                break;
            case 4:
                result = Color.RED;
                break;
                default:
                    result = Color.CYAN;
        }
        return result;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public boolean isSpoiled() {
        return isSpoiled;
    }

    public void spoil() {
        isSpoiled = true;
    }

    public void clear(){ isSpoiled = false; }
}
