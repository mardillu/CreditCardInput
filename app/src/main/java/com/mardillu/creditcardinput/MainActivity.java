package com.mardillu.creditcardinput;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mardillu.creditcardedittext.CreditCardEditText;
import com.mardillu.creditcardedittext.CreditCardInputListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    CreditCardEditText creditCardEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        creditCardEditText = findViewById(R.id.input_card);

        creditCardEditText.setCreditCardInputListener(new CreditCardInputListener() {
            @Override
            public void onInput(String cardNumber, String cardType, boolean isValid) {
                Log.d(TAG, "onInput: " + cardNumber + cardType + isValid);
            }
        });

    }
}
