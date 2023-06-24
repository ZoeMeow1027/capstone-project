using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.Enums;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("warranties")]
    public class WarrantyController : UserSessionControllerBase
    {
        private readonly IWarrantyService _warrantyService;

        public WarrantyController(IWarrantyService warrantyService, IUserSessionService userSessionService)
            : base(userSessionService)
        {
            _warrantyService = warrantyService;
        }

        [HttpGet]
        public ActionResult Get(int? id = null, string? query = null, bool includeExpired = false)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                // TODO: Warranty [Get] - User bill/Staff/Administrator area
                if (id != null)
                {
                    var user = GetUserByToken(Request.Cookies["token"]);
                    if (user == null)
                        throw new UnauthorizedAccessException("Unauthorized!");

                    var warrantyData = _warrantyService.GetWarrantyById(id.Value);
                    if (warrantyData == null)
                        throw new BadHttpRequestException("Warranty with this ID isn't exist!");

                    if (!(warrantyData.ID == user.ID || HasPermission(Request.Cookies["token"], new List<UserType>() { UserType.Administrator, UserType.Staff })))
                        throw new UnauthorizedAccessException("Unauthorized!");

                    // TODO: Return here!
                    result.Data = warrantyData;
                }
                // TODO: Warranty [Get] - Staff/Administrator area only
                else
                {
                    if (!HasPermission(Request.Cookies["token"], new List<UserType>() { UserType.Administrator, UserType.Staff }))
                        throw new UnauthorizedAccessException("Unauthorized!");

                    result.Data = _warrantyService.GetAllWarranties(includeExpired)
                        .Where(p => query == null ? true : (p.SerialNumberOrIMEI.ToLower().Contains(query) || p.Product.Name.ToLower().Contains(query)))
                        .ToList();
                }
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
