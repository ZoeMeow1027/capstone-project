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
            ) : base()
        {
            this.productService = productService;
            this.productCategoryService = productCategoryService;
            this.productManufacturerService = productManufacturerService;
        }

        [HttpGet]
        public ActionResult Get(string? type = null, int? id = null, string? nameQuery = null, bool includeHidden = false)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            if (type == null)
            {
                result.StatusCode = 200;
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
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                string? action = (string?)data["action"];
                string? type = (string?)data["type"];
                dynamic? data1 = data["data"];

                if (action == null || type == null || data1 == null)
                {
                    result.StatusCode = 400;
                    result.Message = "Invalid requests!";
                }
                else
                {
                    switch (type)
                    {
                        case "manufacturer":
                            switch (action)
                            {
                                case "add":
                                    AddProductManufacturer(data1);
                                    break;
                                case "update":
                                    UpdateProductManufacturer(data1);
                                    break;
                                case "delete":
                                    DeleteProductManufacturerById(data1);
                                    break;
                            }
                            break;
                        case "category":
                            switch (action)
                            {
                                case "add":
                                    AddProductCategory(data1);
                                    break;
                                case "update":
                                    UpdateProductCategory(data1);
                                    break;
                                case "delete":
                                    DeleteProductCategoryById(data1);
                                    break;
                            }
                            break;
                        case "product":
                            switch (action)
                            {
                                case "add":
                                    AddProduct(data1);
                                    break;
                                case "update":
                                    UpdateProduct(data1);
                                    break;
                                case "delete":
                                    HideProductById(data1);
                                    break;
                            }
                            break;
                        default:
                            throw new ArgumentException("Invalid \"type\" value!", "type");
                    }

                    result.StatusCode = 200;
                    result.Message = "Successful!";
                }
            }
            catch (ArgumentNullException argNullEx)
            {
                result.StatusCode = 400;
                result.Message = argNullEx.Message;
            }
            catch (ArgumentException argEx)
            {
                result.StatusCode = 400;
                result.Message = argEx.Message;
            }
            catch (Exception ex)
            {
                result.StatusCode = 500;
                result.Message = ex.Message;
            }

            return StatusCode(result.StatusCode, result.ToDynamicObject());
        }

        #region Product Manufacturer area
        private void AddProductManufacturer(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "name" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            productManufacturerService.AddProductManufacturer(new ProductManufacturer()
            {
                Name = (string)data[reqParamList[0]]
            });
        }

        private void UpdateProductManufacturer(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "id", "name" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            productManufacturerService.UpdateProductManufacturer(new ProductManufacturer()
            {
                ID = (int)data[reqParamList[0]],
                Name = (string)data[reqParamList[1]]
            });
        }

        private void DeleteProductManufacturerById(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "id" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            productManufacturerService.DeleteProductManufacturerById(data[reqParamList[0]]);
        }
        #endregion

        #region Product Category area
        private void AddProductCategory(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "name" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            productCategoryService.AddProductCategory(new ProductCategory()
            {
                Name = (string)data[reqParamList[0]]
            });
        }

        private void UpdateProductCategory(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "id", "name" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            productCategoryService.UpdateProductCategory(new ProductCategory()
            {
                ID = (int)data[reqParamList[0]],
                Name = (string)data[reqParamList[1]]
            });
        }

        private void DeleteProductCategoryById(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "id" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            productCategoryService.DeleteProductCategoryById(data[reqParamList[0]]);
        }
        #endregion

        #region Product area
        private void AddProduct(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "name", "categoryid", "manufacturerid", "inventorycount", "warrantymonth", "price", "showinpage" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            // Check if invalid inventorycount value
            if ((long)data[reqParamList[3]] < 0)
            {
                throw new ArgumentException("Invalid 'inventorycount' value!", reqParamList[3]);
            }

            // Check if invalid inventorycount value
            if ((long)data[reqParamList[4]] < 0)
            {
                throw new ArgumentException("Invalid 'warrantymonth' value!", reqParamList[4]);
            }

            // Check if invalid price value
            if ((long)data[reqParamList[5]] < 0)
            {
                throw new ArgumentException("Invalid 'price' value!", reqParamList[5]);
            }

            productService.AddProduct(new Product()
            {
                Name = (string)data[reqParamList[0]],
                CategoryID = (int)data[reqParamList[1]],
                ManufacturerID = (int)data[reqParamList[2]],
                InventoryCount = (int?)data[reqParamList[3]] ?? 0,
                WarrantyMonth = (int?)data[reqParamList[4]] ?? 12,
                Price = (long)data[reqParamList[5]],
                ShowInPage = (bool?)data[reqParamList[6]] ?? true
            });
        }

        private void UpdateProduct(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "id", "name", "categoryid", "manufacturerid", "inventorycount", "warrantymonth", "price", "showinpage" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            // Check if invalid inventorycount value
            if ((long)data[reqParamList[3]] < 0)
            {
                throw new ArgumentException("Invalid 'inventorycount' value!", reqParamList[3]);
            }

            // Check if invalid inventorycount value
            if ((long)data[reqParamList[4]] < 0)
            {
                throw new ArgumentException("Invalid 'warrantymonth' value!", reqParamList[4]);
            }

            // Check if invalid price value
            if ((long)data[reqParamList[5]] < 0)
            {
                throw new ArgumentException("Invalid 'price' value!", reqParamList[5]);
            }

            productService.UpdateProduct(new Product()
            {
                ID = (int)data[reqParamList[0]],
                Name = (string)data[reqParamList[1]],
                CategoryID = (int)data[reqParamList[2]],
                ManufacturerID = (int)data[reqParamList[3]],
                InventoryCount = (int?)data[reqParamList[4]] ?? 0,
                WarrantyMonth = (int?)data[reqParamList[5]] ?? 12,
                Price = (long)data[reqParamList[6]],
                ShowInPage = (bool?)data[reqParamList[7]] ?? true
            });
        }

        private void HideProductById(dynamic data)
        {
            List<string> reqParamList = new List<string>() { "id" };

            foreach (string reqParamItem in reqParamList)
            {
                if (data[reqParamItem] == null)
                {
                    throw new ArgumentNullException(reqParamItem, string.Format("Missing some data! (Required: {0})", string.Join(", ", reqParamList)));
                }
            }

            productService.HideProductById(data[reqParamList[0]]);
        }
        #endregion
    }
}
