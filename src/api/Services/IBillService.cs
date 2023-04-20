using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IBillService
    {
        List<BillSummary> GetAllBillSummaries();

        List<BillSummary> FindAllBillSummariesByUserId(int userId);

        BillSummary GetBillSummaryById(int id);

        bool AddBill(BillSummary item);

        bool UpdateBill(BillSummary item);
    }
}
