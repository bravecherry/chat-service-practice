package org.example.fastcampus.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.example.fastcampus.entities.Member;
import org.example.fastcampus.enums.Gender;
import org.example.fastcampus.enums.Role;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class MemberFactory {
    public static Member create(OAuth2UserRequest request, OAuth2User oAuth2User) {
        String registrationId = request.getClientRegistration().getRegistrationId();
        return switch (registrationId) {
            case "kakao" ->  {
                Map<String, Object> attributes = oAuth2User.getAttribute("kakao_account");
                yield Member.builder()
                        .email((String) attributes.get("email"))
                        .nickName((String) ((Map) attributes.get("profile")).get("nickName"))
                        .name((String) attributes.get("name"))
                        .phoneNumber((String) attributes.get("phone_number"))
                        .gender(Gender.valueOf(((String) attributes.get("gender")).toUpperCase()))
                        .birthday(getBirthDay(attributes))
                        .role(Role.USER.getCode())
                        .build();
            }
            case "google" -> {
                Map<String, Object> attributes = oAuth2User.getAttributes();
                yield Member.builder()
                        .email((String) attributes.get("email"))
                        .nickName((String) attributes.get("given_name"))
                        .name((String) attributes.get("name"))
                        .role(Role.USER.getCode())
                        .build();
            }
            default -> throw new IllegalArgumentException("not supported service : " + registrationId);
        };
    }

    private static LocalDate getBirthDay(Map<String, Object> attributes) {
        String birthYear = (String) attributes.get("birthyear");
        String birthDay = (String) attributes.get("birthday");
        return LocalDate.parse(birthYear + birthDay, DateTimeFormatter.BASIC_ISO_DATE);
    }

}
