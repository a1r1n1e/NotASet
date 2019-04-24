package ru.whobuys.vovch.notaset;

import android.graphics.Color;
import android.widget.TextView;

public class Player {
    private int id;
    private PlayerButton button;
    private TextView textView;
    private int score;
    private boolean isSpoiled;

    static final int DEFAULT_ID = -100;
    static final int DEFAULT_SCORE = 0;
    static final boolean DEFAULT_SPOILED_STATE = false;

    Player(){
        this.id = DEFAULT_ID;
        this.score = DEFAULT_SCORE;
        this.isSpoiled = DEFAULT_SPOILED_STATE;
    }
    Player(int id){
        this.id = id;
        this.score = DEFAULT_SCORE;
        this.isSpoiled = DEFAULT_SPOILED_STATE;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    void setButton(PlayerButton button) {
        this.button = button;
    }

    PlayerButton getButton() {
        return button;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    void increaseScore(){
        this.score++;
        if(textView != null){
            textView.setText(String.valueOf(score));
        }
    }

    void decreaseScore(){
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

    void setTextView(TextView textView) {
        this.textView = textView;
    }

    boolean isSpoiled() {
        return isSpoiled;
    }

    void spoil() {
        isSpoiled = true;
    }

    void clear(){ isSpoiled = false; }
}
