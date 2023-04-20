using Microsoft.EntityFrameworkCore;
using PhoneStoreManager.Model;

var builder = WebApplication.CreateBuilder(args);

//var dbContext = new DataContext(builder.Configuration.GetConnectionString("Default"));
//var data1 = dbContext.BillDetails;
//var data2 = dbContext.BillSummaries;
//var data3 = dbContext.Users;
//var data4 = dbContext.UserAddresses;
//var data5 = dbContext.ProductCategories;
//var data6 = dbContext.Products;
//var data7 = dbContext.ProductManufacturers;

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Add MSSQL
builder.Services.AddDbContext<DataContext>(options => options.UseSqlServer(builder.Configuration.GetConnectionString("Default")));

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
