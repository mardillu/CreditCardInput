package com.mardillu.creditcardedittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import java.util.regex.Pattern;

/**
 * Created on 21/08/2020 at 10:11.
 *
 * @author Ezekiel Sebastine.
 */
public class CreditCardEditText extends AppCompatAutoCompleteTextView {

    String type = "UNKNOWN";
    CreditCardInputListener creditCardInputListener;
    int previousCharacterCount = 0;
    boolean proceed = true;

    public CreditCardEditText(Context context) {
        super(context);
        addMagic();
    }

    public CreditCardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addMagic();
    }

    public CreditCardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addMagic();
    }

    private void addMagic() {
        // Changing the icon when it's empty
        changeIcon();
        // Adding the TextWatcher
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int charCount = s.toString().replace("-", "").length();
                if (!proceed){
                    setSelection(s.length());
                    proceed = true;
                    return;
                }

                if (previousCharacterCount >= charCount){//is delete
                    previousCharacterCount = charCount;
                    return;
                }
                previousCharacterCount = charCount;
                formatAndSetText(s.toString().replace("-", ""));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!proceed){
                    proceed = true;
                    return;
                }
                changeIcon();
            }
        });
        // The input filters
        updateInputFilters(19);
    }

    void updateInputFilters(int maxLength){
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i) {
                    if (!Pattern.compile("[0-9\\-]*").matcher(String.valueOf(source)).matches()) {
                        return "";
                    }
                }
                return null;
            }
        };
        // Setting the filters
        setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(maxLength)});
    }

    private void changeIcon() {
        String s = getText().toString().replace("-", "").trim();
        if (s.startsWith("4") || s.matches(CardPattern.VISA)) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.vi, 0);
            type = "Visa";
        } else if (s.matches(CardPattern.MASTERCARD_SHORTER) || s.matches(CardPattern.MASTERCARD_SHORT) || s.matches(CardPattern.MASTERCARD)) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mc, 0);
            type = "MasterCard";
        } else if (s.matches(CardPattern.AMERICAN_EXPRESS)) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.am, 0);
            type = "American_Express";
        } else if (s.matches(CardPattern.DISCOVER_SHORT) || s.matches(CardPattern.DISCOVER)) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ds, 0);
            type = "Discover";
        } else if (s.matches(CardPattern.JCB_SHORT) || s.matches(CardPattern.JCB)) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.jcb, 0);
            type = "JCB";
        } else if (s.matches(CardPattern.DINERS_CLUB_SHORT) || s.matches(CardPattern.DINERS_CLUB)) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dc, 0);
            type = "Diners_Club";
        }else if (s.length() >= 4 && s.substring(0, 4).matches(CardPattern.REGX_VERVE_TYPE)) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.vv, 0);
            type = "Verve";
        } else {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.card, 0);
            type = "UNKNOWN";
        }

        if (type.equals("Verve")){
            updateInputFilters(23);
        }else {
            updateInputFilters(19);
        }

        if (creditCardInputListener != null){
            creditCardInputListener.onInput(s, type, isValid());
        }
    }

    public String getCardNumber() {
        return getText().toString().replace("-", "").trim();
    }

    public boolean isValid() {
        if (getCardNumber().matches(CardPattern.VISA_VALID)) return true;
        if (getCardNumber().matches(CardPattern.MASTERCARD_VALID)) return true;
        if (getCardNumber().matches(CardPattern.AMERICAN_EXPRESS_VALID)) return true;
        if (getCardNumber().matches(CardPattern.DISCOVER_VALID)) return true;
        if (getCardNumber().matches(CardPattern.DINERS_CLUB_VALID)) return true;
        if (getCardNumber().matches(CardPattern.JCB_VALID)) return true;
        if (getCardNumber().matches(CardPattern.REGX_VERVE)) return true;
        return false;
    }

    public String getCardType(){
        return type;
    }

    public void setCreditCardInputListener(CreditCardInputListener creditCardInputListener) {
        this.creditCardInputListener = creditCardInputListener;
    }

    public void removeCreditCardInputListener(){
        this.creditCardInputListener = null;
    }

    private void formatAndSetText(String s){
        proceed = false;
        if (s.length() < 4){
            setText(s);
            return;
        }
        if (s.length() == 4){
            setText(s + "-");
            return;
        }
        int charCount = s.length();
        StringBuilder builder = new StringBuilder("");
        if (type.equals("UNKNOWN") || type.equals("Visa") || type.equals("Discover") || type.equals("JCB") || type.equals("Verve")) {
//            if (charCount == 4 || charCount == 8 || charCount == 12) {
//                if (!s.toString().endsWith("-")) {
//                    append("-");
//                }
//            }

            for (int i = 0; i < charCount; i++) {
                if (i == 4 || i == 8 || i == 12 || i == 16){ //4th, 8th and 12th chars
                    builder.append("-");
                }
                builder.append(s.charAt(i));
            }
        } else if (type.equals("American_Express") || type.equals("Diners_Club")) {
//            if (charCount == 4 || charCount == 10) {
//                if (!s.toString().endsWith("-")) {
//                    append("-");
//                }
//            }

            for (int i = 0; i < charCount; i++) {
                if (i == 4 || i == 10){ //4th, 10th chars
                    builder.append("-");
                }
                builder.append(s.charAt(i));
            }
        }

        setText(builder.toString());
    }
}
