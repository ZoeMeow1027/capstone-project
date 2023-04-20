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
        public BillSummary BillSummary { get; set; }

        public Product Product { get; set; }
        #endregion
    }
}
