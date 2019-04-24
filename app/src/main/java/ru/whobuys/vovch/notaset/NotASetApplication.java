package ru.whobuys.vovch.notaset;

import android.app.Application;
import android.util.SparseArray;

public class NotASetApplication extends Application {

    private SparseArray<OneFourActivity.Card> Table;

    @Override
    public void onCreate() {
        super.onCreate();
        Table = new SparseArray<>();
    }

    public class OnlinePlayer extends Player{
        private boolean isRegistered;
        private int room;

        private final int DEFAULT_ROOM_ID = -100;

        OnlinePlayer(){
            super();
            this.isRegistered = false;
            this.room = DEFAULT_ROOM_ID;
        }
        OnlinePlayer(int id) {
            super(id);
            this.isRegistered = (id == Player.DEFAULT_ID);
            this.room = DEFAULT_ROOM_ID;
        }

        boolean isRegistered(){
            return this.isRegistered;
        }

        void setRegiistered(){
            this.isRegistered = true;
        }

        void setNotRegistered(){
            this.isRegistered = false;
        }
    }
}
