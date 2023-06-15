using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    [Table("ProductComment")]
    public class ProductComment
    {
        [Key]
        [JsonProperty("id")]
        public int ID { get; set; }

        [Required]
        [JsonProperty("userid")]
        public int UserID { get; set; }

        [Required]
        [JsonProperty("productid")]
        public int ProductID { get; set; }

        [Required]
        [JsonProperty("comment")]
        public string Comment { get; set; } = string.Empty;

        [Required]
        [JsonProperty("rating")]
        public float? Rating { get; set; } = null;

        [JsonProperty("disabled")]
        public bool Disabled { get; set; } = false;

        [JsonProperty("disabledreason")]
        public string? DisabledReason { get; set; } = null;

        #region Entity
        [JsonProperty("user")]
        public User User { get; set; }

        [JsonIgnore]
        public Product Product { get; set; }
        #endregion
    }
}
