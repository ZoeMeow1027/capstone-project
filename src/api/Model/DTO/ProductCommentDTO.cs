using Newtonsoft.Json;

namespace PhoneStoreManager.Model.DTO
{
    public class ProductCommentDTO
    {
        [JsonProperty("productid")]
        public int? ProductID { get; set; }

        [JsonProperty("rating")]
        public int? Rating { get; set; }

        [JsonProperty("comment")]
        public string? Comment { get; set; }
    }
}
