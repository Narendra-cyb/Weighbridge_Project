package com.weighbridge.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class VehicleResponse {
    private String vehicleNo;
    private Set<String> transporter;
    private String vehicleType;
    private String vehicleManufacturer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fitnessUpto;
}
