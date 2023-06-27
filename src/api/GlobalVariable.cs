namespace PhoneStoreManager
{
    public static class GlobalVariable
    {
        private static readonly string PATH_BASE = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile);
        private static readonly string PATH_PROJECTNAME = ".phonestore";
        private static readonly string PATH_DIR_AVATARNAME = "img_useravt";
        private static readonly string PATH_DIR_PRODUCTIMAGENAME = "img_product";

        public static void CreateAppDirIfNotExist()
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

        public static string GetAppDirPath(bool fullPath = true)
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

        public static string GetAvatarDirPath(bool fullPath = true)
        {
            return Path.Combine(new string[]
            {
                    GetAppDirPath(fullPath),
                    PATH_DIR_AVATARNAME
            });
        }

        public static string GetAvatarFilePath(int userId, string? extBak = null, bool fullPath = true)
        {
            return Path.Combine(new string[]
            {
                GetAvatarDirPath(fullPath),
                string.Format("{0}{1}", userId, extBak == null ? ".jpg" : extBak)
            });
        }

        public static string GetProductImageDirPath(bool fullPath = true)
        {
            return Path.Combine(new string[]
            {
                GetAppDirPath(fullPath),
                PATH_DIR_PRODUCTIMAGENAME
            });
        }

        public static string GetProductImageFilePath(string fileName, bool fullPath = true)
        {
            return Path.Combine(new string[]
            {
                GetProductImageDirPath(fullPath),
                fileName
            });
        }
    }
}
