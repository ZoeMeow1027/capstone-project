using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;

namespace PhoneStoreManager.Controllers
{
    [Route("api/product")]
    public class ProductController : ControllerBase
    {
        public ProductController()
        {

        }

        [HttpGet]
        public ActionResult<List<Product>> Get()
        {
            return new List<Product>();
        }
    }
}
