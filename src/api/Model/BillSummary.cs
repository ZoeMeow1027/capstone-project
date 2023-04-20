using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class BillSummary
    {
        [Key]
        public int ID { get; set; }

        [Required]
        public int? UserID { get; set; } = null;

        [Required]
        public string Recipient { get; set; }

        [Required]
        public string RecipientAddress { get; set; }

        [Required]
        public DateTime DateCreated { get; set; } = DateTime.Now;

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
        public User? User { get; set; } = null;

        public List<BillDetails> BillDetails { get; set; }
        #endregion
    }
}
