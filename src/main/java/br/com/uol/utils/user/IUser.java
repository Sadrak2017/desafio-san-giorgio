package br.com.uol.utils.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface IUser extends UserDetails {
  long getId();
  boolean hasPatterns();

  boolean hasNrAccess();

  boolean isChangePassword();
}
