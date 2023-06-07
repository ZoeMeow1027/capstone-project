using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;
using PhoneStoreManager.Model.Enums;
using System.Diagnostics;

namespace PhoneStoreManager
{
    public static partial class Utils
    {
        public static void InitialDb(string cString)
        {
            using (var sqlDataContext = new DataContext(new DbContextOptionsBuilder<DataContext>().UseSqlServer(cString).Options))
            {
                sqlDataContext.Database.EnsureCreated();
                if (sqlDataContext.Users.Where(p => p.Username.ToLower() == "admin").FirstOrDefault() == null)
                {
                    Debug.WriteLine("Default admin account is not exist! Creating one...");
                    string pHash = Utils.RandomString(12);
                    sqlDataContext.Users.Add(new User()
                    {
                        Username = "admin",
                        PasswordHash = pHash,
                        Password = Utils.EncryptSHA256(string.Format("{0}{1}", "admin", pHash)),
                        Name = "Administrator",
                        DateCreated = DateTimeOffset.Now.ToUnixTimeMilliseconds(),
                        DateModified = DateTimeOffset.Now.ToUnixTimeMilliseconds(),
                        IsEnabled = true,
                        UserType = UserType.Administrator,
                    });
                    sqlDataContext.SaveChanges();
                    Debug.WriteLine("\nDone creating! Default username/password is admin/admin.");
                    Debug.WriteLine("Remember to change your password to enhance security.\n");
                }
            }
        }

        public static void InitialDbSqLite(string cString)
        {
            using (var sqlDataContext = new DataContext(new DbContextOptionsBuilder<DataContext>().UseSqlite(cString).Options))
            {
                sqlDataContext.Database.EnsureCreated();
                if (sqlDataContext.Users.Where(p => p.Username.ToLower() == "admin").FirstOrDefault() == null)
                {
                    Debug.WriteLine("Default admin account is not exist! Creating one...");
                    string pHash = Utils.RandomString(12);
                    sqlDataContext.Users.Add(new User()
                    {
                        Username = "admin",
                        PasswordHash = pHash,
                        Password = Utils.EncryptSHA256(string.Format("{0}{1}", "admin", pHash)),
                        Name = "Administrator",
                        DateCreated = DateTimeOffset.Now.ToUnixTimeMilliseconds(),
                        DateModified = DateTimeOffset.Now.ToUnixTimeMilliseconds(),
                        IsEnabled = true,
                        UserType = UserType.Administrator,
                    });
                    sqlDataContext.SaveChanges();
                    Debug.WriteLine("\nDone creating! Default username/password is admin/admin.");
                    Debug.WriteLine("Remember to change your password to enhance security.\n");
                }
            }
        }
    }
}
