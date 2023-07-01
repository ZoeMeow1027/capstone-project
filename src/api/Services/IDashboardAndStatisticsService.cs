using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public interface IDashboardAndStatisticsService
    {
        StatisticsByMonth GetStatisticsByMonth(int? year = null, int? month = null);

        Dashboard GetDashboard();
    }
}
