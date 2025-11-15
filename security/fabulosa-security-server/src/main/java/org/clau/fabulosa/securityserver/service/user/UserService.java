package org.clau.fabulosa.securityserver.service.user;

public interface UserService {

   void deleteById(Long userId, String password);

   void passwordMatches(Long userId, String password);
}