using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
    public class ProductManufacturerDTO
    {
        [JsonProperty("id")]
        public int? ID { get; set; }

        [JsonProperty("name")]
        public string? Name { get; set; }
    }
}
