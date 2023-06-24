using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
    public class ProductDTO
    {
        [JsonProperty("id")]
        public int? ID { get; set; }

        [JsonProperty("name")]
        public string? Name { get; set; }

        [JsonProperty("categoryid")]
        public int? CategoryID { get; set; }

        [JsonProperty("manufacturerid")]
        public int? ManufacturerID { get; set; }

        [JsonProperty("inventorycount")]
        public int? InventoryCount { get; set; }

        [JsonProperty("unit")]
        public string? Unit { get; set; }

        [JsonProperty("article")]
        public string? Article { get; set; }

        [JsonProperty("specification")]
        public string? Specification { get; set; }

        [JsonProperty("warrantymonth")]
        public int? WarrantyMonth { get; set; }

        [JsonProperty("price")]
        public long? Price { get; set; }

        [JsonProperty("showinpage")]
        public bool? ShowInPage { get; set; }

    }
}
