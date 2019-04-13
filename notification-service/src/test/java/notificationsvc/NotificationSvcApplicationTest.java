package notificationsvc;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import contract.domain.ForgotPasswordInfo;
import contract.domain.WelcomeInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
public class NotificationSvcApplicationTest {

    @LocalServerPort int port;
    @Value("${spring.mail.username}") String username;
    @Value("${spring.mail.password}") String password;

    private GreenMail testSmtpServer;
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setUp() {
        testSmtpServer = new GreenMail(ServerSetup.SMTP);
        testSmtpServer.setUser(username, password);
        testSmtpServer.start();
    }

    @After
    public void tearDown() {
        testSmtpServer.stop();
    }

    @Test
    public void shouldSendPostRegistrationMail() throws MessagingException {
        WelcomeInfo welcomeInfo = new WelcomeInfo();
        welcomeInfo.setFirstName("Jacob");
        welcomeInfo.setEmailAddress("jj@google.com");

        restTemplate.postForEntity(String.format("http://localhost:%d/notification/postregister", port),
                welcomeInfo, ResponseEntity.class);

        MimeMessage[] receivedMessages = testSmtpServer.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        MimeMessage current = receivedMessages[0];

        assertEquals("Welcome to Coster.io!", current.getSubject());
        assertEquals("jj@google.com", current.getAllRecipients()[0].toString());
    }

    @Test
    public void shouldSendForgotPasswordMail() throws MessagingException, IOException {
        ForgotPasswordInfo forgotPasswordInfo = new ForgotPasswordInfo();
        forgotPasswordInfo.setFirstName("Jacob");
        forgotPasswordInfo.setEmailAddress("jj@google.com");
        forgotPasswordInfo.setPasswordResetUrl("URL-TEST");

        restTemplate.postForEntity(String.format("http://localhost:%d/notification/forgotpwd", port),
                forgotPasswordInfo, ResponseEntity.class);

        MimeMessage[] receivedMessages = testSmtpServer.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        MimeMessage current = receivedMessages[0];

        assertEquals("Your password reset link for Coster.io", current.getSubject());
        assertEquals("jj@google.com", current.getAllRecipients()[0].toString());
    }

}