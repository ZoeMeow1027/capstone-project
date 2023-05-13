using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/products")]
    public class ProductController : UserSessionControllerBase
    {
        private readonly IProductService productService;
        private readonly IProductCategoryService productCategoryService;
        private readonly IProductManufacturerService productManufacturerService;

        public ProductController(
            IProductService productService,
            IProductCategoryService productCategoryService,
            IProductManufacturerService productManufacturerService,
            IUserSessionService userSessionService
            ) : base(userSessionService)
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
        public ActionResult Post(JToken args)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                CheckPermission(
                    Request.Cookies["token"],
                    new List<UserType>() { UserType.Administrator, UserType.Staff }
                    );

                string? action = (string?)args["action"];
                string? type = (string?)args["type"];
                dynamic? data = args["data"];

                if (action == null || type == null || data == null)
                {
                    throw new ArgumentException("Invalid requests!");
                }

#pragma warning disable CS8602 // Dereference of a possibly null reference.
                switch (type.ToLower())
                {
                    case "manufacturer":
                        switch (action.ToLower())
                        {
                            case "add":
                                AddProductManufacturer(data);
                                break;
                            case "update":
                                UpdateProductManufacturer(data);
                                break;
                            case "delete":
                                DeleteProductManufacturerById(data);
                                break;
                            default:
                                throw new ArgumentException("Invalid \"action\" value!", "action");
                        }
                        break;
                    case "category":
                        switch (action.ToLower())
                        {
                            case "add":
                                AddProductCategory(data);
                                break;
                            case "update":
                                UpdateProductCategory(data);
                                break;
                            case "delete":
                                DeleteProductCategoryById(data);
                                break;
                            default:
                                throw new ArgumentException("Invalid \"action\" value!", "action");
                        }
                        break;
                    case "product":
                        switch (action.ToLower())
                        {
                            case "add":
                                AddProduct(data);
                                break;
                            case "update":
                                UpdateProduct(data);
                                break;
                            case "delete":
                                HideProductById(data);
                                break;
                            default:
                                throw new ArgumentException("Invalid \"action\" value!", "action");
                        }
                        break;
                    default:
                        throw new ArgumentException("Invalid \"type\" value!", "type");
                }
#pragma warning restore CS8602 // Dereference of a possibly null reference.

                result.StatusCode = 200;
                result.Message = "Successful!";
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 401;
                result.Message = uaEx.Message;
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
            List<string> reqArgList = new List<string>() { "name" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productManufacturerService.AddProductManufacturer(new ProductManufacturer()
            {
                Name = (string)data["name"]
            });
        }

        private void UpdateProductManufacturer(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "id", "name" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // Product Category [Update] - Check if product category exist
            var dataTemp = productManufacturerService.GetProductManufacturerById((int)data["id"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("ProductManufacturer with ID is not exist!", "id");
            }

            dataTemp.Name = (string)data["name"];
            productManufacturerService.UpdateProductManufacturer(dataTemp);
        }

        private void DeleteProductManufacturerById(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productManufacturerService.DeleteProductManufacturerById(data["id"]);
        }
        #endregion

        #region Product Category area
        private void AddProductCategory(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "name" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productCategoryService.AddProductCategory(new ProductCategory()
            {
                Name = (string)data["name"]
            });
        }

        private void UpdateProductCategory(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "id", "name" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // Product Category [Update] - Check if product category exist
            var dataTemp = productCategoryService.GetProductCategoryById((int)data["id"]);
            if (dataTemp == null)
            {
                throw new ArgumentException("ProductCategory with ID is not exist!", "id");
            }

            dataTemp.Name = (string)data["name"];
            productCategoryService.UpdateProductCategory(dataTemp);
        }

        private void DeleteProductCategoryById(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productCategoryService.DeleteProductCategoryById(data["id"]);
        }
        #endregion

        #region Product area
        private void AddProduct(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "name", "categoryid", "manufacturerid", "inventorycount", "warrantymonth", "price", "showinpage" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // Product [Add] - Check if invalid 'inventorycount' value
            if ((long)data[reqArgList[3]] < 0)
            {
                throw new ArgumentException("Invalid 'inventorycount' value!", "inventorycount");
            }

            // Product [Add] - Check if invalid 'warrantymonth' value
            if ((long)data[reqArgList[4]] < 0)
            {
                throw new ArgumentException("Invalid 'warrantymonth' value!", "warrantymonth");
            }

            // Product [Add] - Check if invalid 'price' value
            if ((long)data[reqArgList[5]] < 0)
            {
                throw new ArgumentException("Invalid 'price' value!", "price");
            }

            // Product [Add] - Check if product category by id is exist
            if (productCategoryService.GetProductCategoryById((int)data["categoryid"]) == null)
            {
                throw new ArgumentException("Product Category with ID is not exist!", "categoryid");
            }

            // Product [Add] - Check if product manufacturer by id is exist
            if (productManufacturerService.GetProductManufacturerById((int)data["manufacturerid"]) == null)
            {
                throw new ArgumentException("Product Manufacturer with ID is not exist!", "manufacturerid");
            }

            productService.AddProduct(new Product()
            {
                Name = (string)data["name"],
                CategoryID = (int)data["categoryid"],
                ManufacturerID = (int)data["manufacturerid"],
                InventoryCount = (int?)data["inventorycount"] ?? 0,
                WarrantyMonth = (int?)data["warrantymonth"] ?? 12,
                Price = (long)data["price"],
                ShowInPage = (bool?)data["showinpage"] ?? true
            });
        }

        private void UpdateProduct(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // Product [Update] - Check if invalid 'inventorycount' value
            if ((long)data[reqArgList[3]] < 0)
            {
                throw new ArgumentException("Invalid 'inventorycount' value!", "inventorycount");
            }

            // Product [Update] - Check if invalid 'warrantymonth' value
            if ((long)data[reqArgList[4]] < 0)
            {
                throw new ArgumentException("Invalid 'warrantymonth' value!", "warrantymonth");
            }

            // Product [Update] - Check if invalid 'price' value
            if ((long)data[reqArgList[5]] < 0)
            {
                throw new ArgumentException("Invalid 'price' value!", "price");
            }

            // Product [Update] - Check if product category by id is exist
            if (productCategoryService.GetProductCategoryById((int)data["categoryid"]) == null)
            {
                throw new ArgumentException("Product Category with ID is not exist!", "categoryid");
            }

            // Product [Update] - Check if product manufacturer by id is exist
            if (productManufacturerService.GetProductManufacturerById((int)data["manufacturerid"]) == null)
            {
                throw new ArgumentException("Product Manufacturer with ID is not exist!", "manufacturerid");
            }

            // Product [Update] - Check if product exist
            var productTemp = productService.GetProductById((int)data["id"]);
            if (productTemp == null)
            {
                throw new ArgumentException("Product with ID is not exist!", "id");
            }

            productTemp.Name = (string?)data["name"] ?? productTemp.Name;
            productTemp.CategoryID = (int?)data["categoryid"] ?? productTemp.CategoryID;
            productTemp.ManufacturerID = (int?)data["manufacturerid"] ?? productTemp.ManufacturerID;
            productTemp.InventoryCount = (int?)data["inventorycount"] ?? productTemp.InventoryCount;
            productTemp.WarrantyMonth = (int?)data["warrantymonth"] ?? productTemp.WarrantyMonth;
            productTemp.Price = (long?)data["price"] ?? productTemp.Price;
            productTemp.ShowInPage = (bool?)data["showinpage"] ?? productTemp.ShowInPage;
            productTemp.DateModified = DateTime.UtcNow;
            // Product [Update] - Begin updating
            productService.UpdateProduct(productTemp);
        }

        private void HideProductById(dynamic data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productService.HideProductById(data["id"]);
        }
        #endregion
    }
}
