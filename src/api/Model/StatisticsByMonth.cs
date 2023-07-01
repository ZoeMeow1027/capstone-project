using Newtonsoft.Json;

namespace PhoneStoreManager.Model
{
    public class StatisticsByMonth
    {
        [JsonProperty("year")]
        public int? YearFrom { get; set; } = null;

        [JsonProperty("month")]
        public int? MonthFrom { get; set; } = null;

        [JsonIgnore]
        public int? YearTo { get; set; } = null;

        [JsonIgnore]
        public int? MonthTo { get; set; } = null;

        [JsonProperty("totalprice")]
        public double TotalPrice { get; set; } = 0.0;

        [JsonProperty("totaldelivery")]
        public int TotalDelivery { get; set; } = 0;

        [JsonProperty("completeddeliery")]
        public int CompletedDelivery { get; set; } = 0;

        [JsonProperty("completedpercent")]
        public double CompletedPercent { get; set; } = 0.0;

        [JsonProperty("maxpricedeliveryid")]
        public int? MaxPriceDeliveryID { get; set; } = null;

        #region
        [JsonProperty("maxpricedelivery")]
        public BillSummary? BillSummary { get; set; } = null;
        #endregion
    }
}
