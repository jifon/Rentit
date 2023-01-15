package com.neobis.rentit.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
  private String token;
  private String refreshToken;
  private String type = "Bearer";
  private Long id;
  private String username;
  private String email;

  private String phoneNumber;

  private List<String> roles;

  public JwtResponse(String accessToken, String refreshToken,Long id, String username, String email,String phoneNumber, List<String> roles) {
    this.token = accessToken;
    this.refreshToken = refreshToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.roles = roles;
  }


}
