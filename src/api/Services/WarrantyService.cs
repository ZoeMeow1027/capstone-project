using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class WarrantyService : IWarrantyService
    {
        private readonly DataContext _context;

        public WarrantyService(DataContext context)
        {
            _context = context;
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
                return (warranty.DateEnd < DateTime.Now);
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
            return _context.Warranties.Include(p => p.Product).Include(p => p.Bill).Where(p => (p.BillID == billId) && (includeExpired ? true : p.DateEnd > DateTime.Now)).ToList();
        }

        public List<Warranty> GetAllWarranties(bool includeExpired = true)
        {
            return _context.Warranties.Include(p => p.Product).Include(p => p.Bill).Where(p => (includeExpired ? true : p.DateEnd > DateTime.Now)).ToList();
        }

        public Warranty? GetWarrantyById(int id)
        {
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
    }
}
