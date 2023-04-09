package com.nidyran.rolebasedspringsecurity.web;

import com.nidyran.rolebasedspringsecurity.utils.BackendUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private-ressources")
@Tag(name = "Private Resource")
@RequiredArgsConstructor
public class PrivateController {

    @GetMapping("/customer")
    @PreAuthorize("hasAuthority('CUSTOMER_AUTHORITY')")
    public ResponseEntity<String> privateResourceCustomer() {
        return ResponseEntity.ok(BackendUtils.getCurrentUsername());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<String> privateResourceAdmin() {
        return ResponseEntity.ok(BackendUtils.getCurrentUsername());
    }
}