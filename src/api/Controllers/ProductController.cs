using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Model.Enums;
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
        public ActionResult Get(string? type = null, int? id = null, string? query = null, bool includehidden = false)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            if (type == null)
                type = "product";

            try
            {
                switch (type.ToLower())
                {
                    case "product":
                        result.Data = query != null
                            ? productService.FindAllProductsByName(query, includehidden)
                            : id == null
                                ? productService.GetAllProducts(includehidden)
                                : productService.GetProductById(id.Value);
                        break;
                    case "category":
                        result.Data = query != null
                            ? productCategoryService.FindAllProductCategoriesByName(query, includehidden)
                            : id == null
                                ? productCategoryService.GetAllProductCategories(includehidden)
                                : productCategoryService.GetProductCategoryById(id.Value);
                        break;
                    case "manufacturer":
                        result.Data = query != null
                            ? productManufacturerService.FindAllProductManufacturersByName(query, includehidden)
                            : id == null
                                ? productManufacturerService.GetAllProductManufacturers(includehidden)
                                : productManufacturerService.GetProductManufacturerById(id.Value);
                        break;
                    default:
                        throw new BadHttpRequestException("Invalid \"type\" value!");
                }
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 401;
                result.Message = uaEx.Message;
            }
            catch (BadHttpRequestException bhrEx)
            {
                result.StatusCode = 400;
                result.Message = bhrEx.Message;
            }
            catch (Exception ex)
            {
                result.StatusCode = 500;
                result.Message = ex.Message;
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
                RequestDTO? productDTO = args.ToObject<RequestDTO>();
                if (productDTO == null)
                    throw new Exception("Error while converting parameters!");

                CheckPermission(
                    Request.Cookies["token"],
                    new List<UserType>() { UserType.Administrator }
                    );

                if (productDTO.Type == null || productDTO.Action == null || productDTO.Data == null)
                {
                    throw new BadHttpRequestException("Missing parameters!");
                }

#pragma warning disable CS8602 // Dereference of a possibly null reference.
                switch (productDTO.Type.ToLower())
                {
                    case "manufacturer":
                        switch (productDTO.Action.ToLower())
                        {
                            case "add":
                                AddProductManufacturer(productDTO.Data.ToObject<ProductManufacturerDTO>());
                                break;
                            case "update":
                                UpdateProductManufacturer(productDTO.Data.ToObject<ProductManufacturerDTO>());
                                break;
                            case "delete":
                                DeleteProductManufacturerById(productDTO.Data.ToObject<ProductManufacturerDTO>());
                                break;
                            default:
                                throw new BadHttpRequestException("Invalid \"action\" value!");
                        }
                        break;
                    case "category":
                        switch (productDTO.Action.ToLower())
                        {
                            case "add":
                                AddProductCategory(productDTO.Data.ToObject<ProductCategoryDTO>());
                                break;
                            case "update":
                                UpdateProductCategory(productDTO.Data.ToObject<ProductCategoryDTO>());
                                break;
                            case "delete":
                                DeleteProductCategoryById(productDTO.Data.ToObject<ProductCategoryDTO>());
                                break;
                            default:
                                throw new BadHttpRequestException("Invalid \"action\" value!");
                        }
                        break;
                    case "product":
                        switch (productDTO.Action.ToLower())
                        {
                            case "add":
                                AddProduct(productDTO.Data.ToObject<ProductDTO>());
                                break;
                            case "update":
                                UpdateProduct(productDTO.Data.ToObject<ProductDTO>());
                                break;
                            case "delete":
                                HideProductById(productDTO.Data.ToObject<ProductDTO>());
                                break;
                            default:
                                throw new BadHttpRequestException("Invalid \"action\" value!");
                        }
                        break;
                    default:
                        throw new BadHttpRequestException("Invalid \"type\" value!");
                }
#pragma warning restore CS8602 // Dereference of a possibly null reference.

                result.StatusCode = 200;
                result.Message = "Successful!";
            }
            catch (UnauthorizedAccessException uaEx)
            {
                result.StatusCode = 403;
                result.Message = uaEx.Message;
            }
            catch (BadHttpRequestException bhrEx)
            {
                result.StatusCode = 400;
                result.Message = bhrEx.Message;
            }
            catch (Exception ex)
            {
                result.StatusCode = 500;
                result.Message = ex.Message;
            }

            return StatusCode(result.StatusCode, result.ToDynamicObject());
        }

        #region Product Manufacturer area
        private void AddProductManufacturer(ProductManufacturerDTO data)
        {
            List<string> reqArgList = new List<string>() { "name" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productManufacturerService.AddProductManufacturer(new ProductManufacturer()
            {
                Name = data.Name
            });
        }

        private void UpdateProductManufacturer(ProductManufacturerDTO data)
        {
            List<string> reqArgList = new List<string>() { "id", "name" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // Product Category [Update] - Check if product category exist
            var dataTemp = productManufacturerService.GetProductManufacturerById(data.ID.Value);
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("Bad Request: ProductManufacturer with ID {0} is not exist!", data.ID));
            }

            dataTemp.Name = data.Name;
            productManufacturerService.UpdateProductManufacturer(dataTemp);
        }

        private void DeleteProductManufacturerById(ProductManufacturerDTO data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productManufacturerService.DeleteProductManufacturerById(data.ID.Value);
        }
        #endregion

        #region Product Category area
        private void AddProductCategory(ProductCategoryDTO data)
        {
            List<string> reqArgList = new List<string>() { "name" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productCategoryService.AddProductCategory(new ProductCategory()
            {
                Name = data.Name
            });
        }

        private void UpdateProductCategory(ProductCategoryDTO data)
        {
            List<string> reqArgList = new List<string>() { "id", "name" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // Product Category [Update] - Check if product category exist
            var dataTemp = productCategoryService.GetProductCategoryById(data.ID.Value);
            if (dataTemp == null)
            {
                throw new BadHttpRequestException(string.Format("Bad Request: ProductCategory with ID {0} is not exist!", data.ID));
            }

            dataTemp.Name = data.Name;
            productCategoryService.UpdateProductCategory(dataTemp);
        }

        private void DeleteProductCategoryById(ProductCategoryDTO data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productCategoryService.DeleteProductCategoryById(data.ID.Value);
        }
        #endregion

        #region Product area
        private void AddProduct(ProductDTO data)
        {
            List<string> reqArgList = new List<string>() { "name", "categoryid", "manufacturerid", "price" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // Product [Add] - Check if invalid 'inventorycount' value
            if (data.InventoryCount != null)
            {
                if (data.InventoryCount < 0)
                {
                    throw new BadHttpRequestException("Invalid 'inventorycount' value!");
                }
            }
            // Product [Add] - Check if invalid 'warrantymonth' value
            if (data.WarrantyMonth != null)
            {
                if (data.WarrantyMonth < 0)
                {
                    throw new BadHttpRequestException("Invalid 'warrantymonth' value!");
                }
            }
            // Product [Add] - Check if invalid 'price' value
            if (data.Price < 0)
            {
                throw new BadHttpRequestException("Invalid 'price' value!");
            }

            // Product [Add] - Check if product category by id is exist
            if (productCategoryService.GetProductCategoryById(data.CategoryID.Value) == null)
            {
                throw new BadHttpRequestException(string.Format("Product Category ID {0} is not exist!", data.CategoryID));
            }

            // Product [Add] - Check if product manufacturer by id is exist
            if (productManufacturerService.GetProductManufacturerById(data.ManufacturerID.Value) == null)
            {
                throw new BadHttpRequestException(string.Format("Product Manufacturer ID {0} is not exist!", data.ManufacturerID));
            }

            productService.AddProduct(new Product()
            {
                Name = data.Name,
                CategoryID = data.CategoryID.Value,
                ManufacturerID = data.ManufacturerID.Value,
                InventoryCount = data.InventoryCount ?? 0,
                WarrantyMonth = data.WarrantyMonth ?? 12,
                Price = data.Price.Value,
                ShowInPage = data.ShowInPage ?? true
            });
        }

        private void UpdateProduct(ProductDTO data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            // Product [Update] - Check if invalid 'inventorycount' value
            if (data.InventoryCount != null)
            {
                if (data.InventoryCount < 0)
                {
                    throw new BadHttpRequestException("Invalid 'inventorycount' value!");
                }
            }
            // Product [Update] - Check if invalid 'warrantymonth' value
            if (data.WarrantyMonth != null)
            {
                if (data.WarrantyMonth < 0)
                {
                    throw new BadHttpRequestException("Invalid 'warrantymonth' value!");
                }
            }
            // Product [Update] - Check if invalid 'price' value
            if (data.Price != null)
            {
                if (data.Price < 0)
                {
                    throw new BadHttpRequestException("Invalid 'price' value!");
                }
            }
            // Product [Update] - Check if product category by id is exist
            if (data.CategoryID != null)
            {
                if (productCategoryService.GetProductCategoryById(data.CategoryID.Value) == null)
                {
                    throw new BadHttpRequestException(string.Format("Product Category ID {0} is not exist!", data.CategoryID));
                }
            }
            // Product [Update] - Check if product manufacturer by id is exist
            if (data.ManufacturerID != null)
            {
                if (productManufacturerService.GetProductManufacturerById(data.ManufacturerID.Value) == null)
                {
                    throw new BadHttpRequestException(string.Format("Product Manufacturer ID {0} is not exist!", data.ManufacturerID));
                }
            }

            // Product [Update] - Check if product exist
            var productTemp = productService.GetProductById(data.ID.Value);
            if (productTemp == null)
            {
                throw new BadHttpRequestException(string.Format("Product ID {0} is not exist!", data.ID));
            }

            productTemp.Name = data.Name ?? productTemp.Name;
            productTemp.CategoryID = data.CategoryID ?? productTemp.CategoryID;
            productTemp.ManufacturerID = data.ManufacturerID ?? productTemp.ManufacturerID;
            productTemp.InventoryCount = data.InventoryCount ?? productTemp.InventoryCount;
            productTemp.WarrantyMonth = data.WarrantyMonth ?? productTemp.WarrantyMonth;
            productTemp.Price = data.Price ?? productTemp.Price;
            productTemp.ShowInPage = data.ShowInPage ?? productTemp.ShowInPage;
            productTemp.DateModified = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            // Product [Update] - Begin updating
            productService.UpdateProduct(productTemp);
        }

        private void HideProductById(ProductDTO data)
        {
            List<string> reqArgList = new List<string>() { "id" };
            Utils.CheckRequiredArguments(data, reqArgList);

            productService.HideProductById(data.ID.Value);
        }
        #endregion
    }
}
