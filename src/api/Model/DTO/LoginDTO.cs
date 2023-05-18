using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
    public class LoginDTO
    {
        [JsonProperty("username")]
        public string Username { get; set; }

        [JsonProperty("password")]
        public string Password { get; set; }

        public bool IsValidatedLogin()
        {
            return (Username != null && Username.Length >= 5) || (Password != null && Password.Length >= 5);
        }
    }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
}
