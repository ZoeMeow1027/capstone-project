using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class WarrantyService : IWarrantyService
    {
        private readonly DataContext _context;
        private DateTime _dateTimeStamp;
        private int _updateIntervalBySecond;

        public WarrantyService(DataContext context)
        {
            _context = context;
            _dateTimeStamp = DateTime.UtcNow.Subtract(new TimeSpan(0, 0, 1));
            _updateIntervalBySecond = 60;
        }

        public void AddWarranty(Warranty warranty)
        {
            _context.Warranties.Add(warranty);
            int _rowAffected = _context.SaveChanges();
            if (_rowAffected != 1)
            {
                throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
            }
        }

        public bool CheckIsExpired(Warranty warranty)
        {
            var data = _context.Warranties.Where(p => p.ID == warranty.ID).FirstOrDefault();
            if (data != null)
            {
                return (warranty.DateEnd < DateTime.UtcNow);
            }
            else
            {
                throw new Exception(string.Format("Warranty with ID {0} is not exist!", warranty.ID));
            }
        }

        public bool CheckIsExpiredById(int id)
        {
            var data = _context.Warranties.Where(p => p.ID == id).FirstOrDefault();
            if (data != null)
            {
                return CheckIsExpired(data);
            }
            else
            {
                throw new Exception(string.Format("Warranty with ID {0} is not exist!", id));
            }
        }

        public void DeleteWarranty(Warranty warranty)
        {
            var data = _context.Warranties.Where(p => p.ID == warranty.ID).FirstOrDefault();
            if (data != null)
            {
                _context.Warranties.Remove(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("Warranty with ID {0} is not exist!", warranty.ID));
            }
        }

        public void DeleteWarrantyById(int id)
        {
            var data = _context.Warranties.Where(p => p.ID == id).FirstOrDefault();
            if (data != null)
            {
                DeleteWarranty(data);
            }
            else
            {
                throw new Exception(string.Format("Warranty with ID {0} is not exist!", id));
            }
        }

        public List<Warranty> FindAllWarrantiesByBillId(int billId, bool includeExpired = true)
        {
            UpdateWarrantyStatus();
            return _context.Warranties.Include(p => p.Product).Include(p => p.Bill).Where(p => (p.BillID == billId) && (includeExpired ? true : p.DateEnd > DateTime.UtcNow)).ToList();
        }

        public List<Warranty> GetAllWarranties(bool includeExpired = true)
        {
            UpdateWarrantyStatus();
            return _context.Warranties.Include(p => p.Product).Include(p => p.Bill).Where(p => (includeExpired ? true : p.DateEnd > DateTime.UtcNow)).ToList();
        }

        public Warranty? GetWarrantyById(int id)
        {
            UpdateWarrantyStatus();
            return _context.Warranties.Include(p => p.Product).Include(p => p.Bill).Where(p => p.ID == id).FirstOrDefault();
        }

        public void UpdateWarranty(Warranty warranty)
        {
            // TODO: Needs to be reviewed before release.
            var data = GetWarrantyById(warranty.ID);
            if (data != null)
            {
                data.WarrantyMonth = warranty.WarrantyMonth;
                data.DateEnd = warranty.DateEnd;
                data.SerialNumberOrIMEI = warranty.SerialNumberOrIMEI;
                if (data.DateEnd < DateTime.UtcNow && !data.WarrantyDisabled)
                {
                    data.WarrantyDisabled = true;
                    data.WarrantyDisabledReason = "Warranty has beed expired.";
                }

                _context.Warranties.Update(data);
                int _rowAffected = _context.SaveChanges();
                if (_rowAffected != 1)
                {
                    throw new Exception(string.Format("Affected rows: {0}!", _rowAffected));
                }
            }
            else
            {
                throw new Exception(string.Format("Warranty with ID {0} is not exist!", warranty.ID));
            }
        }

        public void UpdateWarrantyStatus(bool forceUpdate = false)
        {
            if (_dateTimeStamp < DateTime.UtcNow || forceUpdate)
            {
                _context.Warranties
                    .Where(p => (!p.WarrantyDisabled && p.DateEnd < DateTime.UtcNow))
                    .ExecuteUpdate(p => p.SetProperty(d => d.WarrantyDisabled, d => true).SetProperty(d => d.WarrantyDisabledReason, d => "Warranty has been expired."));

                _dateTimeStamp = DateTime.UtcNow.AddSeconds(_updateIntervalBySecond);
            }
        }
    }
}
