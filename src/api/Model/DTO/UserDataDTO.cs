using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
    public class UserDataDTO
    {
        [JsonProperty("id")]
        public int? ID { get; set; }

        [JsonProperty("username")]
        public string? Username { get; set; }

        [JsonProperty("password")]
        public string? Password { get; set; }

        [JsonProperty("name")]
        public string? Name { get; set; }

        [JsonProperty("email")]
        public string? Email { get; set; }

        [JsonProperty("phone")]
        public string? Phone { get; set; }

        [JsonProperty("isenabled")]
        public bool? IsEnabled { get; set; }

        [JsonProperty("disabledreason")]
        public string? DisabledReason { get; set; }

        [JsonProperty("usertype")]
        public int? UserType { get; set; }
    }
}
