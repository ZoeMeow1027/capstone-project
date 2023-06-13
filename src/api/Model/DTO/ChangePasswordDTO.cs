using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
    public class ChangePasswordDTO
    {
        [JsonProperty("old-pass")]
        public string OldPassword { get; set; } = string.Empty;

        [JsonProperty("new-pass")]
        public string NewPassword { get; set; } = string.Empty;

        [JsonProperty("re-new-pass")]
        public string ConfirmPassword { get; set;} = string.Empty;
    }
}
