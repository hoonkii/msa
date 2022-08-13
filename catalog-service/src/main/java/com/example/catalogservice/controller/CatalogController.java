package com.example.catalogservice.controller;

import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.CatalogResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
@RequiredArgsConstructor
public class CatalogController {
    private final Environment env;
    private final CatalogService catalogService;

    @GetMapping("/health_check")
    public String status(){
        return String.format("Its working in catalog service %s",env.getProperty("local.server.port"));
    }
    @GetMapping("/catalogs")
    public ResponseEntity<List<CatalogResponse>> getCatalogs(){
        Iterable<Catalog> catalogs = catalogService.getAllCatalogs();
        List<CatalogResponse> result = new ArrayList<>();
        catalogs.forEach(v -> {
            result.add(new ModelMapper().map(v, CatalogResponse.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
