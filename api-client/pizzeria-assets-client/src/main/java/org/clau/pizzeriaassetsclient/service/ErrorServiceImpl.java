package org.clau.pizzeriaassetsclient.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.service.ErrorService;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriaassetsclient.dao.ErrorRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class ErrorServiceImpl implements ErrorService {

	private final ErrorRepository errorRepository;

	@Override
	public APIError create(String cause, String message, String origin, String uriPath, boolean fatal) {

		APIError error = APIError.builder()
				.createdOn(TimeUtils.getNowAccountingDST())
				.cause(cause)
				.message(message)
				.origin(origin)
				.path(uriPath)
				.logged(true)
				.fatal(fatal)
				.build();

		return errorRepository.save(error);
	}
}
