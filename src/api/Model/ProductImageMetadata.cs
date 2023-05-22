using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    [Table("ImageMetadata")]
    public class ProductImageMetadata
    {
        [Key]
        [JsonProperty("id")]
        public int ID { get; set; }

        [Required]
        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("description")]
        public string? Description { get; set; }

        [Required]
        [JsonIgnore]
        [JsonProperty("filepath")]
        public string FilePath { get; set; }

        [Required]
        [JsonProperty("productid")]
        public int? ProductID { get; set; }

        [Required]
        [JsonProperty("datecreated")]
        public long DateCreated { get; set; } = DateTimeOffset.Now.ToUnixTimeMilliseconds();

        [Required]
        [JsonProperty("datemodified")]
        public long DateModified { get; set; } = DateTimeOffset.Now.ToUnixTimeMilliseconds();

        #region Entity
        [JsonIgnore]
        public Product Product { get; set; }
        #endregion
    }
}
