package contract.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgotPasswordInfo {
    private String emailAddress;
    private String firstName;
    private String passwordResetUrl;
}
