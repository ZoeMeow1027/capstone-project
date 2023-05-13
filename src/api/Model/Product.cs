using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    public class Product
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public string Name { get; set; } = string.Empty;

        [Required]
        public int CategoryID { get; set; }

        [Required]
        public int ManufacturerID { get; set; }

        [Required]
        public int InventoryCount { get; set; } = 0;

        [Required]
        public int WarrantyMonth { get; set; } = 12;

        [Required]
        public long Price { get; set; } = 0;

        [Required]
        public bool ShowInPage { get; set; } = true;

        [Required]
        public DateTime DateCreated { get; set; } = DateTime.UtcNow;

        [Required]
        public DateTime DateModified { get; set; } = DateTime.UtcNow;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [Required]
        [ForeignKey("CategoryID")]
        public ProductCategory Category { get; set; }

        [Required]
        [ForeignKey("ManufacturerID")]
        public ProductManufacturer Manufacturer { get; set; }

        [JsonIgnore]
        public List<BillDetails> BillDetails { get; set; }

        [JsonIgnore]
        public List<Warranty> Warranties { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
