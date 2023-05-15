using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PhoneStoreManager.Migrations
{
    /// <inheritdoc />
    public partial class UpdateDb20230514165044 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "WarrantyDisabled",
                table: "Warranties",
                type: "bit",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<string>(
                name: "WarrantyDisabledReason",
                table: "Warranties",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "WarrantyDisabled",
                table: "Warranties");

            migrationBuilder.DropColumn(
                name: "WarrantyDisabledReason",
                table: "Warranties");
        }
    }
}
