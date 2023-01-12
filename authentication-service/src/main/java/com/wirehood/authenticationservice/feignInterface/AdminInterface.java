package com.wirehood.authenticationservice.feignInterface;

import com.wirehood.authenticationservice.responseDTO.AdminResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static com.wirehood.authenticationservice.constants.MicroServiceConstants.*;

@FeignClient(name = ADMIN_MICROSERVICE, path = "/api/admin-service")
@Service
//@RequestMapping(value = BASE_API)
public interface AdminInterface {

    @GetMapping(value = BASE_API + AdminMicroServiceConstants.FETCH_ADMIN_BY_USERNAME)
    Optional<AdminResponseDTO> fetchAdminByUsername(@PathVariable("username") String username);
}
