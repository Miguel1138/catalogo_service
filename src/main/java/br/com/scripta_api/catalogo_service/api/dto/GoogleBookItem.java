package br.com.scripta_api.catalogo_service.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBookItem {
    private String id;
    private VolumeInfo volumeInfo;
}
