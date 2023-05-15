using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class BillService : IBillService
    {
        private readonly DataContext _context;

        public BillService(DataContext context)
        {
            _context = context;
        }

        public void AddBill(BillSummary item)
        {
            _context.Add(item);
        }

        public List<BillSummary> FindAllBillSummariesByUserId(int userId)
        {
            return _context.BillSummaries.Where(p => p.UserID == userId).ToList();
        }

        public List<BillSummary> GetAllBillSummaries()
        {
            return _context.BillSummaries.ToList();
        }

        public BillSummary? GetBillSummaryById(int id)
        {
            return _context.BillSummaries.Where(p => p.ID == id).FirstOrDefault();
        }

        public void UpdateBill(BillSummary item)
        {
            var data = _context.BillSummaries.Where(p => p.ID == item.ID).FirstOrDefault();
            if (data == null)
                throw new Exception("Bill with ID is not exist!");

            // TODO: Update bill here!
            throw new NotImplementedException();
        }

        public void UpdateBillPayment(int id, PaymentMethod method, string? methodName = null, string? paymentId = null)
        {
            var data = _context.BillSummaries.Where(p => p.ID == id).FirstOrDefault();
            if (data == null)
                throw new Exception("Bill with ID is not exist!");

            data.PaymentMethod = method;
            data.PaymentMethodName = methodName;
            data.PaymentID = paymentId;

            _context.BillSummaries.Update(data);
            _context.SaveChanges();
        }

        public void UpdateBillStatus(int id, DeliverStatus deliverStatus, string deliverStatusAddress)
        {
            var data = _context.BillSummaries.Where(p => p.ID == id).FirstOrDefault();
            if (data == null)
                throw new Exception("Bill with ID is not exist!");

            data.Status = deliverStatus;
            data.StatusAddress = deliverStatusAddress;

            _context.BillSummaries.Update(data);
            _context.SaveChanges();
        }
    }
}
