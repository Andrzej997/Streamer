package pl.polsl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mateusz on 11.12.2016.
 */
public class RateEbookDTO {

    @JsonProperty("_ebookId")
    private Long ebookId;

    @JsonProperty("_rate")
    private Integer rate;

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
