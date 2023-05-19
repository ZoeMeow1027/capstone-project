using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

namespace PhoneStoreManager
{
    public static partial class Utils
    {
        public static void CheckRequiredArguments(object args, List<string> requiredProperties)
        {
            var data = JsonConvert.DeserializeObject<JToken>(JsonConvert.SerializeObject(args));

            foreach (var requiredPropertyItem in requiredProperties)
            {
                if (data[requiredPropertyItem] == null)
                {
                    throw new ArgumentNullException(
                        requiredPropertyItem,
                        string.Format(
                            "Missing '{0}' argument! (Required: {1})",
                            requiredPropertyItem,
                            string.Join(", ", requiredProperties)
                            )
                        );
                }
            }
        }
    }
}
