package com.mardillu.creditcardedittext;

/**
 * Created on 21/08/2020 at 10:31.
 *
 * @author Ezekiel Sebastine.
 */
public interface CreditCardInputListener {
    void onInput(String cardNumber, String cardType, boolean isValid);
}
