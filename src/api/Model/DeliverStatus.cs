namespace PhoneStoreManager.Model
{
    public enum DeliverStatus
    {
        Failed = -2,
        Cancelled = -1,
        WaitingForConfirm = 0,
        Preparing = 1,
        Delivering = 2,
        Completed = 3,
    }
}
