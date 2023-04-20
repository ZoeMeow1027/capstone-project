using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class UserAddress
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public int UserID { get; set; }

        public string Name { get; set; }

        public string Address { get; set; }

        public string Phone { get; set; }

        #region Entity
        public User User { get; set; }
        #endregion
    }
}
