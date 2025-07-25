package org.clau.pizzeriapublicresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriapublicresourceserver.controller.swagger.UtilControllerSwagger;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.util.common.TimeUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.RESOURCE + Route.UTIL_BASE)
public class UtilController implements UtilControllerSwagger {

   @GetMapping(Route.LOCAL_DATE_TIME_NOW)
   public ResponseEntity<LocalDateTime> getNowAccountingDST() {
	  return ResponseEntity.ok(TimeUtils.getNowAccountingDST());
   }
}
