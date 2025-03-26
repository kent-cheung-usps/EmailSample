package com.gundam;

import java.util.Properties;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

/**
 * Hello world!
 */
public class EmailSample {
    public static void main(String[] args) {
    	System.out.println("Hello World!");
        String pop3Host = "pop.gmx.com";
        String mailStoreType = "pop3";
        String username = "amiga.maria@gmx.com";
        String password = "DummyDummyCh@rger";

        try {
            // Create properties object
            Properties properties = new Properties();
            properties.put("mail.pop3.host", pop3Host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.ssl.enable", "true");

            // Get the session object
            Session emailSession = Session.getDefaultInstance(properties);

            // Create the POP3 store object and connect with the server
            Store store = emailSession.getStore(mailStoreType);
            store.connect(pop3Host, username, password);

            // Create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // Get message count and retrieve the latest email
            int messageCount = emailFolder.getMessageCount();
            if (messageCount > 0) {
                Message latestMessage = emailFolder.getMessage(messageCount);
                
                // Print email details
                System.out.println("Subject: " + latestMessage.getSubject());
                System.out.println("From: " + latestMessage.getFrom()[0]);
                System.out.println("Date: " + latestMessage.getSentDate());
                System.out.println("Content: " + latestMessage.getContent().toString());
            } else {
                System.out.println("No emails found in inbox");
            }

            // Close the folder and store objects
            emailFolder.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
