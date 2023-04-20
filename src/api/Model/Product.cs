using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class Product
    {
        [Key]
        public int ID { get; set; }
        
        [Required]
        public string Name { get; set; }

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

        #region Entity
        public ProductCategory Category { get; set; }

        public ProductManufacturer Manufacturer { get; set; }

        public List<BillDetails> BillDetails { get; set; }
        #endregion
    }
}
