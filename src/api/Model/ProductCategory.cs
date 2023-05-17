using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class ProductCategory
    {
        [Key]
        [JsonProperty("id")]
        public int ID { get; set; }

        [Required]
        [JsonProperty("name")]
        public string Name { get; set; } = string.Empty;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [JsonIgnore]
        public List<Product> Products { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
