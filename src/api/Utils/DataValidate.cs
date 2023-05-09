namespace PhoneStoreManager
{
    public static partial class Utils
    {
        public static class DataValidate
        {
            /// <summary>
            /// Validate phone.
            /// </summary>
            /// <param name="phone"></param>
            /// <returns></returns>
            public static bool IsValidPhone(string? phone)
            {
                if (phone == null)
                    return false;

                if (!phone.StartsWith('0') && !phone.StartsWith("+84"))
                    return false;

                foreach (char ch in phone)
                {
                    if (!(ch >= '0' && ch <= '9'))
                    {
                        return false;
                    }
                }

                if (phone.Length < 10 || phone.Length > 11)
                    return false;

                return true;
            }

            /// <summary>
            /// Validate email.
            /// Source: https://stackoverflow.com/questions/1365407/c-sharp-code-to-validate-email-address
            /// </summary>
            /// <param name="email"></param>
            /// <returns></returns>
            public static bool IsValidEmail(string? email)
            {
                if (email == null) return false;

                var trimmedEmail = email.Trim();

                if (trimmedEmail.EndsWith("."))
                {
                    return false;
                }
                try
                {
                    var addr = new System.Net.Mail.MailAddress(email);
                    return addr.Address == trimmedEmail;
                }
                catch
                {
                    return false;
                }
            }
        }
    }
}
