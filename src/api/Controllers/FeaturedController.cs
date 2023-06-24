using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("api/featured")]
    public class FeaturedController : UserSessionControllerBase
    {
        private readonly IProductService _productService;

        public FeaturedController(
            IUserSessionService userSessionService,
            IProductService productService
            ) : base(userSessionService)
        {
            _productService = productService;
        }

        [HttpGet("new")]
        public ActionResult GetNewProductByDay(int days = 7, int limit = 6)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                if (days <= 0)
                {
                    throw new BadHttpRequestException(string.Format("Invalid 'days' value! (must be > 0, you requested {0})", days));
                }

                if (limit <= 0)
                {
                    throw new BadHttpRequestException(string.Format("Invalid 'limit' value! (must be > 0, you requested {0})", limit));
                }

                result.Data = _productService.GetAllProducts(false)
                    .Where(p => p.DateCreated >= DateTimeOffset.UtcNow.AddDays(-days).ToUnixTimeMilliseconds())
                    .Take(limit)
                    .ToList();
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

        [HttpGet("mostview")]
        public ActionResult GetNewProductByDay(int limit = 6)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                if (limit <= 0)
                {
                    throw new BadHttpRequestException(string.Format("Invalid 'limit' value! (must be > 0, you requested {0})", limit));
                }

                result.Data = _productService.GetAllProducts(false)
                    .OrderByDescending(p => p.Views)
                    .Take(limit)
                    .ToList();
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
