using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    [Table("Product")]
    public class Product
    {
        [Key]
        [JsonProperty("id")]
        public int ID { get; set; }

        [Required]
        [JsonProperty("name")]
        public string Name { get; set; } = string.Empty;

        [Required]
        [JsonProperty("categoryid")]
        public int CategoryID { get; set; }

        [Required]
        [JsonProperty("manufacturerid")]
        public int ManufacturerID { get; set; }

        [Required]
        [JsonProperty("inventorycount")]
        public int InventoryCount { get; set; } = 0;

        [Required]
        [JsonProperty("unit")]
        public string Unit { get; set; } = "item";

        [Required]
        [JsonProperty("warrantymonth")]
        public int WarrantyMonth { get; set; } = 12;

        [Required]
        [JsonProperty("price")]
        public long Price { get; set; } = 0;

        [JsonProperty("metadata")]
        public string? Metadata { get; set; } = null;

        [Required]
        [JsonProperty("showinpage")]
        public bool ShowInPage { get; set; } = true;

        [Required]
        [JsonProperty("datecreated")]
        public long DateCreated { get; set; } = DateTimeOffset.Now.ToUnixTimeMilliseconds();

        [Required]
        [JsonProperty("datemodified")]
        public long DateModified { get; set; } = DateTimeOffset.Now.ToUnixTimeMilliseconds();

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [Required]
        [ForeignKey("CategoryID")]
        [JsonProperty("category")]
        public ProductCategory Category { get; set; }

        [Required]
        [ForeignKey("ManufacturerID")]
        [JsonProperty("manufacturer")]
        public ProductManufacturer Manufacturer { get; set; }

        [JsonIgnore]
        public List<BillDetails> BillDetails { get; set; }

        [JsonIgnore]
        public List<Warranty> Warranties { get; set; }

        [JsonProperty("images")]
        public List<ProductImageMetadata> Images { get; set; }

        [JsonIgnore]
        public UserCart UserCart { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
