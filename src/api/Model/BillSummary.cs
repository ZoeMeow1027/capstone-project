using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PhoneStoreManager.Model
{
    public class BillSummary
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public int? UserID { get; set; } = null;

        [Required]
        public string Recipient { get; set; } = string.Empty;

        [Required]
        public string RecipientAddress { get; set; } = string.Empty;

        [Required]
        public DateTime DateCreated { get; set; } = DateTime.UtcNow;

        public long VAT { get; set; } = 0;

        [Required]
        public long TotalPrice { get; set; } = 0;

        [Required]
        public DeliverStatus Status { get; set; } = DeliverStatus.WaitingForConfirm;

        public string StatusAddress { get; set; } = string.Empty;

        [Required]
        public PaymentMethod PaymentMethod { get; set; } = PaymentMethod.COD;

        public string? PaymentMethodName { get; set; } = null;

        public string? PaymentID { get; set; } = null;

        [Required]
        public bool PaymentCompleted { get; set; } = false;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        [ForeignKey("UserID")]
        public User? User { get; set; } = null;

        public List<BillDetails> BillDetails { get; set; }

        [JsonIgnore]
        public List<Warranty> Warranties { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
