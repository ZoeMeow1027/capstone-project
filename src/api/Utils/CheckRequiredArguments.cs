namespace PhoneStoreManager
{
    public static partial class Utils
    {
        public static void CheckRequiredArguments(dynamic data, List<string> reqArgs)
        {
            foreach (string argItem in reqArgs)
            {
                if (data[argItem] == null)
                {
                    throw new ArgumentNullException(
                        argItem,
                        string.Format(
                            "Missing '{0}' argument! (Required: {1})",
                            argItem,
                            string.Join(", ", reqArgs)
                        )
                    );
                }
            }
        }
    }
}
