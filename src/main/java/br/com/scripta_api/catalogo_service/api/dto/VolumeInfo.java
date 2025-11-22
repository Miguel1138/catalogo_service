package br.com.scripta_api.catalogo_service.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInfo {
    private String title;
    private List<String> authors;
    private String publishedDate;
    private String description;

    private List<IndustryIdentifier> industryIdentifiers;
    private int pageCount;
    private List<String> categories;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IndustryIdentifier {
        private String type; // Ex: "ISBN_13"
        private String identifier;
    }
}
