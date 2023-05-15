using PhoneStoreManager.Services;

namespace PhoneStoreManager.Controllers
{
    public class BillController : UserSessionControllerBase
    {
        private readonly IBillService _billService;

        public BillController(
            IUserSessionService userSessionService,
            IBillService billService
            ) : base(userSessionService)
        {
            _billService = billService;
        }
    }
}
