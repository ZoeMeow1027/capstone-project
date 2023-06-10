namespace PhoneStoreManager.Services
{
    public interface IVariableService
    {
        void CreateAppDirIfNotExist();

        string GetAppDirPath(bool fullPath = true);

        string GetAvatarDirPath(bool fullPath = true);

        string GetAvatarFilePath(int userId, string? extBak = null, bool fullPath = true);

        string GetProductImageDirPath(bool fullPath = true);

        string GetProductImageFilePath(string fileName, bool fullPath = true);
    }
}
