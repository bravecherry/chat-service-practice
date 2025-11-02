package org.example.fastcampus.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    CONSULTANT("ROLE_CONSULTANT"),
    ;

    private final String code;

    Role(String code) {
        this.code = code;
    }

    public static Role fromCode(String code) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getCode().equals(code))
                .findFirst().orElseThrow();
    }

}
