using PhoneStoreManager.Model;
using PhoneStoreManager.Model.Enums;

namespace PhoneStoreManager.Services
{
    public interface IBillService
    {
        List<BillSummary> GetBillSummaries(int? userId = null, bool activeOnly = false);

        BillSummary? GetBillSummaryById(int id, int? userId = null);

        void AddBill(BillSummary item);

        void UpdateBill(BillSummary item);
    }
}
