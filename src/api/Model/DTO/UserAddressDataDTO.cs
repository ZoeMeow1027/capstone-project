using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
    public class UserAddressDataDTO
    {
        [JsonProperty("id")]
        public int? ID { get; set; }

        [JsonProperty("userid")]
        public int? UserID { get; set; }

        [JsonProperty("name")]
        public string? Name { get; set; }

        [JsonProperty("address")]
        public string? Address { get; set; }

        [JsonProperty("phone")]
        public string? Phone { get; set; }
    }
}
