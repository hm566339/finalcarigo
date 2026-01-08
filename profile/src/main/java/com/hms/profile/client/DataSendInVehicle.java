package com.hms.profile.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "vehicle", url = "http://vehicle:8083", configuration = FeignClient.class)
public interface DataSendInVehicle {

}
