package contract.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WelcomeInfo {

    private String emailAddress;
    private String firstName;

}
