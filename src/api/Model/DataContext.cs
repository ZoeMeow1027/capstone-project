using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;

namespace PhoneStoreManager.Model
{
    public class DataContext: DbContext
    {
        private string? _connectionString = null;

        public DataContext(string cString): base()
        {
            _connectionString = cString;
        }

        public DataContext(DbContextOptions<DataContext> options) : base(options) { }
        
        public DbSet<User> Users { get; set; }
        public DbSet<UserAddress> UserAddresses { get; set; }
        public DbSet<ProductManufacturer> ProductManufacturers { get; set; }
        public DbSet<ProductCategory> ProductCategories { get; set; }
        public DbSet<Product> Products { get; set; }
        public DbSet<BillSummary> BillSummaries { get; set; }
        public DbSet<BillDetails> BillDetails { get; set; }
        public DbSet<UserSession> UserSessions { get; set; }
        public DbSet<Warranty> Warranties { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (_connectionString != null)
            {
                optionsBuilder.UseSqlServer(_connectionString);
            }
            base.OnConfiguring(optionsBuilder);
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Foreign key UserID in User and UserAddress.
            modelBuilder.Entity<UserAddress>()
                .HasOne(p => p.User)
                .WithMany(c => c.UserAddresses)
                .HasForeignKey(p => p.UserID);

            // Username unique in User table.
            modelBuilder.Entity<User>(p =>
            {
                p.HasIndex(e => e.Username).IsUnique();
            });

            // Foreign key UserID in User and UserSession
            modelBuilder.Entity<UserSession>()
                .HasOne(p => p.User)
                .WithMany(c => c.UserSessions)
                .HasForeignKey(p => p.UserID);

            // Token unique in UserSession
            modelBuilder.Entity<UserSession>(p =>
            {
                p.HasIndex(e => e.Token).IsUnique();
            });

            // Foreign key CategoryID in Product and ProductCategory
            modelBuilder.Entity<Product>()
                .HasOne(p => p.Category)
                .WithMany(c => c.Products)
                .HasForeignKey(p => p.CategoryID);

            // Foreign key ManufacturerID in Product and ProductManufacturer
            modelBuilder.Entity<Product>()
                .HasOne(p => p.Manufacturer)
                .WithMany(c => c.Products)
                .HasForeignKey(p => p.ManufacturerID);

            // Foreign key UserID in User and BillSummary
            modelBuilder.Entity<BillSummary>()
                .HasOne(p => p.User)
                .WithMany(c => c.BillSummaries)
                .HasForeignKey(p => p.UserID);

            // Foreign key BillID in BillSummary and BillDetails
            modelBuilder.Entity<BillDetails>()
                .HasOne(p => p.BillSummary)
                .WithMany(c => c.BillDetails)
                .HasForeignKey(p => p.BillID);

            // Foreign key ProductID in BillDetails and Products
            modelBuilder.Entity<BillDetails>()
                .HasOne(p => p.Product)
                .WithMany(c => c.BillDetails)
                .HasForeignKey(p => p.ProductID);

            // Foreign key ProductID in Warranty and Products
            modelBuilder.Entity<Warranty>()
                .HasOne(p => p.Product)
                .WithMany(c => c.Warranties)
                .HasForeignKey(p => p.ProductID);

            // Foreign key BillID in Warranty and BillSummary
            modelBuilder.Entity<Warranty>()
                .HasOne(p => p.Bill)
                .WithMany(c => c.Warranties)
                .HasForeignKey(p => p.BillID);

            base.OnModelCreating(modelBuilder);
        }
    }
}
