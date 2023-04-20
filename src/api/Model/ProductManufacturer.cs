using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class ProductManufacturer
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public string Name { get; set; } = string.Empty;

        [Required]
        public bool ShowInPage { get; set; } = true;

        #region Entity
        public List<Product> Products { get; set; }
        #endregion
    }
}
