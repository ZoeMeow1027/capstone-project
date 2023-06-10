namespace PhoneStoreManager.Services
{
    public class VariableService : IVariableService
    {
        private readonly string PATH_BASE;
        private readonly string PATH_PROJECTNAME;
        private readonly string PATH_DIR_AVATARNAME;
        private readonly string PATH_DIR_PRODUCTIMAGENAME;

        public VariableService()
        {
            PATH_BASE = Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData);
            PATH_PROJECTNAME = "PhoneStoreManager";
            PATH_DIR_AVATARNAME = "img_useravt";
            PATH_DIR_PRODUCTIMAGENAME = "img_product";
        }

        public void CreateAppDirIfNotExist()
        {
            if (!Directory.Exists(GetAppDirPath(true)))
            {
                Directory.CreateDirectory(GetAppDirPath(true));
            }
            if (!Directory.Exists(GetAvatarDirPath(true)))
            {
                Directory.CreateDirectory(GetAvatarDirPath(true));
            }
            if (!Directory.Exists(GetProductImageDirPath(true)))
            {
                Directory.CreateDirectory(GetProductImageDirPath(true));
            }
        }

        public string GetAppDirPath(bool fullPath = true)
        {
            if (!fullPath)
            {
                return "";
                //return Path.Combine(new string[]
                //{
                //    PATH_PROJECTNAME
                //});
            }
            else
            {
                return Path.Combine(new string[]
                {
                    PATH_BASE,
                    PATH_PROJECTNAME
                });
            }
        }

        public string GetAvatarDirPath(bool fullPath = true)
        {
            return Path.Combine(new string[]
            {
                    GetAppDirPath(fullPath),
                    PATH_DIR_AVATARNAME
            });
        }

        public string GetAvatarFilePath(int userId, string? extBak = null, bool fullPath = true)
        {
            return Path.Combine(new string[]
            {
                GetAvatarDirPath(fullPath),
                string.Format("{0}{1}", userId, extBak == null ? ".jpg" : extBak)
            });
        }

        public string GetProductImageDirPath(bool fullPath = true)
        {
            return Path.Combine(new string[]
            {
                GetAppDirPath(fullPath),
                PATH_DIR_PRODUCTIMAGENAME
            });
        }

        public string GetProductImageFilePath(string fileName, bool fullPath = true)
        {
            return Path.Combine(new string[]
            {
                GetProductImageDirPath(fullPath),
                fileName
            });
        }
    }
}
