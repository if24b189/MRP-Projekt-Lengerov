package org.example.dto;

/**
 * DTO for creating/updating Media via JSON.
 * This class is intentionally mutable so Jackson can deserialize it.
 */
public class MediaCreateRequest {

    public String title;
    public String description;
    public String genre;
    public String type;
    public int ageRestriction;
}
