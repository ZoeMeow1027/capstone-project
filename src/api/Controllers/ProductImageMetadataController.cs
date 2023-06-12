using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Model.Enums;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/products/img")]
    public class ProductImageMetadataController : UserSessionControllerBase
    {
        private readonly IProductService _productService;
        private readonly IProductImageMetadataService _imageMetadataService;
        private readonly IVariableService _variableService;

        public ProductImageMetadataController(
            IUserSessionService userSessionService,
            IProductImageMetadataService imageMetadataService,
            IVariableService variableService,
            IProductService productService)
            : base(userSessionService)
        {
            _variableService = variableService;
            _variableService.CreateAppDirIfNotExist();

            _imageMetadataService = imageMetadataService;
            _productService = productService;
        }

        [HttpPost("upload")]
        public ActionResult Post([FromForm] ProductImageMetadataDTO formSubmit)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                // Check auth before continue.
                CheckPermission(
                    Request.Cookies["token"],
                    new List<UserType>() { UserType.Administrator }
                    );

                // Check if product exist!
                if (formSubmit.ProductID == null)
                {
                    throw new BadHttpRequestException(string.Format(
                        "Must provide product ID!",
                        formSubmit.ProductID
                        ));
                }
                if (_productService.GetProductById(formSubmit.ProductID.Value) == null)
                {
                    throw new BadHttpRequestException(string.Format(
                        "Product with ID {0} is not exist!",
                        formSubmit.ProductID
                        ));
                }

                // Generate full file name and file path.
                string fileName, fullPath;
                do
                {
                    fileName = string.Format(
                        "{0}{1}",
                        Utils.RandomString(64),
                        Path.GetExtension(formSubmit.File.FileName.Trim())
                        );

                    fullPath = Path.Combine(
                        _variableService.GetProductImageDirPath(),
                        fileName
                        );
                }
                while (System.IO.File.Exists(fullPath));

                // Save file to storage and metadata to database.
                using (var stream = new FileStream(fullPath, FileMode.Create))
                {
                    formSubmit.File.CopyTo(stream);
                }
                _imageMetadataService.Add(new ProductImageMetadata()
                {
                    Name = fileName,
                    FilePath = fileName,
                    ProductID = formSubmit.ProductID
                });

                result.StatusCode = 200;
                result.Message = "Successful";
                result.Data = _imageMetadataService.Get(fileName);
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

        [HttpGet("blob")]
        public ActionResult GetImageBlob(int id)
        {
            var data = _imageMetadataService.Get(id);
            if (data == null)
                return BadRequest(string.Format("Product image with ID {0} is not exist!", id));

            string filePath = _variableService.GetProductImageFilePath(data.FilePath);

            if (!System.IO.File.Exists(filePath))
                return BadRequest(string.Format("Product image with ID {0} is not exist!", id));
            return File(
                System.IO.File.ReadAllBytes(filePath),
                "image/jpeg"
                );
        }

        [HttpGet("metadata")]
        public ActionResult GetImageMetadata(int? id)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                if (id != null)
                {
                    var data = _imageMetadataService.Get(id.Value);
                    if (data == null)
                        throw new BadHttpRequestException(string.Format("Image with ID {0} is not exist!", id));

                    result.StatusCode = 200;
                    result.Data = data;
                }
                else
                {
                    result.StatusCode = 200;
                    result.Data = _imageMetadataService.GetAll();
                }
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

        [HttpPost("delete")]
        public ActionResult DeleteImageMetadata(int? id)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                // Check auth before continue.
                CheckPermission(
                    Request.Cookies["token"],
                    new List<UserType>() { UserType.Administrator }
                    );

                if (id != null)
                {
                    var data = _imageMetadataService.Get(id.Value);
                    if (data == null)
                        throw new BadHttpRequestException(string.Format("Image with ID {0} is not exist!", id));
                    _imageMetadataService.Delete(data);

                    try { System.IO.File.Delete(_variableService.GetProductImageFilePath(data.FilePath)); }
                    catch { }

                    result.StatusCode = 200;
                    result.Message = "Successful!";
                    result.Data = null;
                }
                else
                {
                    result.StatusCode = 200;
                    result.Data = _imageMetadataService.GetAll();
                }
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
    }
}
