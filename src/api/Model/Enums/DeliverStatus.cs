namespace PhoneStoreManager.Model.Enums
{
    public enum DeliverStatus
    {
        Failed = -2,
        Cancelled = -1,
        WaitingForPurchase = 0,
        WaitingForConfirm = 1,
        Preparing = 2,
        Delivering = 3,
        Completed = 4,
    }
}
