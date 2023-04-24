using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/products")]
    public class ProductController : ControllerBase
    {
        private readonly IProductService productService;
        private readonly IProductCategoryService productCategoryService;
        private readonly IProductManufacturerService productManufacturerService;

        public ProductController(
            IProductService productService,
            IProductCategoryService productCategoryService,
            IProductManufacturerService productManufacturerService
            )
        {
            this.productService = productService;
            this.productCategoryService = productCategoryService;
            this.productManufacturerService = productManufacturerService;
        }

        [HttpGet]
        public ActionResult Get(string? type = null, int? id = null, string? nameQuery = null, bool includeHidden = false)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();

            if (type == null)
            {
                result.Data = productService.GetAllProducts(includeHidden);
            }
            else
            {
                switch (type.ToLower())
                {
                    case "product":
                        result.Data = nameQuery != null
                            ? productService.FindAllProductsByName(nameQuery, includeHidden)
                            : id == null
                                ? productService.GetAllProducts(includeHidden)
                                : productService.GetProductById(id.Value);
                        break;
                    case "category":
                        result.Data = nameQuery != null
                            ? productCategoryService.FindAllProductCategoriesByName(nameQuery)
                            : id == null
                                ? productCategoryService.GetAllProductCategories()
                                : productCategoryService.GetProductCategoryById(id.Value);
                        break;
                    case "manufacturer":
                        result.Data = nameQuery != null
                            ? productManufacturerService.FindAllProductManufacturersByName(nameQuery)
                            : id == null
                                ? productManufacturerService.GetAllProductManufacturers()
                                : productManufacturerService.GetProductManufacturerById(id.Value);
                        break;
                    default:
                        result.StatusCode = 400;
                        result.Message = "Invalid \"type\" value!";
                        break;
                }
            }

            return StatusCode(result.StatusCode, result.ToDynamicObject());
        }
    }
}
