package org.clau.pizzeriauserclient.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriauserclient.dao.ErrorRepository;
import org.clau.pizzeriauserclient.service.ErrorService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class ErrorServiceImpl implements ErrorService {

	private final ErrorRepository errorRepository;

	@Override
	public APIError create(String cause, String message, String origin, String uriPath, boolean fatal) {

		APIError error = APIError.builder()
				.withCreatedOn(TimeUtils.getNowAccountingDST())
				.withCause(cause)
				.withMessage(message)
				.withOrigin(origin)
				.withPath(uriPath)
				.withLogged(true)
				.withFatal(fatal)
				.build();

		return errorRepository.save(error);
	}
}
