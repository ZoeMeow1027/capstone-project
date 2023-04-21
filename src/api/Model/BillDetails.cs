using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model
{
    public class BillDetails
    {
        public int ID { get; set; }

        [Required]
        public int BillID { get; set; }

        [Required]
        public int ProductID { get; set; }

        [Required]
        public ulong Count { get; set; } = 0;

        public ulong Discount { get; set; } = 0;

        #region Entity
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        public BillSummary BillSummary { get; set; }

        public Product Product { get; set; }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        #endregion
    }
}
