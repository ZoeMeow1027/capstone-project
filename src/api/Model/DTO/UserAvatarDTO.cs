using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
    public class UserAvatarDTO
    {
        [JsonProperty("file")]
        public IFormFile File { get; set; }
    }
}
