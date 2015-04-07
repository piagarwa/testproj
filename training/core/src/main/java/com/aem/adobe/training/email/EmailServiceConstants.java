package com.aem.adobe.training.email;

import aQute.bnd.annotation.ProviderType;

/**
 * Defines special keys for the replacement variable map
 * passed to EmailService.sendEmail().
 */
@ProviderType
public final class EmailServiceConstants {

    private EmailServiceConstants() {

    }

    /**
     * Sender Email Address variable passed in the input parameter
     * map to the sendEmail() function.
     */
    public static final String SENDER_EMAIL_ADDRESS = "senderEmailAddress";

    /**
     * Sender Name variable passed in the input parameter
     * map to the sendEmail() function.
     */
    public static final String SENDER_NAME = "senderName";
}
