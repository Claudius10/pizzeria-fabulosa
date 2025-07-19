package org.clau.pizzeriauserresourceserver.service;

public interface UserService {

   void deleteById(Long userId, String password);

   void passwordMatches(Long userId, String password);
}