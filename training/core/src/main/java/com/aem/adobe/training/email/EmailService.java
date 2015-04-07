
package com.aem.adobe.training.email;

import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import aQute.bnd.annotation.ProviderType;

/**
 * A service interface for sending a generic template based Email Notification.
 * 
 */
@ProviderType
public interface EmailService {

    /**
     * Construct an email based on a template and send it to one or more
     * recipients.
     * 
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param recipients recipient email addresses
     * 
     * @return failureList containing list recipient's InternetAddresses for which email sent failed
     */
    List<InternetAddress> sendEmail(String templatePath, Map<String, String> emailParams,
        InternetAddress... recipients);

    /**
     * Construct an email based on a template and send it to one or more
     * recipients.
     * 
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param recipients recipient email addresses. Invalid email addresses are skipped.
     * 
     * @return failureList containing list recipient's String addresses for which email sent failed
     */
    List<String> sendEmail(String templatePath, Map<String, String> emailParams, String... recipients);
}
