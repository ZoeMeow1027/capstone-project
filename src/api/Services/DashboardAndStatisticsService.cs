using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.Enums;

namespace PhoneStoreManager.Services
{
    public class DashboardAndStatisticsService : IDashboardAndStatisticsService
    {
        private readonly DataContext _context;

        public DashboardAndStatisticsService(DataContext context)
        {
            this._context = context;
        }

        public StatisticsByMonth GetStatisticsByMonth(int? year = null, int? month = null)
        {
            int year1 = year != null ? year.Value : DateTime.UtcNow.Day;
            int month1 = month != null ? month.Value : DateTime.UtcNow.Month;

            DateTime dateTime = new DateTime(year1, month1, 1, 0, 0, 0, DateTimeKind.Utc);
            long startDate = new DateTimeOffset(dateTime).ToUnixTimeMilliseconds();
            long endDate = new DateTimeOffset(dateTime).AddMonths(1).AddTicks(-1).ToUnixTimeMilliseconds();
            var data = _context.BillSummaries.Where(p => (startDate <= p.DateCreated && p.DateCreated <= endDate))
                .OrderByDescending(p => p.TotalPrice).ToList();

            StatisticsByMonth result = new StatisticsByMonth()
            {
                YearFrom = year1,
                MonthFrom = month1,
                TotalPrice = data.Sum(p => p.TotalPrice),
                TotalDelivery = data.Count,
                CompletedDelivery = data.Count(p => p.Status == DeliverStatus.Completed),
            };
            result.CompletedPercent = (result.TotalDelivery == 0) ? 0 : ((double)result.CompletedDelivery * 100 / result.TotalDelivery);
            
            if (data.Count > 0)
            {
                result.BillSummary = data[0];
                result.MaxPriceDeliveryID = result.BillSummary.ID;
            }

            return result;
        }

        public Dashboard GetDashboard()
        {
            Dashboard result = new Dashboard();

            List<DeliverStatus> deliverStatusExclude = new List<DeliverStatus>()
                {
                    DeliverStatus.Failed,
                    DeliverStatus.Cancelled,
                    DeliverStatus.Completed
                };

            // TODO: Dashboard
            result.RegisteredMembers = _context.Users.Count(p => p.UserType == UserType.NormalUser);
            result.RemainingDeliveries = _context.BillSummaries.Count(p => !deliverStatusExclude.Contains(p.Status));

            List<int> productInMonth = _context.BillSummaries
                .Where(p => p.Status == DeliverStatus.Completed)
                .Select(p => p.ID)
                .ToList();

            var data = _context.BillDetails
                .Include(c => c.Product).ThenInclude(q => q.Images)
                .GroupBy(p => p.ProductID)
                .Select(p => new {
                    productid = p.Key,
                    count = p.Count()
                })
                .OrderByDescending(p => p.count);
            result.HotSellingProducts = data;

            return result;
        }
    }
}
