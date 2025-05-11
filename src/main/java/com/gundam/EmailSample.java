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
    private static final String EMAIL = "amiga.maria@gmx.com";
    private static final String PASSWORD = "DummyDummyCh@rger";
    private static final String POP3_SERVER = "pop.gmx.com";
    private static final String IMAP_SERVER = "imap.gmx.com";

    public static void main(String[] args) {
    	System.out.println("Hello World");
        if (args.length != 1) {
            System.out.println("Usage: java EmailRetriever <POP/IMAP>");
            return;
        }

        String protocol = args[0].trim().toUpperCase();

        if (protocol.equals("POP")) {
            retrieveLatestEmail("pop3", POP3_SERVER);
        } else if (protocol.equals("IMAP")) {
            retrieveLatestEmail("imap", IMAP_SERVER);
        } else {
            System.out.println("Invalid choice. Please choose POP or IMAP.");
        }
    }

    private static void retrieveLatestEmail(String protocol, String host) {
        try {
        	System.out.println("Selected Host Protocol: " + host + ":" + protocol);
            Properties properties = new Properties();
            properties.put("mail.store.protocol", protocol);
            properties.put("mail." + protocol + ".host", host);
            properties.put("mail." + protocol + ".port", protocol.equals("pop3") ? "995" : "993");
            properties.put("mail." + protocol + ".ssl.enable", "true");
            properties.put("mail.debug", "false"); // Enable debugging

            Session session = Session.getInstance(properties);
            Store store = session.getStore(protocol);
            
            System.out.println("Connecting ...");
            store.connect(host, EMAIL, PASSWORD);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            System.out.println("Total messages: " + messages.length);
            
            if (messages.length > 0) {
                Message latestMessage = messages[messages.length - 1];
                System.out.println("Latest Email Subject: " + latestMessage.getSubject());
                System.out.println("From: " + latestMessage.getFrom()[0]);
                System.out.println("Date: " + latestMessage.getSentDate());
                System.out.println("Content: " + latestMessage.getContent().toString());

            } else {
                System.out.println("No emails found.");
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
