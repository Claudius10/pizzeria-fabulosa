package org.clau.pizzeriapublicresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriapublicresourceserver.controller.swagger.UtilControllerSwagger;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.util.TimeUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.RESOURCE + ApiRoutes.UTIL_BASE)
public class UtilController implements UtilControllerSwagger {

   @GetMapping(ApiRoutes.LOCAL_DATE_TIME_NOW)
   public ResponseEntity<LocalDateTime> getNowAccountingDST() {
	  return ResponseEntity.ok(TimeUtils.getNowAccountingDST());
   }
}
