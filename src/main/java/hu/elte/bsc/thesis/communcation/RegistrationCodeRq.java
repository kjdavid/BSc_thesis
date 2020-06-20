package hu.elte.bsc.thesis.communcation;

import hu.elte.bsc.thesis.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCodeRq {
    private User.Role role;
    private Long companyId;
}
