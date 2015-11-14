package com.fluidcoding.brian.elizabud;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class Eliza extends AppCompatActivity {
    private Button btnSpeak;
    private TextView txtElizaTyping;
    private EditText chatOut;
    private EditText chatIn;
    private HashMap<String, String[]> responses;
    private String[] keywords;
    public String response;
    private int speakTime = 2000;
    private Handler humanDelay;

    // Android Developer Docs Reference pages


    // http://developer.android.com/reference/android/os/Handler.html
    // http://developer.android.com/reference/android/view/inputmethod/InputMethodManager.html
    // http://developer.android.com/reference/android/widget/EditText.html
    // http://developer.android.com/reference/android/R.styleable.html#EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliza);
        buildResponses();       // Build the hashmap of responses once
        // References to UI Elements
        txtElizaTyping = (TextView)findViewById(R.id.txtElizaTyping);
        txtElizaTyping.setVisibility(View.INVISIBLE);
        btnSpeak = (Button)findViewById(R.id.buttonTalk);
        chatOut = (EditText)findViewById(R.id.editChatWindow);
        chatIn = (EditText)findViewById(R.id.editTextMessage);
        chatOut.setTextColor(Color.BLACK);

        // Handler to use to delay eliza's response
        humanDelay = new Handler();

        // Sets Event that's triggered on keypressed of the softkeyboard
        // If the editor action is of id IME_ACTION_SEND then call speak
        chatIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    speak(btnSpeak);
                }
                return false;
            }
        });
    }

    /**
     * Instantiate responses hashmap load it with responses
     * Pull keywords from responses key set to string array
     */
    public void buildResponses(){
        responses = new HashMap<>();
        String[] temp0 = {"What does that suggest to you?",
                "I see.",
                "I'm not sure I understand you fully.",
                "Can you elaborate?",
                "That is quite interesting."
        };
        responses.put("NOTFOUND", temp0);

        String[] temp1 = {"Can you think of a specific example?"};
        responses.put("always", temp1);

        String[] temp2 = {"Is that the real reason?"};
        responses.put("because", temp2);

        String[] temp3 = {"Please don't apologize."};
        responses.put("sorry", temp3);

        String[] temp4 = {"You don't seem very certain."};
        responses.put("maybe", temp4);

        String[] temp5 = {"Do you really think so?"};
        responses.put("i think", temp5);

        String[] temp6 = {"We were discussing you, not me."};
        responses.put("you", temp6);

        String[] temp7 = {"Why do you think so?",
                "You seem quite positive."};
        responses.put("yes", temp7);

        String[] temp8 = {"Why not?",
                "Are you sure?"};
        responses.put("no", temp8);

        String[] temp9 = {"I am sorry to hear you are *.",
                "How long have you been *?",
                "Do you believe it is normal to be *?",
                "Do you enjoy being *?"
        };
        responses.put("i am", temp9);
        responses.put("i'm", temp9);

        String[] temp10 = {"Tell me more about such feelings.",
                "Do you often feel *?",
                "Do you enjoy feeling *?",
                "Why do you feel that way?"
        };
        responses.put("i feel", temp10);

        String[] temp11 = {"Tell me more about your family.",
                "How do you get along with your family?",
                "Is your family important to you?"
        };
        responses.put("family", temp11);
        responses.put("mother", temp11);
        responses.put("father", temp11);
        responses.put("mom", temp11);
        responses.put("dad", temp11);
        responses.put("sister", temp11);
        responses.put("brother", temp11);
        responses.put("husband", temp11);
        responses.put("wife", temp11);

        String[] temp12 = {"What does that dream suggest to you?",
                "Do you dream often?",
                "What persons appear in your dreams?",
                "Are you disturbed by your dreams?"
        };
        responses.put("dream", temp12);
        responses.put("nightmare", temp12);

        String[] tempHello = {"Hello", "How are you doing?",
                "Hi, hope your day is going well!",
                "Hey, do i know you?",
                "Please stop talking to me...",
                "What's on your mind?"};

        responses.put("hi", tempHello);
        responses.put("sup", tempHello);
        responses.put("hey", tempHello);
        responses.put("hello", tempHello);
        responses.put("sup", tempHello);

        String[] tempNoHope = {"YES YOU CAN!",
                "JUST DO IT!",
                "Don't let your dreams be dreams...",
                "Yesterday you said tomorrow.",
                "Make your dreams come true.",
                "Nothing is impossible",
                "What are you waiting for?"
        };
        // Shia pls
        responses.put("i can't", tempNoHope);
        responses.put("i cant", tempNoHope);
        responses.put("i want", tempNoHope);
        responses.put("tomorrow", tempNoHope);

        String[] tempAnswerName = {"Eliza."};
        responses.put("what's your name", tempAnswerName);
        responses.put("who are you", tempAnswerName);

        // Get current date (restricted to onCreate call time)
        Calendar today = new GregorianCalendar();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String[] time = {df.format(today.getTime())};

        responses.put("what's today's date", time);
        responses.put("what's the date", time);

        String[] tempWritten = {"i'm written in java!",
                                  "god made me"};
        responses.put("what are you", tempWritten);

        // Get list of keywords from hashmap dynamically
        keywords = responses.keySet().toArray(new String[responses.size()]);
    }

    /**
     * Checks for a valid string of input
     * @param s user's message text
     * @return false if empty maybe add other invalid types of input later
     */
    public boolean isValid(String s){
        if(s.length()==0)
            return false;
        return true;
    }

    /**
     * Called via Speak onClick
     * Analyze user input and respond
     * @param v reference to the speak button
     */
    public void speak(View v){
        // Hide keyboard with system service
        InputMethodManager keybHide =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        keybHide.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);

        // Get response
        String ss = chatIn.getText().toString();
        ss = ss.toLowerCase();
        Scanner s = new Scanner(ss);

        // Make sure user input is not empty
        if(isValid(ss)) {
        /* initialize variables */
            response = "";
            String[] response_array = {""};
            boolean found = false;
            String currentkeyword = "";

        /* Loop through keywords */
            for (int i = 0; i < keywords.length; i++) {
                if ((s.findInLine(currentkeyword = (String) keywords[i]) != null)
                        && (responses.get(currentkeyword) != null)) {
                /*If a keyword is found in the current input, get a response
                  from HashMap and return it*/
                    found = true;
                    response_array = responses.get(currentkeyword);
                    response = response_array[(int) ((response_array.length - 1) *
                            Math.random())];
                /* If response has a *, replace it with the remainder of
                   input string _with the last character removed if it is
                   a punctuation character_ */
                    if (response.indexOf('*') != -1) {
                        String remaining_input;
                        if (s.hasNext() &&
                                (remaining_input = s.nextLine().trim()) != null) {
                            response = response.substring(0, response.indexOf('*')) +
                                    remaining_input
                                            .substring(0, remaining_input.length() - 1)
                                    + remaining_input
                                    .substring(remaining_input.length() - 1,
                                            remaining_input.length())
                                    .replaceAll("[^A-Za-z]", "") +
                                    response.substring(response.indexOf('*') + 1,
                                            response.length());
                            response = response.trim();
                        } else
                            //matches a single asterisk
                            response = response.replaceAll("[*]", "");
                    }
                }
            }

        /*respond with a default message if no keywords were found in the
          input string */
            if (!found) {
                response_array = (String[]) responses.get("NOTFOUND");
                response = response_array[(int) ((response_array.length - 1) *
                        Math.random())];
            }
            // Add user message to chat window and clear old text
            chatIn.setText("");
            chatOut.append("You: " + ss + "\n");

            // Disable chat while eliza is typing
            btnSpeak.setEnabled(false);

            // Wait to add Eliza's message...cuz turing
            txtElizaTyping.setVisibility(View.VISIBLE);

            // Do stuff later
            humanDelay.postDelayed(new DelayedMessage(), speakTime);
        }
        else{// Error Toast no input text
            Toast.makeText(this, "Please enter some text first.",
                    Toast.LENGTH_LONG).show();
        }
    }/*- end respond() - */

    /**
     *  A Runnable object that will execute after a delay
     *  Adds Eliza's response after time
     */
    public class DelayedMessage implements Runnable{
        @Override
        public void run() {
            // Hide "Eliza is Typing" textview
            txtElizaTyping.setVisibility(View.INVISIBLE);
            // Add Eliza's message
            chatOut.append(("Eliza: " + response + "\n"));
            // Re enable speak to eliza button
            btnSpeak.setEnabled(true);
        }
    }
    }

