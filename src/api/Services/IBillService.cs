using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IBillService
    {
        List<BillSummary> GetAllBillSummaries();

        List<BillSummary> FindAllBillSummariesByUserId(int userId);

        BillSummary? GetBillSummaryById(int id);

        void AddBill(BillSummary item);

        void UpdateBill(BillSummary item);

        void UpdateBillStatus(int id, DeliverStatus deliverStatus, string deliverStatusAddress);

        void UpdateBillPayment(int id, PaymentMethod method, string? methodName = null, string? paymentId = null);
    }
}
