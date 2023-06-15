using Microsoft.EntityFrameworkCore;

namespace PhoneStoreManager.Model
{
    public class DataContext : DbContext
    {
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.
        public DataContext(DbContextOptions<DataContext> options) : base(options)
        {

        }
#pragma warning restore CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider declaring as nullable.

        public DbSet<User> Users { get; set; }
        public DbSet<UserAddress> UserAddresses { get; set; }
        public DbSet<ProductManufacturer> ProductManufacturers { get; set; }
        public DbSet<ProductCategory> ProductCategories { get; set; }
        public DbSet<Product> Products { get; set; }
        public DbSet<BillSummary> BillSummaries { get; set; }
        public DbSet<BillDetails> BillDetails { get; set; }
        public DbSet<UserSession> UserSessions { get; set; }
        public DbSet<Warranty> Warranties { get; set; }
        public DbSet<ProductImageMetadata> ImageMetadatas { get; set; }
        public DbSet<UserCart> UserCarts { get; set; }
        public DbSet<ProductComment> ProductComments { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            base.OnConfiguring(optionsBuilder);
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Foreign key UserID in User and UserAddress.
            modelBuilder.Entity<UserAddress>()
                .HasOne(p => p.User)
                .WithMany(c => c.UserAddresses)
                .HasForeignKey(p => p.UserID)
                .HasPrincipalKey(c => c.ID);

            // Username unique in User table.
            modelBuilder.Entity<User>(p =>
            {
                p.HasIndex(e => e.Username).IsUnique();
            });

            // Foreign key UserID in User and BillSummary
            modelBuilder.Entity<BillSummary>()
                .HasOne(p => p.User)
                .WithMany(c => c.BillSummaries)
                .HasForeignKey(p => p.UserID)
                .HasPrincipalKey(c => c.ID);

            // Foreign key ProductID in ProductImageMetadata and Product
            modelBuilder.Entity<ProductImageMetadata>()
                .HasOne(p => p.Product)
                .WithMany(c => c.Images)
                .HasForeignKey(p => p.ProductID)
                .HasPrincipalKey(c => c.ID);

            #region ProductComment table
            // Foreign key UserID in ProductComments and User
            modelBuilder.Entity<ProductComment>()
                .HasOne(p => p.User)
                .WithMany(c => c.ProductComments)
                .HasForeignKey(p => p.UserID)
                .HasPrincipalKey(c => c.ID);

            // Foreign key ProductID in ProductComments and Product
            modelBuilder.Entity<ProductComment>()
                .HasOne(p => p.Product)
                .WithMany(c => c.Comments)
                .HasForeignKey(p => p.ProductID)
                .HasPrincipalKey(c => c.ID);
            #endregion

            #region UserSession table
            // Foreign key UserID in User and UserSession
            modelBuilder.Entity<UserSession>()
                .HasOne(p => p.User)
                .WithMany(c => c.UserSessions)
                .HasForeignKey(p => p.UserID)
                .HasPrincipalKey(c => c.ID);

            // Token unique in UserSession
            modelBuilder.Entity<UserSession>(p =>
            {
                p.HasIndex(e => e.Token).IsUnique();
            });
            #endregion

            #region Product table
            // Foreign key CategoryID in Product and ProductCategory
            modelBuilder.Entity<Product>()
                .HasOne(p => p.Category)
                .WithMany(c => c.Products)
                .HasForeignKey(p => p.CategoryID)
                .HasPrincipalKey(c => c.ID);

            // Foreign key ManufacturerID in Product and ProductManufacturer
            modelBuilder.Entity<Product>()
                .HasOne(p => p.Manufacturer)
                .WithMany(c => c.Products)
                .HasForeignKey(p => p.ManufacturerID)
                .HasPrincipalKey(c => c.ID);
            #endregion

            #region BillDetails table
            // Foreign key BillID in BillSummary and BillDetails
            modelBuilder.Entity<BillDetails>()
                .HasOne(p => p.BillSummary)
                .WithMany(c => c.BillDetails)
                .HasForeignKey(p => p.BillID)
                .HasPrincipalKey(c => c.ID);

            // Foreign key ProductID in BillDetails and Products
            modelBuilder.Entity<BillDetails>()
                .HasOne(p => p.Product)
                .WithMany(c => c.BillDetails)
                .HasForeignKey(p => p.ProductID)
                .HasPrincipalKey(c => c.ID);
            #endregion

            #region Warranty table
            // Foreign key ProductID in Warranty and Products
            modelBuilder.Entity<Warranty>()
                .HasOne(p => p.Product)
                .WithMany(c => c.Warranties)
                .HasForeignKey(p => p.ProductID)
                .HasPrincipalKey(c => c.ID);

            // Foreign key BillID in Warranty and BillSummary
            modelBuilder.Entity<Warranty>()
                .HasOne(p => p.Bill)
                .WithMany(c => c.Warranties)
                .HasForeignKey(p => p.BillID)
                .HasPrincipalKey(c => c.ID);

            // Foreign key UserID in Warranty and User
            modelBuilder.Entity<Warranty>()
                .HasOne(p => p.User)
                .WithMany(c => c.Warranties)
                .HasForeignKey(p => p.UserID)
                .HasPrincipalKey(c => c.ID);
            #endregion

            #region UserCart table
            // Foreign key UserID in table User
            modelBuilder.Entity<UserCart>()
                .HasOne(p => p.User)
                .WithMany(c => c.UserCart)
                .HasForeignKey(p => p.UserID)
                .HasPrincipalKey(c => c.ID);

            // Foreign key ProductID in table Product
            modelBuilder.Entity<UserCart>()
                .HasOne(p => p.Product)
                .WithOne(c => c.UserCart)
                .HasForeignKey<UserCart>(p => p.ProductID)
                .HasPrincipalKey<Product>(c => c.ID);
            #endregion

            base.OnModelCreating(modelBuilder);
        }
    }
}
