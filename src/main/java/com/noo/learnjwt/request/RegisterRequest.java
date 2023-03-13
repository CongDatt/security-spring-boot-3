package com.noo.learnjwt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
