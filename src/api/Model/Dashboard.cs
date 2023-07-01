using Newtonsoft.Json;

namespace PhoneStoreManager.Model
{
    public class Dashboard
    {
        [JsonProperty("deliveryremaining")]
        public int RemainingDeliveries { get; set; } = 0;

        [JsonProperty("hotsellingproducts")]
        public dynamic? HotSellingProducts { get; set; } = null;

        [JsonProperty("registeredmembers")]
        public int RegisteredMembers { get; set; } = 0;
    }
}
