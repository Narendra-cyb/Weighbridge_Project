package com.weighbridge.services.impls;

import com.weighbridge.entities.TransporterMaster;
import com.weighbridge.payloads.TransporterRequest;
import com.weighbridge.repsitories.TransporterMasterRepository;
import com.weighbridge.services.TransporterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransporterServiceImpl implements TransporterService {
    @Autowired
    private TransporterMasterRepository transporterMasterRepository;
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public String addTransporter(TransporterRequest transporterRequest) {
        Boolean ByTransporterMaster = transporterMasterRepository.existsByTransporterName(transporterRequest.getTransporterName());
        if (ByTransporterMaster){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Transporter already exist");
        }
        else {
            HttpSession session = request.getSession();
            String userId;
            if (session != null && session.getAttribute("userId") != null) {
                userId = session.getAttribute("userId").toString();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session Expired, Login again !");
            }
            TransporterMaster transporterMaster=new TransporterMaster();
            transporterMaster.setTransporterName(transporterRequest.getTransporterName());
            transporterMaster.setTransporterAddress(transporterRequest.getTransporterAddress());
            transporterMaster.setTransporterContactNo(transporterRequest.getTransporterContactNo());
            transporterMaster.setTransporterEmailId(transporterRequest.getTransporterEmailId());

            LocalDateTime currentDateTime = LocalDateTime.now();

            transporterMaster.setTransporterCreatedBy(String.valueOf(userId));
            transporterMaster.setTransporterCreatedDate(currentDateTime);
            transporterMaster.setTransporterModifiedBy(String.valueOf(userId));
            transporterMaster.setTransporterModifiedDate(currentDateTime);


            transporterMasterRepository.save(transporterMaster);
            return "Transporter added successfully";

        }

    }

    @Override
    public List<String> getAllTransporter() {
        List<TransporterMaster> all = transporterMasterRepository.findAll();
        List<String> str=new ArrayList<>();
        for(TransporterMaster transporterMaster:all){
           str.add(transporterMaster.getTransporterName());
        }
        return str;
    }
}
