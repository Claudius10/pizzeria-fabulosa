package org.clau.pizzeriaadminresourceserver.service;

import java.util.List;

public interface AdminOrderService {

   List<Integer> findCountForTimelineAndState(String timeline, String state);
}
