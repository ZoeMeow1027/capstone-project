using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class UserSession
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public int UserID { get; set; }

        [Required]
        public string Token { get; set; } = string.Empty;

        [Required]
        public DateTime DateCreated { get; set; } = DateTime.UtcNow;

        [Required]
        public DateTime DateExpired { get; set; } = DateTime.UtcNow;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        public User User { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
