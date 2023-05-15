using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IWarrantyService
    {
        List<Warranty> GetAllWarranties(bool includeExpired);

        List<Warranty> FindAllWarrantiesByBillId(int billId, bool includeExpired);

        Warranty? GetWarrantyById(int id);

        void AddWarranty(Warranty warranty);

        void UpdateWarranty(Warranty warranty);

        void DeleteWarranty(Warranty warranty);

        void DeleteWarrantyById(int id);

        #region Quick Actions
        void UpdateWarrantyStatus(bool forceUpdate);

        bool CheckIsExpired(Warranty warranty);

        bool CheckIsExpiredById(int id);
        #endregion
    }
}
