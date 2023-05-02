using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class User
    {
        [Key]
        public int ID { get; set; }

        [Required]
        [MaxLength(64)]
        public string Username { get; set; } = string.Empty;

        [Required]
        public string Password { get; set; } = string.Empty;

        public string Name { get; set; } = string.Empty;

        public string? Email { get; set; } = null;

        public string? Phone { get; set; } = null;

        public DateTime DateCreated { get; set; } = DateTime.Now;

        [Required]
        public bool IsEnabled { get; set; } = true;

        [Required]
        public UserType UserType { get; set; } = UserType.Normal;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [JsonIgnore] 
        public List<UserAddress> UserAddresses { get; set; }

        [JsonIgnore] 
        public List<BillSummary> BillSummaries { get; set; }

        [JsonIgnore] 
        public List<UserSession> UserSessions { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
