using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace PhoneStoreManager.Model.DTO
{
    public class ProductImageMetadataDTO
    {
        [JsonProperty("file")]
        public IFormFile File { get; set; }

        [JsonProperty("productid")]
        public int ProductID { get; set; }
    }
}
