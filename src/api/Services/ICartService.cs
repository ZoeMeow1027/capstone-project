using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface ICartService
    {
        List<UserCart> GetAllItems(int userid);

        List<UserCart> GetAllItems(User user);

        void AddItem(int userid, int productid, int count);

        void AddItem(User user, int productid, int count);

        void AddItem(int userid, Product product, int count);

        void AddItem(User user, Product product, int count);

        void SubtractItemCount(int userid, int productid, int count);

        void SubtractItemCount(User user, int productid, int count);

        void SubtractItemCount(int userid, Product product, int count);

        void SubtractItemCount(User user, Product product, int count);

        void RemoveItem(int userid, int productid);

        void RemoveItem(User user, int productid);

        void RemoveItem(int userid, Product product);

        void RemoveItem(User user, Product product);

        void ClearItemZeroCount();
    }
}
