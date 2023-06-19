using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    [Table("BillDetails")]
    public class BillDetails
    {
        [Key]
        [JsonProperty("id")]
        public int ID { get; set; }

        [Required]
        [JsonProperty("billid")]
        public int BillID { get; set; }

        [Required]
        [JsonProperty("productid")]
        public int ProductID { get; set; }

        [Required]
        [JsonProperty("count")]
        public long Count { get; set; } = 0;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [JsonIgnore]
        [Required]
        [ForeignKey("BillID")]
        public BillSummary BillSummary { get; set; }

        [JsonProperty("product")]
        [Required]
        [ForeignKey("ProductID")]
        public Product Product { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
