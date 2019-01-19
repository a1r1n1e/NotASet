package ru.whobuys.vovch.notaset;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

public class OneFourActivity extends AppCompatActivity {

    private Deck currentDeck;
    private final static int FIELD_SIZE = 12;
    private ArrayList<Card> set;

    private int player1Score;
    private int player2Score;
    private int player3Score;
    private int player4Score;

    private int mode;

    View.OnClickListener cardButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CardButton cardButton = (CardButton) v;
            cardButton.setBackgroundColor(Color.BLUE);
            set.add(cardButton.getCard());

            if(set.size() == 3){
                if(isGoodSet((Card[]) set.toArray(new Card[3]))){
                    switch (mode){
                        case 1:
                            player1Score++;
                            break;
                        case 2:
                            player2Score++;
                            break;
                        case 3:
                            player3Score++;
                            break;
                        case 4:
                            player4Score++;
                            break;
                    }
                    currentDeck.removeSet((Card[]) set.toArray(new Card[3]));
                    FrameLayout[] layouts;
                    for(Card card : set){
                        card.getButton().setBackgroundColor(Color.TRANSPARENT);
                        for(FrameLayout layout : card.getFrameLayouts()){
                            layout.removeAllViews();
                        }
                    }
                    if(currentDeck.isAny()) {
                        Card[] newBunch = currentDeck.getNextThree();
                        Card currentCard;
                        FrameLayout frameLayout;
                        for(int i = 0; i < 3; ++i){                    // Same Sizes
                            currentCard = set.get(i);
                            newBunch[i].setButton(currentCard.getButton());
                            newBunch[i].setFrameLayouts(currentCard.getFrameLayouts());
                            currentCard.getButton().setCard(newBunch[i]);

                            layouts = currentCard.getFrameLayouts();
                            for(int k = 0; k < 3; ++k) {
                                frameLayout = layouts[k];
                                ImageView imageView = new ImageView(frameLayout.getContext());
                                imageView.setImageBitmap(newBunch[i].threeImages[k]);
                                frameLayout.addView(imageView);
                            }
                        }
                    }
                } else {
                    switch (mode){
                        case 1:
                            player1Score--;
                            break;
                        case 2:
                            player2Score--;
                            break;
                        case 3:
                            player3Score--;
                            break;
                        case 4:
                            player4Score--;
                            break;
                    }
                    for(Card card : set){
                        card.getButton().setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                switch (mode){
                    case 1:
                        PlayerButton button1 = (PlayerButton) findViewById(R.id.button_1);
                        button1.setText("Player 1\n" + String.valueOf(player1Score));
                        break;
                    case 2:
                        PlayerButton button2 = (PlayerButton) findViewById(R.id.button_2);
                        button2.setText("Player 2\n" + String.valueOf(player2Score));
                        break;
                    case 3:
                        PlayerButton button3 = (PlayerButton) findViewById(R.id.button_3);
                        button3.setText("Player 3\n" + String.valueOf(player3Score));
                        break;
                    case 4:
                        PlayerButton button4 = (PlayerButton) findViewById(R.id.button_4);
                        button4.setText("Player 4\n" + String.valueOf(player4Score));
                        break;
                }

                set.clear();
                set.trimToSize();

                mode = 0;
                blockCards();
                adBlockPlayers();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_four);

        player1Score = 0;
        player2Score = 0;
        player3Score = 0;
        player4Score = 0;
        mode = 0;

        currentDeck = new Deck();
        set = new ArrayList<>();

        adBlockPlayers();
        setPlayersListeners();

        Card[] cards = currentDeck.getField();

        String[] lines = {"one", "two", "three"};

        Card currentCard;
        String name;
        int buttonId, frameId;
        FrameLayout[] layouts = new FrameLayout[3];
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 4; ++j) {
                currentCard = cards[i * 4 + j];
                name = "card_button_" + String.valueOf(i + 1) + "_" + String.valueOf(j + 1);
                buttonId = getResources().getIdentifier(name, "id", getPackageName());
                CardButton button = (CardButton) findViewById(buttonId);
                button.setAlpha(0.3f);
                button.setCard(currentCard);
                button.setOnClickListener(cardButtonListener);
                currentCard.setButton(button);
                for(int k = 0; k < 3; ++k) {
                    name = "line_" + lines[i] + "_" +
                            String.valueOf(j + 1) + "_"  + String.valueOf(k + 1);
                    frameId = getResources().getIdentifier(name,"id", getPackageName());
                    FrameLayout frameLayout = (FrameLayout) findViewById(frameId);
                    ImageView imageView = new ImageView(frameLayout.getContext());
                    imageView.setImageBitmap(currentCard.threeImages[k]);
                    frameLayout.addView(imageView);
                    layouts[k] = frameLayout;
                }
                currentCard.setFrameLayouts(layouts.clone());
            }
        }
        blockCards();
    }

    private void setPlayersListeners(){
        PlayerButton button1 = (PlayerButton) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockPlayers();
                adBlockCards();
                mode = 1;
            }
        });
        PlayerButton button2 = (PlayerButton) findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockPlayers();
                adBlockCards();
                mode = 2;
            }
        });
        PlayerButton button3 = (PlayerButton) findViewById(R.id.button_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockPlayers();
                adBlockCards();
                mode = 3;
            }
        });
        PlayerButton button4 = (PlayerButton) findViewById(R.id.button_4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockPlayers();
                adBlockCards();
                mode = 4;
            }
        });
    }

    private boolean isGoodSet(Card[] cards){
        // Hardcoded set length of 3
        if((cards[0].color + cards[1].color + cards[2].color) % 3 != 0){
           return false;
        }
        if((cards[0].items + cards[1].items + cards[2].items) % 3 != 0){
            return false;
        }
        if((cards[0].coloring + cards[1].coloring + cards[2].coloring) % 3 != 0){
            return false;
        }
        if((cards[0].shape + cards[1].shape + cards[2].shape) % 3 != 0){
            return false;
        }
        return true;
    }

    private void blockPlayers(){
        PlayerButton button1 = (PlayerButton) findViewById(R.id.button_1);
        button1.setFocusable(false);
        button1.setClickable(false);
        PlayerButton button2 = (PlayerButton) findViewById(R.id.button_2);
        button2.setFocusable(false);
        button2.setClickable(false);
        PlayerButton button3 = (PlayerButton) findViewById(R.id.button_3);
        button3.setFocusable(false);
        button3.setClickable(false);
        PlayerButton button4 = (PlayerButton) findViewById(R.id.button_4);
        button4.setFocusable(false);
        button4.setClickable(false);
    }

    private void adBlockPlayers(){
        PlayerButton button1 = (PlayerButton) findViewById(R.id.button_1);
        button1.setFocusable(true);
        button1.setClickable(true);
        PlayerButton button2 = (PlayerButton) findViewById(R.id.button_2);
        button2.setFocusable(true);
        button2.setClickable(true);
        PlayerButton button3 = (PlayerButton) findViewById(R.id.button_3);
        button3.setFocusable(true);
        button3.setClickable(true);
        PlayerButton button4 = (PlayerButton) findViewById(R.id.button_4);
        button4.setFocusable(true);
        button4.setClickable(true);
    }

    private void blockCards(){
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 4; ++j) {
                String name = "card_button_" + String.valueOf(i + 1) + "_"  + String.valueOf(j + 1);
                int buttonId = getResources().getIdentifier(name,"id", getPackageName());
                CardButton button = (CardButton) findViewById(buttonId);
                button.setFocusable(false);
                button.setClickable(false);
            }
        }
    }

    private void adBlockCards(){
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 4; ++j) {
                String name = "card_button_" + String.valueOf(i + 1) + "_"  + String.valueOf(j + 1);
                int buttonId = getResources().getIdentifier(name,"id", getPackageName());
                CardButton button = (CardButton) findViewById(buttonId);
                button.setFocusable(true);
                button.setClickable(true);
            }
        }
    }

    private class Deck{

        public ArrayList<Integer> deck;

        protected Deck(){
            ArrayList<Integer> startDeck = new ArrayList<>();
            for(int i = 0; i < 81; ++i){
                startDeck.add(Integer.valueOf(i));
            }
            ArrayList<Integer> resultDeck = new ArrayList<>();
            int removingItemNumber, removingItem;
            Random random = new Random();
            for(int i = 81; i > 0; --i){
                removingItemNumber = random.nextInt(i);
                removingItem = startDeck.get(removingItemNumber);
                startDeck.remove(removingItemNumber);
                resultDeck.add(Integer.valueOf(removingItem));
            }
            deck = resultDeck;
        }

        protected Card[] getField(){
            Card[] firstDozen = new Card[FIELD_SIZE];
            for(int i  = 0; i < FIELD_SIZE; ++i){
                firstDozen[i] = new Card(deck.get(i));
            }
            return firstDozen;
        }

        protected void removeSet(Card[] set){
            for(Card card : set){
                deck.remove(Integer.valueOf(card.number));
                deck.trimToSize();
            }
        }

        protected Card[] getNextThree(){            // Getting last three up to dozen
            Card[] bunch = new Card[3];
            for(int i = 0; i < 3; ++i){
                bunch[i] = new Card(deck.get(FIELD_SIZE - 1 - i));
            }
            return bunch;
        }

        protected boolean isAny(){
            return deck.size() > 0;
        }

    }

    protected class Card{

        public int number;

        public int items;
        public int color;
        public int shape;
        public int coloring;

        public Bitmap[] threeImages;

        private CardButton button;

        private FrameLayout[] frameLayouts;

        public Card(int number){

            this.items = number % 3;
            this.color = (number / 3) % 3;
            this.shape = (number / 9) % 3;
            this.coloring = (number / 27) % 3;

            this.number = number;

            this.threeImages = new Bitmap[3];

            switch (number % 3){
                case 0:
                    threeImages[0] = BitmapFactory.decodeResource(getResources(), R.drawable.wit);
                    threeImages[1] = getSubImage(number / 3);
                    threeImages[2] = BitmapFactory.decodeResource(getResources(), R.drawable.wit);
                    break;
                case 1:
                    threeImages[0] = getSubImage(number / 3);
                    threeImages[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wit);
                    threeImages[2] = getSubImage(number / 3);
                    break;
                case 2:
                    threeImages[0] = getSubImage(number / 3);
                    threeImages[1] = getSubImage(number / 3);
                    threeImages[2] = getSubImage(number / 3);
                    break;
            }
        }

        public CardButton getButton() {
            return button;
        }

        public void setButton(CardButton button) {
            this.button = button;
        }

        public FrameLayout[] getFrameLayouts() {
            return frameLayouts;
        }

        public void setFrameLayouts(FrameLayout[] frameLayouts) {
            this.frameLayouts = frameLayouts;
        }

        private Bitmap getSubImage(int number){

            String imageName = "";
            switch (number % 3){
                case 0:
                    imageName += "ovaal";
                    break;
                case 1:
                    imageName += "ruit";
                    break;
                case 2:
                    imageName += "tilde";
                    break;
            }

            imageName += String.valueOf((number / 9) % 3 + 1);
            switch ((number / 3) % 3){
                case 0:
                    imageName += "groen";
                    break;
                case 1:
                    imageName += "paars";
                    break;
                case 2:
                    imageName += "rood";
                    break;
            }

            Drawable drawable = getResources().getDrawable(getResources()
                    .getIdentifier(imageName, "drawable", getPackageName()));
            return ((BitmapDrawable) drawable).getBitmap();
        }
    }
}
