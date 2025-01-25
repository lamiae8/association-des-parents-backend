package com.ili.service;


import com.ili.dto.Adulte;
import com.ili.dto.Commande;
import com.ili.dto.Evenement;
import com.ili.model.AdulteEntity;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

@ApplicationScoped


public class MailService {

    private static final String EMAIL_SUBJECT = "Reset Password";
    private static final String LINK = "https://a71b-2001-861-3507-5030-3dea-ef7b-f66c-5bd3.ngrok-free.app/reset-password/";




    private String EMAIL_TEXT;

    private String EMAIL_FROM;
    private String PASSWORD;
    private String SMTP_HOST;
    private String SMTP_PORT;
    private Properties props;


    public MailService() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("/mail.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);
            EMAIL_FROM = prop.getProperty("email");
            PASSWORD = prop.getProperty("password");
            SMTP_HOST = prop.getProperty("smtp.host");
            SMTP_PORT = prop.getProperty("smtp.port");
            props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void sendPasswordResetMail(AdulteEntity adulte, String token)  throws MessagingException {
        EMAIL_TEXT="Afin de reinitialiser votre mot de passe, veiuillez suivre le lien: <a href=\"" + LINK +token+ "\">Reset Password</a>";

    
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, PASSWORD);
            }
        });
    
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(EMAIL_FROM));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adulte.getMail()));
        msg.setSubject(EMAIL_SUBJECT);
        msg.setContent(EMAIL_TEXT, "text/html");
    
        Transport.send(msg);
    }


    public void sendReminderEmail(Commande commande, LocalDate DATE_LIMIT)   throws MessagingException {



        EMAIL_TEXT="Votre commande:   ::" + commande.getId() + "::      n'a pas encore été payé.</br> Veuillez penser à la payer au plutard le   "+DATE_LIMIT.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH))+
        "<br> Merci de votre compréhension <br> Cordialement, <br> L'équipe de l'association des etudiants";


        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, PASSWORD);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(EMAIL_FROM));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(commande.getAdulte().getMail()));
        msg.setSubject("Rappel de délai de paiement de commande");

        msg.setContent(EMAIL_TEXT, "text/html");

        Transport.send(msg);
    }



}