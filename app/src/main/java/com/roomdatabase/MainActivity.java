package com.roomdatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roomdatabase.adapter.WordListAdapter;
import com.roomdatabase.dao.WordDao;
import com.roomdatabase.databinding.ActivityMainBinding;
import com.roomdatabase.db.WordRoomDatabase;
import com.roomdatabase.entity.Word;
import com.roomdatabase.viewmodel.WordViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {


    /**
     * https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#4
     */

    private ActivityMainBinding mMainActivityBinding;
    private WordViewModel mWordViewModel;
    private int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        /** bind activity  **/
        mMainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        /** get the viewModel reference  **/
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        initUi();


    }

    private void initUi() {
        RecyclerView recyclerView = findViewById(R.id.rv_words);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.iv_delete_db).setOnClickListener(v -> deleteDb());
        findViewById(R.id.iv_add_words).setOnClickListener(v -> onAddWordClick());

        mMainActivityBinding.setLabel("Room Database Sample");
        mMainActivityBinding.setDeleteImg(R.drawable.iv_delete);
        mMainActivityBinding.setIvAddWords(R.drawable.iv_add);

        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                // Update the cached copy of the words in the adapter.

                if (words != null && words.size() > 0) {
                    adapter.setWords(words);
                    mMainActivityBinding.tvError.setVisibility(View.GONE);
                } else {
                    mMainActivityBinding.tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onAddWordClick() {
        Intent intent = new Intent(MainActivity.this, AddNewWordActivity.class);
        startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
    }

    private void deleteDb() {
        mWordViewModel.deleteWordDb();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(AddNewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }


    public static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WordDao mDao;

        public PopulateDbAsync(WordRoomDatabase db) {
            mDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
//            mDao.deleteAll();
            Word word = new Word("Hello");
            mDao.insert(word);
            word = new Word("How are you?");
            mDao.insert(word);
            return null;
        }
    }
}
