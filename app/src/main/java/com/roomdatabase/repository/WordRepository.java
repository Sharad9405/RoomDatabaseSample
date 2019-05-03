package com.roomdatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.roomdatabase.dao.WordDao;
import com.roomdatabase.db.WordRoomDatabase;
import com.roomdatabase.entity.Word;

import java.util.List;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    public  WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
    }

    public LiveData<List<Word>> getmAllWords() {
        return mAllWords;
    }

    public void insert(Word word) {
        new insertAsyncTask(mWordDao).execute(word);
    }
    public void deleteAll() {
//        mWordDao.deleteAll();
        new DeleteData(mWordDao).execute();
    }

 /** These transaction should be done in background thread otherwise it will not work &
  * app will  crash cause these are not supposed to do in ui thread
  *  **/
    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

     private static class DeleteData extends AsyncTask<Void, Void, Void> {
        private WordDao dao;

        DeleteData(WordDao d) {
            this.dao = d;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            dao.deleteAll();
            return null;
        }
    }


}
