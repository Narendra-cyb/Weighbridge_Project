package com.weighbridge.controllers;

import com.weighbridge.payloads.VehicleRequest;
import com.weighbridge.payloads.VehicleResponse;
import com.weighbridge.repsitories.VehicleMasterRepository;
import com.weighbridge.services.VehicleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleMasterController {

    @Autowired
    private VehicleMasterService vehicleMasterService;

    @Autowired
    private VehicleMasterRepository vehicleMasterRepository;

    @PostMapping("/{transporterName}")
    public ResponseEntity<String> addVehicle(@RequestBody VehicleRequest vehicleRequest, @PathVariable String transporterName) {
        String response = vehicleMasterService.addVehicle(vehicleRequest, transporterName);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping()
    public ResponseEntity<List<VehicleResponse>> getAllVehicles(@RequestParam(defaultValue = "0", required = false) int page,
                                                                @RequestParam(defaultValue = "10", required = false) int size,
                                                                @RequestParam(required = false, defaultValue = "vehicleModifiedDate") String sortField,
                                                                @RequestParam(defaultValue = "desc", required = false) String sortOrder) {

        Pageable pageable;

        if (sortField != null && !sortField.isEmpty()) {
            Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortField);
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }

        Page<VehicleResponse> vehiclePage = vehicleMasterService.vehicles(pageable);
        List<VehicleResponse> vehicleLists = vehiclePage.getContent();
        return ResponseEntity.ok(vehicleLists);
    }

    @GetMapping("/{vehicleNo}")
    public ResponseEntity<VehicleResponse> getVehicleByVehicleNo(@PathVariable String vehicleNo) {
        VehicleResponse vehicleResponse = vehicleMasterService.vehicleByNo(vehicleNo);
        return ResponseEntity.ok(vehicleResponse);
    }

    @PutMapping("/update/{vehicleNo}")
    public ResponseEntity<String> updateVehicle(@PathVariable String vehicleNo, @RequestBody VehicleRequest vehicleRequest){
        String response = vehicleMasterService.updateVehicleByVehicleNo(vehicleNo, vehicleRequest);
        return ResponseEntity.ok(response);
    }




   @DeleteMapping("/delete/{vehicleNo}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String vehicleNo){
       String deletedVehicle = vehicleMasterService.deleteVehicleByVehicleNo(vehicleNo);
       return ResponseEntity.ok(deletedVehicle);
   }


}
