using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    [Table("Warranty")]
    public class Warranty
    {
        [Key]
        [JsonProperty("id")]
        public int ID { get; set; }

        [Required]
        [JsonProperty("productid")]
        public int ProductID { get; set; }

        [JsonProperty("userid")]
        public int? UserID { get; set; }

        [JsonProperty("billid")]
        public int? BillID { get; set; }

        [Required]
        [JsonProperty("warrantymonth")]
        public int WarrantyMonth { get; set; } = 12;

        [Required]
        [JsonProperty("datestart")]
        public long DateStart { get; set; } = DateTimeOffset.Now.ToUnixTimeMilliseconds();

        [Required]
        [JsonProperty("dateend")]
        public long DateEnd { get; set; } = DateTimeOffset.Now.ToUnixTimeMilliseconds();

        [Required]
        [JsonProperty("serialnumberorimei")]
        public string SerialNumberOrIMEI { get; set; } = string.Empty;

        [Required]
        [JsonProperty("warrantydisabled")]
        public bool WarrantyDisabled { get; set; } = false;

        [Required]
        [JsonProperty("warrantydisabledreason")]
        public string WarrantyDisabledReason { get; set; } = string.Empty;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [ForeignKey("ProductID")]
        [JsonProperty("product")]
        public Product Product { get; set; }

        [ForeignKey("BillID")]
        [JsonProperty("bill")]
        public BillSummary? Bill { get; set; }

        [ForeignKey("UserID")]
        [JsonProperty("user")]
        public User? User { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
