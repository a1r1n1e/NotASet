package ru.whobuys.vovch.notaset;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class CardButton extends Button {

    private OneFourActivity.Card card;

    public CardButton(Context context, AttributeSet atr){
        super(context, atr);
        this.card = null;
    }

    protected void setCard(OneFourActivity.Card card){
        this.card = card;
    }

    protected OneFourActivity.Card getCard(){
        return card;
    }
}
