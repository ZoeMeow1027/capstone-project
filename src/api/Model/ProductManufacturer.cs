using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class ProductManufacturer
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public string Name { get; set; }

        #region Entity
        public List<Product> Products { get; set; }
        #endregion
    }
}
