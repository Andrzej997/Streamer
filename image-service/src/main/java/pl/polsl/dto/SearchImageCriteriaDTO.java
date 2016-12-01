package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mateusz on 01.12.2016.
 */
public class SearchImageCriteriaDTO {

    @JsonProperty("_textSearched")
    private String textSearched;

    @JsonProperty("_criteria")
    private String criteria;

    public String getTextSearched() {
        return textSearched;
    }

    public void setTextSearched(String textSearched) {
        this.textSearched = textSearched;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }
}
