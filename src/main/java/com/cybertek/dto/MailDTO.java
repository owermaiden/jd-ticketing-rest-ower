package com.cybertek.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MailDTO {

    private String emailTo;
    private String emailFrom;
    private String message;
    private String token;  // this is not jwt token...when we sent confirmation e-mail, if user clicks the url we gonna delete this token
    private String subject;
    private String url;    // when user click this link we will confirm this user..
}
