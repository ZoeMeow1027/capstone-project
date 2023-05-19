using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace PhoneStoreManager.Model.DTO
{
    public class RequestDTO
    {
        [JsonProperty("type")]
        public required string Type { get; set; }

        [JsonProperty("action")]
        public required string Action { get; set; }

        [JsonProperty("data")]
        public JToken? Data { get; set; }
    }
}
