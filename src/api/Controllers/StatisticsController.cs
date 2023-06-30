using Microsoft.AspNetCore.Mvc;
using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    [ApiController]
    [Route("statistics")]
    public class StatisticsController : UserSessionControllerBase
    {
        public StatisticsController(
            IUserSessionService userSessionService
            ) : base(userSessionService)
        {
        
        }
    }
}
