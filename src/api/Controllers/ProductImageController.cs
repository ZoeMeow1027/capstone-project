using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.DTO;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/img/products/")]
    public class ProductImageController : UserSessionControllerBase
    {
        private readonly string DIR_APPDATA;
        private readonly string DIR_ROOT;
        private readonly string DIR_IMAGE;  
        private readonly string DIR_ROOTIMAGEPATH;
        private readonly IImageMetadataService _imageMetadataService;

        public ProductImageController(IUserSessionService userSessionService, IImageMetadataService imageMetadataService)
            : base(userSessionService)
        {
            this._imageMetadataService = imageMetadataService;

            DIR_APPDATA = Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData);
            DIR_ROOT = Path.Combine(new string[] { DIR_APPDATA, "PhoneStoreManager" });
            DIR_IMAGE = Path.Combine(new string[] { DIR_ROOT, "img" });

            DIR_ROOTIMAGEPATH = Path.Combine(new string[] { "img" });
        }

        [HttpPost("upload")]
        public ActionResult Post([FromForm] ProductImageMetadataDTO formSubmit)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                var fileName = string.Format("{0}{1}", Utils.RandomString(64), Path.GetExtension(formSubmit.File.FileName.Trim()));
                string? fullPath = null;
                do
                {
                    fullPath = Path.Combine(DIR_IMAGE, fileName);
                } while (System.IO.File.Exists(Path.Combine(new string[]{ DIR_IMAGE, fullPath })));

                using (var stream = new FileStream(fullPath, FileMode.Create))
                {
                    formSubmit.File.CopyTo(stream);
                }

                _imageMetadataService.Add(new ProductImageMetadata()
                {
                    Name = fileName,
                    FilePath = Path.Combine(new string[]{ DIR_ROOTIMAGEPATH, fileName }),
                    ProductID = formSubmit.ProductID
                });

                result.StatusCode = 200;
                result.Message = "Successful";
                result.Data = _imageMetadataService.Get(fileName);
            }
            catch (Exception ex)
            {
                result.StatusCode = 500;
                result.Message = ex.Message;
            }

            return StatusCode(result.StatusCode, result);
        }

        [HttpGet("blob")]
        public ActionResult GetImageBlob(int id)
        {
            var data = _imageMetadataService.Get(id);
            if (data == null)
                return BadRequest(string.Format("Image with ID {0} is not exist!", id));

            string path = Path.Combine(new string[] { DIR_ROOT, data.FilePath });

            if (!System.IO.File.Exists(path))
                return BadRequest(string.Format("Image with ID {0} is not exist!", id));
            return File(
                System.IO.File.ReadAllBytes(path),
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
    }
}
