package com.carigo.drivinglicense.service;

import java.util.Map;

public interface DlExternalClient {

    Map<String, Object> fetchByDlNumber(String dlNumber);
}
