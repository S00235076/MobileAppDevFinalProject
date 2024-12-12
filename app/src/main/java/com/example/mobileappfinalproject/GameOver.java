package com.example.mobileappfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GameOver extends AppCompatActivity {
TextView score;
    EditText editText;
ListView lv;
    List<String> highScoresStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
lv = findViewById(R.id.listview1);
        score = findViewById(R.id.score);
        Intent i = getIntent();
        String sequenceScore = getIntent().getStringExtra("score");
        score.setText(sequenceScore);
        DatabaseHandler db = new DatabaseHandler(this);

        List<HighScoreClass> highscore = db.getAllHighscore();




        //db.emptyContacts();     // empty table if required
        db.emptyHighscore();
        // Inserting Contacts
        Log.i("Insert: ", "Inserting ..");
        db.addHighscore(new HighScoreClass("Oran", 10));
        db.addHighscore(new HighScoreClass("Ethan", 8));
        db.addHighscore(new HighScoreClass("James", 9));
        db.addHighscore(new HighScoreClass("Jake", 3));
        db.addHighscore(new HighScoreClass("Aoife", 2));
        db.addHighscore(new HighScoreClass("Olivia", 1));

        // Reading all contacts
        Log.i("Reading: ", "Reading all contacts..");

        for (HighScoreClass cn : highscore) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Highscore: " +
                    cn.getHighscore();
            Log.i("Name: ", log);

        }

        Log.i("divider", "====================");

      HighScoreClass singleUser = db.getHighscoreClass(5);
        Log.i("contact 5 is ", singleUser.getName());
        Log.i("divider", "====================");

        // Calling SQL statement
        int userCount = db.getHighscoreCount();
        Log.i("User count: ", String.valueOf(userCount));

    }
    public Object topFiveFilter()
    {
        DatabaseHandler db = new DatabaseHandler(this);
        List<HighScoreClass> highscoreList = db.top5Highscore();
        Log.i("Name: ", "list successful");
        for (HighScoreClass cn2 : highscoreList) {


            String log = "Id: " + cn2.getID() + " ,Name: " + cn2.getName() + " ,Highscore: " +
                    cn2.getHighscore();

            Log.i("Name: ", log);
        }
      return highscoreList;
    }

    public void addHighScore(View view) {
        String name;
        String highscore;
        editText = (EditText) findViewById(R.id.editTextPersonName);
        score = (TextView) findViewById(R.id.score);

        name = String.valueOf(editText.getText());
        highscore = String.valueOf(score.getText());
        DatabaseHandler db = new DatabaseHandler(this);
        int  userCount = db.getHighscoreCount();
        db.addHighscore(new HighScoreClass(name, Integer.parseInt(highscore)));

        Log.i("User count: ", String.valueOf(userCount));

        List<HighScoreClass> highscoreList = db.getAllHighscore();
highscoreList = db.top5Highscore();
List<String> highScoresStr;
        highScoresStr = new ArrayList<>();

        int j = 1;
        for (HighScoreClass cn2 : highscoreList) {


            String log = "Id: " + cn2.getID() + " ,Name: " + cn2.getName() + " ,Highscore: " +
                    cn2.getHighscore();


            highScoresStr.add(j++ + ":" +
                    cn2.getID() + "\t" +
                    cn2.getName() + "\t" +
                    cn2.getHighscore());
            //if(cn2.getHighscore() >= )
            Log.i("Score: ", log);

        }
        Log.i("divider", "==================");
        Log.i("divider","Scores in list<>>");
        for (String ss : highScoresStr) {
            Log.i("Score:", ss);
        }



topFiveFilter();

    }


    public void getHighScore(View view) {

        String name;
        String highscore;
        name = String.valueOf(editText.getText());
        highscore = String.valueOf(score.getText());
        DatabaseHandler db = new DatabaseHandler(this);
        int  userCount = db.getHighscoreCount();
        db.addHighscore(new HighScoreClass(name, Integer.parseInt(highscore)));

        Log.i("User count: ", String.valueOf(userCount));

        List<HighScoreClass> highscoreList = db.getAllHighscore();
        highscoreList = db.top5Highscore();
        List<String> highScoresStr;
        highScoresStr = new ArrayList<>();

        int j = 1;
        for (HighScoreClass cn2 : highscoreList) {


            String log = "Id: " + cn2.getID() + " ,Name: " + cn2.getName() + " ,Highscore: " +
                    cn2.getHighscore();


            highScoresStr.add(j++ + ":" +

                    cn2.getName() + "\t" +
                    cn2.getHighscore());
            Log.i("Score: ", log);

        }
        Log.i("divider", "==================");
        Log.i("divider","Scores in list<>>");
        for (String ss : highScoresStr) {
            Log.i("Score:", ss);
        }
        ArrayAdapter itemsAdapter =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, highScoresStr);
        lv.setAdapter(itemsAdapter);
    }



    public void nextGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
