package com.universe.dto.user;

import com.universe.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class CreateUserForm extends BaseUserForm {

    @ToString.Exclude
    @NotBlank
    @Size(min = 3, max = 16, message = "Password length should be between 3 and 16 symbols.")
    private String password;

    public UserEntity toUserEntity() {
        UserEntity user = new UserEntity();
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setEmail(this.getEmail());
        return user;
    }
}
