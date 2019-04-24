package ru.whobuys.vovch.notaset;

import android.os.AsyncTask;

class CheckLoginAsyncTask extends AsyncTask<Object, Void, Integer>{
    private NotASetApplication application;

    @Override
    public Integer doInBackground(Object... loginPair) {
        int result = 0;
        application = (NotASetApplication) loginPair[0];

        return result;
    }

    @Override
    public void onPostExecute(Integer result){

    }
}
