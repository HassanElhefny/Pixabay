
package com.elhefny.pixabay.TargetImages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Images implements Serializable {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("totalHits")
    @Expose
    private Integer totalHits;
    @SerializedName("hits")
    @Expose
    private List<Hit> hits = null;
    private final static long serialVersionUID = 8857372152544516578L;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Images withTotal(Integer total) {
        this.total = total;
        return this;
    }

    public Integer getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    public Images withTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
        return this;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public Images withHits(List<Hit> hits) {
        this.hits = hits;
        return this;
    }

}
