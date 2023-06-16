using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IUserCartService
    {
        List<UserCart> GetAllItems(int userid);

        List<UserCart> GetAllItems(User user);

        void AddItem(int userid, int productid, int count);

        void AddItem(User user, int productid, int count);

        void AddItem(int userid, Product product, int count);

        void AddItem(User user, Product product, int count);

        void UpdateItem(int userid, int id, int count);

        void UpdateItem(User user, int id, int count);

        void RemoveItem(int userid, int cartId);

        void RemoveItem(User user, int cartId);

        void ClearCart(int userId);

        void ClearCart(User user);

        void ClearItemZeroCount();
    }
}
