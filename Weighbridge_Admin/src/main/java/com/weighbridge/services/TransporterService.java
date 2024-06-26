package com.weighbridge.services;

import com.weighbridge.payloads.TransporterRequest;

import java.util.List;

/**
 * This Service interface for managing transporter master data.
 */
public interface TransporterService {
    /**
     * Saved a transporter using provided payload.
     *
     * @param transporterRequest The payload containing the transporter to be saved.
     * @return String containing a successful message.
     */
    String addTransporter(TransporterRequest transporterRequest);

    /**
     * Get all transporters
     * @return List of Strings containing all transporters
     */
    public List<String> getAllTransporter();
}
