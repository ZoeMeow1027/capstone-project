using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    public class Warranty
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public int ProductID { get; set; }

        public int? UserID { get; set; }

        public int? BillID { get; set; }

        [Required]
        public int WarrantyMonth { get; set; } = 12;

        [Required]
        public DateTime DateStart { get; set; } = DateTime.UtcNow;

        [Required]
        public DateTime DateEnd { get; set; } = DateTime.UtcNow;

        [Required]
        public string SerialNumberOrIMEI { get; set; } = string.Empty;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [ForeignKey("ProductID")]
        public Product Product { get; set; }

        [ForeignKey("BillID")]
        public BillSummary? Bill { get; set; }

        [ForeignKey("UserID")]
        public User? User { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
