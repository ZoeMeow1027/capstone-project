package io.zoemeow.pbl6.phonestoremanager.model.bean;

import com.google.gson.annotations.SerializedName;

public class StatisticsByMonth {
    @SerializedName("year")
    Integer yearFrom;

    @SerializedName("month")
    Integer monthFrom;

    @SerializedName("totalprice")
    Double totalPrice;

    @SerializedName("totaldelivery")
    Integer totalDelivery;

    @SerializedName("completeddeliery")
    Integer completedDelivery;

    @SerializedName("completedpercent")
    Double completedPercent;

    @SerializedName("maxpricedeliveryid")
    Integer maxPriceDeliveryId;

    @SerializedName("maxpricedelivery")
    BillSummary maxPriceDelivery;

    public Integer getYearFrom() {
        return yearFrom;
    }
    public void setYearFrom(Integer yearFrom) {
        this.yearFrom = yearFrom;
    }
    public Integer getMonthFrom() {
        return monthFrom;
    }
    public void setMonthFrom(Integer monthFrom) {
        this.monthFrom = monthFrom;
    }
    public Double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Integer getTotalDelivery() {
        return totalDelivery;
    }
    public void setTotalDelivery(Integer totalDelivery) {
        this.totalDelivery = totalDelivery;
    }
    public Integer getCompletedDelivery() {
        return completedDelivery;
    }
    public void setCompletedDelivery(Integer completedDelivery) {
        this.completedDelivery = completedDelivery;
    }
    public Double getCompletedPercent() {
        return completedPercent;
    }
    public void setCompletedPercent(Double completedPercent) {
        this.completedPercent = completedPercent;
    }
    public Integer getMaxPriceDeliveryId() {
        return maxPriceDeliveryId;
    }
    public void setMaxPriceDeliveryId(Integer maxPriceDeliveryId) {
        this.maxPriceDeliveryId = maxPriceDeliveryId;
    }
    public BillSummary getMaxPriceDelivery() {
        return maxPriceDelivery;
    }
    public void setMaxPriceDelivery(BillSummary maxPriceDelivery) {
        this.maxPriceDelivery = maxPriceDelivery;
    }

    
}
