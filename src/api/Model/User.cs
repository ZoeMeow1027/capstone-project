using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class User
    {
        [Key]
        public int ID { get; set; }

        [Required]
        [MaxLength(64)]
        public string Username { get; set; }

        [Required]
        public string Password { get; set; }

        public string Name { get; set; }

        public string Email { get; set; }

        public string Phone { get; set; }

        public DateTime DateCreated { get; set; } = DateTime.Now;

        [Required]
        public bool IsEnabled { get; set; } = true;

        [Required]
        public UserType UserType { get; set; } = UserType.Normal;

        #region Entity
        public List<UserAddress> UserAddresses { get; set; }

        public List<BillSummary> BillSummaries { get; set; }
        #endregion
    }
}
