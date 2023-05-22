using Newtonsoft.Json;
using PhoneStoreManager.Model.Enums;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    [Table("User")]
    public class User
    {
        [Key]
        [JsonProperty("id")]
        public int ID { get; set; }

        [Required]
        [MaxLength(64)]
        [JsonProperty("username")]
        public string Username { get; set; } = string.Empty;

        [Required]
        [JsonIgnore]
        [JsonProperty("password")]
        public string Password { get; set; } = string.Empty;

        [Required]
        [JsonIgnore]
        public string PasswordHash { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; } = string.Empty;

        [JsonProperty("email")]
        public string? Email { get; set; } = null;

        [JsonProperty("phone")]
        public string? Phone { get; set; } = null;

        [JsonProperty("datecreated")]
        public long DateCreated { get; set; } = DateTimeOffset.Now.ToUnixTimeMilliseconds();

        [JsonProperty("datemodified")]
        public long DateModified { get; set; } = DateTimeOffset.Now.ToUnixTimeMilliseconds();

        [Required]
        [JsonProperty("isenabled")]
        public bool IsEnabled { get; set; } = true;

        [JsonProperty("disabledreason")]
        public string? DisabledReason { get; set; } = null;

        [Required]
        [JsonProperty("usertype")]
        public UserType UserType { get; set; } = UserType.NormalUser;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [JsonIgnore]
        public List<UserAddress> UserAddresses { get; set; }

        [JsonIgnore]
        public List<BillSummary> BillSummaries { get; set; }

        [JsonIgnore]
        public List<UserSession> UserSessions { get; set; }

        [JsonIgnore]
        public List<Warranty> Warranties { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
