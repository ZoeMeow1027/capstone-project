using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

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
            builder.Services.AddScoped<IVariableService, VariableService>();
            builder.Services.AddScoped<IProductManufacturerService, ProductManufacturerService>();
            builder.Services.AddScoped<IProductCategoryService, ProductCategoryService>();
            builder.Services.AddScoped<IProductService, ProductService>();
            builder.Services.AddScoped<IUserAddressService, UserAddressService>();
            builder.Services.AddScoped<IUserService, UserService>();
            builder.Services.AddScoped<IUserSessionService, UserSessionService>();
            builder.Services.AddScoped<IWarrantyService, WarrantyService>();
            builder.Services.AddScoped<IProductImageMetadataService, ProductImageMetadataService>();
            builder.Services.AddScoped<IUserCartService, UserCartService>();
            builder.Services.AddScoped<IUserAvatarService, UserAvatarService>();

            // Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();

            // Add MSSQL
            // builder.Services.AddDbContext<DataContext>(options => options.UseSqlServer(builder.Configuration.GetConnectionString("Default")));

            // Add SqLite
            string SQLITE_CSTRING = string.Format(
                "Filename={0}",
                System.IO.Path.Combine(new string[] { Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), "PhoneStoreManager", "data.db" })
                );
            builder.Services.AddDbContext<DataContext>(options => options.UseSqlite(SQLITE_CSTRING));

            builder.Services.AddControllersWithViews()
                .AddNewtonsoftJson(options =>
                    options.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore
                );

            // Initialize Database and create admin account if not exist.
#pragma warning disable CS8604 // Possible null reference argument.
            if (builder.Configuration.GetConnectionString("Default") == null)
            {
                throw new Exception("ConnectionString for connect to SQL Server database hasn't been defined! Please config them in appsettings.json");
            }
            else
            {
                // Utils.InitialDb(builder.Configuration.GetConnectionString("Default"));
                Utils.InitialDbSqLite(SQLITE_CSTRING);
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