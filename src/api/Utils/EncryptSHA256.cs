using System.Security.Cryptography;
using System.Text;

namespace PhoneStoreManager
{
    public static partial class Utils
    {
        /// <summary>
        /// Encrypt string with SHA256 method.
        /// Source: https://stackoverflow.com/a/17001289
        /// </summary>
        /// <param name="input">String that needs to be encrypted.</param>
        /// <returns>Encrypted string in SHA256 method.</returns>
        public static string EncryptSHA256(string input)
        {
            StringBuilder Sb = new StringBuilder();

            // Old method (All .NET)
            //using (SHA256 hash = SHA256Managed.Create())
            //{
            //    Encoding enc = Encoding.UTF8;
            //    Byte[] result = hash.ComputeHash(enc.GetBytes(input));
            //    foreach (Byte b in result)
            //        Sb.Append(b.ToString("x2"));
            //}

            // New method
            // Note: this is available only with .NET 5 or higher.
            using (var hash = SHA256.Create())
            {
                Encoding enc = Encoding.UTF8;
                byte[] result = hash.ComputeHash(enc.GetBytes(input));
                foreach (byte b in result)
                    Sb.Append(b.ToString("x2"));
            }

            return Sb.ToString();
        }
    }
}
