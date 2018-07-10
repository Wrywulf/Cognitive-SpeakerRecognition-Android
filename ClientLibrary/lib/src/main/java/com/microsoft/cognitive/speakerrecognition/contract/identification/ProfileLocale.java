package com.microsoft.cognitive.speakerrecognition.contract.identification;

public class ProfileLocale {

    public ProfileLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Ie. "en-us"
     */
    public final String locale;
}
