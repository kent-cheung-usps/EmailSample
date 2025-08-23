package com.gundam;

import java.io.InputStream;
import java.util.Properties;

import com.gundam.util.FieldEncryptor;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMultipart;

/**
 * Example email retriever with encrypted password support.
 */
public class EmailSample {
    private static String EMAIL;
    private static String PASSWORD;
    private static String POP3_SERVER;
    private static String IMAP_SERVER;

    static {
        try (InputStream input = EmailSample.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
            } else {
                prop.load(input);
                EMAIL = prop.getProperty("email");
                String passwordRaw = prop.getProperty("password");
                if (passwordRaw != null && passwordRaw.startsWith("{enc}")) {
                    try {
                        PASSWORD = FieldEncryptor.decrypt(passwordRaw.substring(5));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to decrypt password", e);
                    }
                } else {
                    PASSWORD = passwordRaw;
                }
                POP3_SERVER = prop.getProperty("pop3.server");
                IMAP_SERVER = prop.getProperty("imap.server");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
                System.out.println("Content: " + getTextFromMessage(latestMessage));
            } else {
                System.out.println("No emails found.");
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return "Unsupported content type";
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            var bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent().toString());
            } else if (bodyPart.isMimeType("text/html")) {
                String html = bodyPart.getContent().toString();
                // You can use a library like jsoup to parse HTML if needed
                result.append(html);
            }
        }
        return result.toString();
    }
}