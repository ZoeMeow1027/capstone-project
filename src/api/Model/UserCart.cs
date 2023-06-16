using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    [Table("UserCart")]
    public class UserCart
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
        [JsonProperty("count")]
        public int Count { get; set; } = 0;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [JsonIgnore]
        public User User { get; set; }

        [JsonProperty("product")]
        public Product Product { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
