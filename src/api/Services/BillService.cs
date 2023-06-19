using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.Enums;

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
            _context.BillSummaries.Add(item);
            _context.SaveChanges();
        }

        public List<BillSummary> GetBillSummaries(int? userId = null, bool activeOnly = false)
        {
            var data = _context.BillSummaries
                .Include(p => p.User)
                .Include(p => p.BillDetails).ThenInclude(c => c.Product).ThenInclude(q => q.Images)
                .Include(p => p.Warranties)
                .OrderByDescending(p => p.DateCreated)
                .ToList();
            if (userId != null)
            {
                data = data.Where(p => p.UserID == userId).ToList();
            }
            if (activeOnly)
            {
                data = data.Where(p => (new DeliverStatus[] { DeliverStatus.Completed, DeliverStatus.Failed, DeliverStatus.Cancelled }.Contains(p.Status)) == false).ToList();
            }
            return data;
        }

        public BillSummary? GetBillSummaryById(int id, int? userId = null)
        {
            return GetBillSummaries(userId)
                .Where(p => p.ID == id)
                .FirstOrDefault();
        }

        public void UpdateBill(BillSummary item)
        {
            var data = _context.BillSummaries
                .Where(p => p.ID == item.ID)
                .FirstOrDefault();
            if (data == null)
                throw new Exception("Bill with ID is not exist!");

            data.DateCompleted = item.DateCompleted;
            data.PaymentCompleted = item.PaymentCompleted;
            data.PaymentID = item.PaymentID;
            data.PaymentMethod = item.PaymentMethod;
            data.PaymentMethodName = item.PaymentMethodName;
            data.PaymentCompleted = item.PaymentCompleted;
            data.Status = item.Status;
            data.StatusAddress = item.StatusAddress;
            data.StatusAdditional = item.StatusAdditional;

            _context.BillSummaries.Update(data);
            _context.SaveChanges();
        }
    }
}
