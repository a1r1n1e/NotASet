package ru.whobuys.vovch.notaset;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class OneFourActivity extends AppCompatActivity {

    Animation flashAnimationIn;
    Animation flashAnimationOut;

    private Deck currentDeck;
    private final static int FIELD_SIZE = 12;
    private ArrayList<Card> set;

    private ArrayList<Player> players;

    private Player currentPlayer;

    View.OnClickListener cardButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CardButton cardButton = (CardButton) v;
            cardButton.setBackgroundColor(currentPlayer.getColor());
            cardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    set.remove(cardButton.getCard());
                    cardButton.setBackgroundColor(Color.TRANSPARENT);
                    cardButton.setOnClickListener(cardButtonListener);
                }
            });
            set.add(cardButton.getCard());
            currentPlayer.spoil();

            if(set.size() == 3){
                if(isGoodSet((Card[]) set.toArray(new Card[3]))){

                    currentPlayer.increaseScore();

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
                    } else{
                        for(Card card : set){
                            card.getButton().setBackgroundColor(Color.TRANSPARENT);
                            for(FrameLayout layout : card.getFrameLayouts()){
                                layout.setBackgroundColor(Color.BLACK);
                            }
                        }
                    }
                } else {
                    currentPlayer.decreaseScore();
                    for(Card card : set){
                        card.getButton().setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                set.clear();
                set.trimToSize();

                blockCards();
                adBlockPlayers();
                setPlayersListeners();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_four);

        FloatingActionButton refresh_fab = (FloatingActionButton) findViewById(R.id.fab_refresh);
        refresh_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Card[] result = new Card[0];
                if(currentDeck.length() > 12){
                    Card[] bunch = new Card[3];
                    int middle = random.nextInt(10);
                    bunch[0] = currentDeck.getCard(middle);
                    bunch[1] = currentDeck.getCard(middle + 1);
                    bunch[2] = currentDeck.getCard(middle + 2);

                    currentDeck.removeSet(bunch);
                    FrameLayout[] layouts;
                    for(Card card : bunch){
                        card.getButton().setBackgroundColor(Color.TRANSPARENT);
                        for(FrameLayout layout : card.getFrameLayouts()){
                            layout.removeAllViews();
                        }
                    }
                    Card[] newBunch = currentDeck.getNextThree();
                    Card currentCard;
                    FrameLayout frameLayout;
                    for(int i = 0; i < 3; ++i){                    // Same Sizes
                        currentCard = bunch[i];
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
            }
        });

        players = new ArrayList<>();
        for(int i = 0; i < 4; ++i){
            players.add(new Player(i + 1));
        }
        PlayerButton playerButton = (PlayerButton) findViewById(R.id.button_1);
        TextView textView = (TextView) findViewById(R.id.player_1_score);
        players.get(0).setButton(playerButton);
        players.get(0).setTextView(textView);
        playerButton = (PlayerButton) findViewById(R.id.button_2);
        textView = (TextView) findViewById(R.id.player_2_score);
        players.get(1).setButton(playerButton);
        players.get(1).setTextView(textView);
        playerButton = (PlayerButton) findViewById(R.id.button_3);
        textView = (TextView) findViewById(R.id.player_3_score);
        players.get(2).setButton(playerButton);
        players.get(2).setTextView(textView);
        playerButton = (PlayerButton) findViewById(R.id.button_4);
        textView = (TextView) findViewById(R.id.player_4_score);
        players.get(3).setButton(playerButton);
        players.get(3).setTextView(textView);

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
        final ImageView flashView = (ImageView) findViewById(R.id.flash_image_view);

        flashAnimationIn = AnimationUtils.loadAnimation(this, R.anim.alpha_animation_up);
        flashAnimationIn.setFillAfter(true);
        flashAnimationOut = AnimationUtils.loadAnimation(this, R.anim.alpha_animation_down);
        flashAnimationOut.setFillAfter(true);

        flashAnimationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flashView.startAnimation(flashAnimationOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        PlayerButton button1 = players.get(0).getButton();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockOthers(players.get(0));
                adBlockCards();
                flashView.setBackgroundColor(players.get(0).getColor());
                flashView.startAnimation(flashAnimationIn);
                currentPlayer = players.get(0);
            }
        });
        PlayerButton button2 = (PlayerButton) findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockOthers(players.get(1));
                adBlockCards();
                flashView.setBackgroundColor(players.get(1).getColor());
                flashView.startAnimation(flashAnimationIn);
                currentPlayer = players.get(1);
            }
        });
        PlayerButton button3 = (PlayerButton) findViewById(R.id.button_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockOthers(players.get(2));
                adBlockCards();
                flashView.setBackgroundColor(players.get(2).getColor());
                flashView.startAnimation(flashAnimationIn);
                currentPlayer = players.get(2);
            }
        });
        PlayerButton button4 = (PlayerButton) findViewById(R.id.button_4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockOthers(players.get(3));
                adBlockCards();
                flashView.setBackgroundColor(players.get(3).getColor());
                flashView.startAnimation(flashAnimationIn);
                currentPlayer = players.get(3);
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
        PlayerButton button1 = players.get(0).getButton();
        button1.setFocusable(false);
        button1.setClickable(false);
        PlayerButton button2 = players.get(1).getButton();
        button2.setFocusable(false);
        button2.setClickable(false);
        PlayerButton button3 = players.get(2).getButton();
        button3.setFocusable(false);
        button3.setClickable(false);
        PlayerButton button4 = players.get(3).getButton();
        button4.setFocusable(false);
        button4.setClickable(false);
    }

    private void blockOthers(Player player){
        blockPlayers();
        PlayerButton button = player.getButton();
        button.setFocusable(true);
        button.setClickable(true);
        for(Player otherPlayer : players){
            if(otherPlayer != player){
                otherPlayer.getButton().setBackgroundColor(Color.BLACK);
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(set.size() == 0 && !currentPlayer.isSpoiled()) {
                    adBlockPlayers();
                    blockCards();
                } else {

                    currentPlayer.decreaseScore();
                    currentPlayer.clear();

                    for(Card card : set){
                        card.getButton().setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                adBlockPlayers();
                setPlayersListeners();
            }
        });
    }

    private void adBlockPlayers(){
        currentPlayer = null;

        PlayerButton button1 = (PlayerButton) findViewById(R.id.button_1);
        button1.setBackgroundColor(players.get(0).getColor());
        button1.setFocusable(true);
        button1.setClickable(true);
        PlayerButton button2 = (PlayerButton) findViewById(R.id.button_2);
        button2.setBackgroundColor(players.get(1).getColor());
        button2.setFocusable(true);
        button2.setClickable(true);
        PlayerButton button3 = (PlayerButton) findViewById(R.id.button_3);
        button3.setBackgroundColor(players.get(2).getColor());
        button3.setFocusable(true);
        button3.setClickable(true);
        PlayerButton button4 = (PlayerButton) findViewById(R.id.button_4);
        button4.setBackgroundColor(players.get(3).getColor());
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
        set.clear();
        set.trimToSize();

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
        private ArrayList<Card> cards;

        protected Deck(){
            ArrayList<Integer> startDeck = new ArrayList<>();
            cards = new ArrayList<>();
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
                cards.add(firstDozen[i]);
            }
            return firstDozen;
        }

        protected void removeSet(Card[] set){
            for(Card card : set){
                deck.remove(Integer.valueOf(card.number));
                deck.trimToSize();
                cards.remove(card);
            }
        }

        protected Card[] getNextThree(){            // Getting last three up to dozen
            Card[] bunch = new Card[3];
            for(int i = 0; i < 3; ++i){
                bunch[i] = new Card(deck.get(FIELD_SIZE - 1 - i));
                cards.add(bunch[i]);
            }
            return bunch;
        }

        protected boolean isAny(){
            return deck.size() > 12;
        }

        protected int length(){
            return deck.size();
        }

        protected int get(int i){
            return deck.get(i);
        }

        protected Card getCard(int i){           // dangerous, can cause NullPointerException
            return cards.get(i);
        }

        protected boolean isSetOnTable(){
            Card[] bunch;
            for (int i = 0; i < deck.size() && i < 12; ++i){
                for (int j = i + 1; j < deck.size() && j < 12; ++j){
                    for (int k = j + 1; j < deck.size() && j < 12; ++j){
                        bunch = new Card[]{cards.get(i), cards.get(j), cards.get(k)};
                        if(isGoodSet(bunch)){
                            return true;
                        }
                    }
                }
            }
            return false;
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
