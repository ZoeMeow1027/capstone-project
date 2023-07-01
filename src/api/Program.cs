using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;
using PhoneStoreManager.Services;

namespace PhoneStoreManager
{
    public class Program
    {
        public static void Main(string[] args)
        {
            GlobalVariable.CreateAppDirIfNotExist();

            string SQLITE_CSTRING = string.Format(
                "Filename={0};Mode=ReadWriteCreate;",
                Path.Combine(new string[] { GlobalVariable.GetAppDirPath(), "data.db" })
                );

            // Initialize Database and create admin account if not exist.
#pragma warning disable CS8604 // Possible null reference argument.
            bool isInit = true;
            if (isInit)
            {
                Utils.InitialDbSqLite(SQLITE_CSTRING);
                //if (builder.Configuration.GetConnectionString("Default") == null)
                //{
                //    throw new Exception("ConnectionString for connect to SQLite database hasn't been defined! Please config them in appsettings.json");
                //}
                //else
                //{
                //    // Utils.InitialDb(builder.Configuration.GetConnectionString("Default"));
                //}
            }
#pragma warning restore CS8604 // Possible null reference argument.

            var builder = WebApplication.CreateBuilder(args);

            // Add MSSQL
            // builder.Services.AddDbContext<DataContext>(options => options.UseSqlServer(builder.Configuration.GetConnectionString("Default")));

            // Add SqLite
            builder.Services.AddDbContext<DataContext>(options => options.UseSqlite(SQLITE_CSTRING));

            builder.Services.AddControllersWithViews()
                .AddNewtonsoftJson(options =>
                    options.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore
                );

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
            builder.Services.AddScoped<IProductImageMetadataService, ProductImageMetadataService>();
            builder.Services.AddScoped<IUserCartService, UserCartService>();
            builder.Services.AddScoped<IUserAvatarService, UserAvatarService>();
            builder.Services.AddScoped<IBillService, BillService>();
            builder.Services.AddScoped<IDashboardAndStatisticsService, DashboardAndStatisticsService>();

            // Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();

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

            //app.UseHttpsRedirection();

            app.UseAuthorization();

            app.MapControllers();

            app.Run();
        }
    }
}