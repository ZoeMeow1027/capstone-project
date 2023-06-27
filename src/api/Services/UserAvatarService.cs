using PhoneStoreManager.Model;

namespace PhoneStoreManager.Services
{
    public class UserAvatarService : IUserAvatarService
    {
        public UserAvatarService()
        {

        }

        public byte[]? GetAvatar(int userid)
        {
            string PATH_FULLAVT = GlobalVariable.GetAvatarFilePath(userid);

            if (!File.Exists(PATH_FULLAVT))
                return null;

            return File.ReadAllBytes(PATH_FULLAVT);
        }

        public byte[]? GetAvatar(User user)
        {
            return GetAvatar(user.ID);
        }

        public void RemoveAvatar(int userid)
        {
            string PATH_FULLAVT = GlobalVariable.GetAvatarFilePath(userid);

            try
            {
                if (File.Exists(PATH_FULLAVT))
                {
                    File.Delete(PATH_FULLAVT);
                }
            }
            catch { }
        }

        public void RemoveAvatar(User user)
        {
            RemoveAvatar(user.ID);
        }

        public void SetAvatar(int userid, IFormFile avatar)
        {
            string PATH_FULLAVT = GlobalVariable.GetAvatarFilePath(userid);
            string PATH_FULLAVT_BAK = GlobalVariable.GetAvatarFilePath(userid, ".jpg.bak");

            bool OVERWRITE_MODE = File.Exists(PATH_FULLAVT);

            void SaveImageToPath(IFormFile avatar, string path, int limitPx = 1024)
            {
                Image img = Image.Load(avatar.OpenReadStream());
                int width = 0, height = 0;
                if (img.Size.Width > limitPx || img.Size.Height > limitPx)
                {
                    double ratio = img.Width / img.Height;
                    if (img.Width > img.Height)
                    {
                        width = limitPx;
                        height = (int)(width * (1.0 / ratio));
                    }
                    else if (img.Width < img.Height)
                    {
                        height = limitPx;
                        width = (int)(height * ratio);
                    }
                    else
                    {
                        width = limitPx;
                        height = limitPx;
                    }
                }
                else
                {
                    width = limitPx;
                    height = limitPx;
                }
                img.Mutate(p => p.Resize(width, height));
                img.SaveAsJpeg(path);
            }

            try
            {
                if (!OVERWRITE_MODE)
                {
                    SaveImageToPath(avatar, PATH_FULLAVT);
                }
                else
                {
                    SaveImageToPath(avatar, PATH_FULLAVT_BAK);
                    File.Delete(PATH_FULLAVT);
                    File.Move(PATH_FULLAVT_BAK, PATH_FULLAVT);
                }
            }
            catch
            {
                if (!OVERWRITE_MODE) { File.Delete(PATH_FULLAVT); }
                else { File.Delete(PATH_FULLAVT_BAK); }

                throw;
            }
        }

        public void SetAvatar(User user, IFormFile avatar)
        {
            SetAvatar(user.ID, avatar);
        }
    }
}
