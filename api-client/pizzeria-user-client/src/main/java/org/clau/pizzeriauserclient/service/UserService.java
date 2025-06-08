package org.clau.pizzeriauserclient.service;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserService {

	Mono<Map<String, Object>> getInfo(OAuth2AuthorizedClient authorizedClient);

	Mono<Object> deleteById(Long userId, OAuth2AuthorizedClient authorizedClient);
}
