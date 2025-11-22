package br.com.scripta_api.catalogo_service.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBooksResponse {
    private int totalItems;
    private List<GoogleBookItem> items;
}
