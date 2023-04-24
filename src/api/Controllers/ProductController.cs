using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
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

        [HttpPost]
        public ActionResult Post(JToken data)
        {
            try
            {
                string? action = (string?)data["action"];
                string? type = (string?)data["type"];
                dynamic? data1 = data["data"];

                object dataTemp = null;

                if (action == null || type == null || data1 == null)
                {
                    return StatusCode(400, "Invalid requests!");
                }

                switch (type)
                {
                    case "manufacturer":
                        switch (action)
                        {
                            case "add":
                                if (data1["name"] == null)
                                {
                                    return StatusCode(400, "Missing necessary data!");
                                }

                                productManufacturerService.AddProductManufacturer(new ProductManufacturer()
                                {
                                    Name = (string)data1["name"]
                                });
                                break;
                        }
                        break;
                    case "category":
                        switch (action)
                        {
                            case "add":
                                if (data1["name"] == null)
                                {
                                    return StatusCode(400, "Missing necessary data!");
                                }

                                productCategoryService.AddProductCategory(new ProductCategory()
                                {
                                    Name = (string)data1["name"]
                                });
                                break;
                        }
                        break;
                    case "product":
                        switch (action)
                        {
                            case "add":
                                if (data1["name"] == null || data1["categoryid"] == null || data1["manufacturerid"] == null || data1["price"] == null)
                                {
                                    return StatusCode(400, "Missing necessary data!");
                                }

                                productService.AddProduct(new Product()
                                {
                                    Name = (string)data1["name"],
                                    CategoryID = (int)data1["categoryid"],
                                    ManufacturerID = (int)data1["manufacturerid"],
                                    InventoryCount = (int?)data1["inventorycount"] ?? 0,
                                    WarrantyMonth = (int?)data1["warrantymonth"] ?? 12,
                                    Price = (long)data1["price"],
                                    ShowInPage = (bool?)data1["showinpage"] ?? true
                                });
                                break;
                        }
                        break;
                }

                return StatusCode(200, JsonConvert.DeserializeObject<JToken>(data.ToString()));
            }
            catch (Exception ex)
            {
                return StatusCode(500, new ReturnResultTemplate(500, ex.Message));
            }
        }
    }
}
