package com.wirehood.adminservice.service;

import com.wirehood.adminservice.entities.Admin;
import com.wirehood.adminservice.dto.request.AdminRequestDTO;
import com.wirehood.adminservice.dto.response.AdminResponseDTO;
import com.wirehood.adminservice.dto.response.ResponseDTO;

import java.util.List;

public interface AdminService {

    void saveAdmin(AdminRequestDTO requestDTO);

    AdminResponseDTO searchAdmin(AdminRequestDTO requestDTO);

    Admin updateAdmin(AdminRequestDTO requestDTO);

    Admin fetchAdminByUsername(String username);

    ResponseDTO adminsToSendEmails();


    List<Admin> fetchAllAdmins();
}
