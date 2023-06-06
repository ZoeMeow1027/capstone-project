using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace PhoneStoreManager.Model
{
    [Table("UserCart")]
    public class UserCart
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public int UserID { get; set; }

        [Required]
        public int ProductID { get; set; }

        [Required]
        public int Count { get; set; } = 0;

        #region Entity
        [JsonIgnore]
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        public User User { get; set; }

        public Product Product { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
