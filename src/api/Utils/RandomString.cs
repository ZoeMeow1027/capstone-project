namespace PhoneStoreManager
{
    public static partial class Utils
    {
        public static string RandomString(int length = 8)
        {
            var chars = "abcdefghijklmnopqrstuvwxyz0123456789";
            var random = new Random();

            string result = "";

            for (int i = 0; i < length; i++)
            {
                result += chars.ElementAt(random.Next(chars.Length));
            }

            return result;
        }
    }
}
