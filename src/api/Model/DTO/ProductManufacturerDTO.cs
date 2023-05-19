using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
    public class ProductCategoryDTO
    {
        [JsonProperty("id")]
        public int? ID { get; set; }

        [JsonProperty("name")]
        public string? Name { get; set; }
    }
}
