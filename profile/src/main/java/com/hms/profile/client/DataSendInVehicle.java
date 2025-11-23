package com.hms.profile.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "vehicle", url = "http://localhost:8083")
public interface DataSendInVehicle {

}
