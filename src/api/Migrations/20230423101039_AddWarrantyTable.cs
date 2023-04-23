using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PhoneStoreManager.Migrations
{
    /// <inheritdoc />
    public partial class AddWarrantyTable : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "ShowInPage",
                table: "ProductManufacturers");

            migrationBuilder.DropColumn(
                name: "ShowInPage",
                table: "ProductCategories");

            migrationBuilder.CreateTable(
                name: "Warranties",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    ProductID = table.Column<int>(type: "int", nullable: false),
                    BillID = table.Column<int>(type: "int", nullable: false),
                    WarrantyMonth = table.Column<int>(type: "int", nullable: false),
                    DateStart = table.Column<DateTime>(type: "datetime2", nullable: false),
                    DateEnd = table.Column<DateTime>(type: "datetime2", nullable: false),
                    SerialNumberOrIMEI = table.Column<string>(type: "nvarchar(max)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Warranties", x => x.ID);
                    table.ForeignKey(
                        name: "FK_Warranties_BillSummaries_BillID",
                        column: x => x.BillID,
                        principalTable: "BillSummaries",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Warranties_Products_ProductID",
                        column: x => x.ProductID,
                        principalTable: "Products",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Warranties_BillID",
                table: "Warranties",
                column: "BillID");

            migrationBuilder.CreateIndex(
                name: "IX_Warranties_ProductID",
                table: "Warranties",
                column: "ProductID");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Warranties");

            migrationBuilder.AddColumn<bool>(
                name: "ShowInPage",
                table: "ProductManufacturers",
                type: "bit",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<bool>(
                name: "ShowInPage",
                table: "ProductCategories",
                type: "bit",
                nullable: false,
                defaultValue: false);
        }
    }
}
