using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.Enums;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("statistics")]
    public class StatisticsController : UserSessionControllerBase
    {
        private readonly IBillService _billService;
        private readonly IDashboardAndStatisticsService _dashboardAndStatisticsService;

        public StatisticsController(
            IUserSessionService userSessionService,
            IBillService billService,
            IDashboardAndStatisticsService dashboardAndStatisticsService
            ) : base(userSessionService)
        {
            _billService = billService;
            _dashboardAndStatisticsService = dashboardAndStatisticsService;
        }

        [HttpGet("bymonth")]
        public ActionResult Get(int? year, int? month)
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                CheckPermission(
                    Request.Cookies["token"],
                    new List<UserType>() { UserType.Administrator }
                );

                result.Data = _dashboardAndStatisticsService.GetStatisticsByMonth(year, month);
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

        [HttpGet("dashboard")]
        public ActionResult GetDashboard()
        {
            ReturnResultTemplate result = new ReturnResultTemplate();
            result.StatusCode = 200;

            try
            {
                CheckPermission(
                    Request.Cookies["token"],
                    new List<UserType>() { UserType.Administrator }
                );

                result.Data = _dashboardAndStatisticsService.GetDashboard();
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
    }
}
