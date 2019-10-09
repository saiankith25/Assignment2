package fr.isep.ii3510.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GridLayout keyoard,word;
    private static TextView guess_counts,remaining_attempts,word_length;
    private static String random_word="";
    private ArrayList<String> guess_letter = new ArrayList<String>();
    private int guess_count=0,reamining_attempt=10;
    private ArrayList<String> word_list = new ArrayList<String>();
    private ArrayList<String> match_letter = new ArrayList<String>();
    private static String matched_string="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String array[] = getResources().getStringArray(R.array.alphabets);
        guess_counts= (TextView) findViewById(R.id.guess_count);
        remaining_attempts= (TextView) findViewById(R.id.remaining_count);
        word_length= (TextView) findViewById(R.id.word_length);

        keyoard = (GridLayout) findViewById(R.id.keyboard);
        word = (GridLayout) findViewById(R.id.word);
        int numOfCol = keyoard.getColumnCount();
        int numOfRow = keyoard.getRowCount();
        int pWidth = keyoard.getWidth();
        int pHeight = keyoard.getHeight();
        int w = pWidth/numOfCol;
        int h = pHeight/numOfRow;
        int counter=0;
        for(int yPos=0; yPos<numOfRow; yPos++) {
            for (int xPos = 0; xPos < numOfCol; xPos++) {
                if(counter<26){
                    Log.d("Ankith-logger","COLUMN->"+xPos+", ROW->"+yPos);
                keyoard.addView(addKeys(w,h,yPos,xPos,array[counter]));
                counter++;}
            }
        }
        getWords();
        word_length.setText("Word Length / Size : "+random_word.length());
        addTextView();
    }


    private Button addKeys(int w, int h,int row,int column,String alphabet){
        final Button item = new Button(this);
        item.setText(alphabet);
        item.setMinWidth(44);
        GridLayout gridLayout = new GridLayout(this);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.height = GridLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.width = w - 2*2;
        layoutParams.height = h - 2*2;
        gridLayout.setLayoutParams(layoutParams);

        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row);
        item.setLayoutParams( layoutParams );
        item.setTag("bt"+alphabet);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(evaluate(item)){
                    guess_count+=1;
                    guess_counts.setText("Guess Count : "+guess_count);
                    item.setBackgroundColor(Color.parseColor("#228B22"));
                    item.setEnabled(false);

                }
                else{
                    guess_count+=1;
                    reamining_attempt=reamining_attempt-1;
                    guess_counts.setText("Guess Count : "+guess_count);
                    remaining_attempts.setText("Remaining Attempts : "+reamining_attempt);
                    item.setBackgroundColor(Color.parseColor("#F51B00"));
                    item.setEnabled(false);
                    if(reamining_attempt==0){
                        Log.d("Akith_finish","Completed Attempts");
                        Intent i = new Intent(getApplicationContext(),GameOverActivity.class);
                        i.putExtra("word", random_word);
                        startActivity(i);
                    }
                }

            }
        });
        return item;
    }

    public void addTextView(){
        int numOfCol1 = word.getColumnCount();
        int numOfRow1 = word.getRowCount();
        int pWidth1 = word.getWidth();
        int pHeight1 = word.getHeight();

        int w1 = pWidth1/numOfCol1;
        int h1 = pHeight1/numOfRow1;
        char rand_word[] = random_word.toCharArray();
        word_list = new ArrayList<String>();
        for(int i =0;i<rand_word.length;i++){
            word_list.add(""+rand_word[i]);
        }
        int counter1=0;
        matched_string = "";
        for(int yPos=0; yPos<numOfRow1; yPos++) {
            for (int xPos = 0; xPos < numOfCol1; xPos++) {
                if(counter1<word_list.size()){
                    Log.d("Ankith-logger","COLUMN->"+xPos+", ROW->"+yPos);
                    word.addView(addWordtoLayout(w1,h1,yPos,xPos,word_list.get(counter1)));
                    counter1++;}
            }
        }

    }

    public boolean evaluate(Button item){
        Log.d("Ankith_button","-->"+item.getText().toString());


        word.removeAllViews();
        if(word_list.contains(item.getText().toString())) {
            guess_letter.add(item.getText().toString());
            addTextView();

            Log.d("Ankith_final","Word_list: "+word_list+word_list.size()+" Match Size: "+matched_string+matched_string.length());

            return true;
        }
        else{
            addTextView();
            return false;}
    }

    public void getWords(){
        ArrayList<String> words = new ArrayList<String>();
        BufferedReader reader;
        try{
            final InputStream file = getAssets().open("words.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while(line != null){
                Log.d("StackOverflow", line);
                words.add(line.toUpperCase());
                line = reader.readLine();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        Log.d("Ankith-Words",""+words);
        Random r = new Random();
        int random_number =r.nextInt((words.size() - 1) + 0) + 0;
        random_word = words
                .get(random_number);
        Log.d("Ankith-Words",""+random_word);
    }

    public TextView addWordtoLayout(int w, int h,int row,int column,String word){

        TextView letter = new TextView(this);
        String s = ""+word;

        if(guess_letter.size()>0) {
            for (String g_letter : guess_letter) {
                if (s.equalsIgnoreCase(g_letter)) {
                    letter.setText(s);
                    match_letter.add(s);
                    matched_string=matched_string+s;

                }

            }
        }
        letter.setBackgroundResource(R.drawable.background);
        letter.setTextSize(30);
        letter.setPadding(0,0,0,0);

        GridLayout gridLayout = new GridLayout(this);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.height = GridLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
        layoutParams.setMargins(10, 20, 10, 10);
        layoutParams.width = w - 2*4;
        layoutParams.height = h - 2*4;
        gridLayout.setLayoutParams(layoutParams);

        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row);
        letter.setLayoutParams( layoutParams );
        letter.setTag("bt"+s);
        if(word_list.size()==matched_string.length()){
            Log.d("Ankith_final","Game Finished  "+word_list.size()+"--"+matched_string.length());

            Intent i = new Intent(getApplicationContext(),CongratulateActivity.class);
            i.putExtra("word", random_word);
            startActivity(i);
        }
        return letter;
    }


}
