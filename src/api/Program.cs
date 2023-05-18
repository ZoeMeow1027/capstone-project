using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;
using System.Diagnostics;

namespace PhoneStoreManager
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // Add services to the container.
            builder.Services.AddControllers();

            // Add scopes
            builder.Services.AddScoped<IProductManufacturerService, ProductManufacturerService>();
            builder.Services.AddScoped<IProductCategoryService, ProductCategoryService>();
            builder.Services.AddScoped<IProductService, ProductService>();
            builder.Services.AddScoped<IUserAddressService, UserAddressService>();
            builder.Services.AddScoped<IUserService, UserService>();
            builder.Services.AddScoped<IUserSessionService, UserSessionService>();
            builder.Services.AddScoped<IWarrantyService, WarrantyService>();

            // Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();

            // Add MSSQL
            builder.Services.AddDbContext<DataContext>(options => options.UseSqlServer(builder.Configuration.GetConnectionString("Default")));

            builder.Services.AddControllersWithViews()
                .AddNewtonsoftJson(options =>
                    options.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore
                );

            // Initialize Database and create admin account if not exist.
#pragma warning disable CS8604 // Possible null reference argument.
            if (builder.Configuration.GetConnectionString("Default") == null)
                throw new Exception("ConnectionString for connect to database hasn't been defined! Please config them in appsettings.json");

            using (var sqlDataContext = new DataContext(builder.Configuration.GetConnectionString("Default")))
            {
                sqlDataContext.Database.EnsureCreated();
                if (sqlDataContext.Users.Where(p => p.Username.ToLower() == "admin").FirstOrDefault() == null)
                {
                    Debug.WriteLine("Default admin account is not exist! Creating one...");
                    sqlDataContext.Users.Add(new User()
                    {
                        Username = "admin",
                        Password = "admin",
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
#pragma warning restore CS8604 // Possible null reference argument.

            // TODO: Temporary ignore CORS. Remember to delete below line in release
            #region Ignore CORS
            builder.Services.AddHttpContextAccessor();

            builder.Services.AddCors(o =>
                o.AddPolicy("CorsPolicy", builder =>
                {
                    builder.WithOrigins("http://localhost:4200")
                    .AllowAnyHeader()
                    .AllowAnyMethod();
                    builder.WithOrigins("https://localhost:4200")
                    .AllowAnyHeader()
                    .AllowAnyMethod();
                }
                    )
                );
            #endregion

            var app = builder.Build();

            // Configure the HTTP request pipeline.
            if (app.Environment.IsDevelopment())
            {
                app.UseSwagger();
                app.UseSwaggerUI();
            }

            app.UseHttpsRedirection();

            app.UseAuthorization();

            app.MapControllers();

            app.Run();
        }
    }
}