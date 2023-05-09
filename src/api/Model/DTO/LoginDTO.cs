using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
    public class LoginDTO
    {
        [JsonProperty("username")]
        public string Username { get; set; }

        [JsonProperty("password")]
        public string Password { get; set; }
    }
}
