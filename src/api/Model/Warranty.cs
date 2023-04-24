using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace PhoneStoreManager.Model
{
    public class Warranty
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public int ProductID { get; set; }

        [Required]
        public int BillID { get; set; }

        [Required]
        public int WarrantyMonth { get; set; } = 12;

        [Required]
        public DateTime DateStart { get; set; } = DateTime.Now;

        [Required]
        public DateTime DateEnd { get; set; } = DateTime.Now;

        [Required]
        public string SerialNumberOrIMEI { get; set; } = string.Empty;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        public Product Product { get; set; }

        [JsonIgnore]
        public BillSummary Bill { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
