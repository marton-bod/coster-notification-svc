package notificationsvc.controllers;

import contract.domain.ForgotPasswordInfo;
import contract.domain.WelcomeInfo;
import lombok.RequiredArgsConstructor;
import notificationsvc.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService emailSender;

    @PostMapping("/postregister")
    public ResponseEntity sendWelcomeMessage(@RequestBody WelcomeInfo welcomeInfo) {
        emailSender.sendPostRegisterationEmail(welcomeInfo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgotpwd")
    public ResponseEntity sendForgotPasswordLink(@RequestBody ForgotPasswordInfo welcomeInfo) {
        emailSender.sendForgotPasswordEmail(welcomeInfo);
        return ResponseEntity.ok().build();
    }

}
