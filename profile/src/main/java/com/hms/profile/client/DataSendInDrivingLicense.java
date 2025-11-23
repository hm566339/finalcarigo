package com.hms.profile.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "drivinglicense", url = "http://localhost:8082")
public interface DataSendInDrivingLicense {

}
