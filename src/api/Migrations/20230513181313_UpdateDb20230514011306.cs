using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PhoneStoreManager.Migrations
{
    /// <inheritdoc />
    public partial class UpdateDb20230514011306 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Warranties_BillSummaries_BillID",
                table: "Warranties");

            migrationBuilder.AlterColumn<int>(
                name: "BillID",
                table: "Warranties",
                type: "int",
                nullable: true,
                oldClrType: typeof(int),
                oldType: "int");

            migrationBuilder.AddColumn<int>(
                name: "UserID",
                table: "Warranties",
                type: "int",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_Warranties_UserID",
                table: "Warranties",
                column: "UserID");

            migrationBuilder.AddForeignKey(
                name: "FK_Warranties_BillSummaries_BillID",
                table: "Warranties",
                column: "BillID",
                principalTable: "BillSummaries",
                principalColumn: "ID");

            migrationBuilder.AddForeignKey(
                name: "FK_Warranties_Users_UserID",
                table: "Warranties",
                column: "UserID",
                principalTable: "Users",
                principalColumn: "ID");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Warranties_BillSummaries_BillID",
                table: "Warranties");

            migrationBuilder.DropForeignKey(
                name: "FK_Warranties_Users_UserID",
                table: "Warranties");

            migrationBuilder.DropIndex(
                name: "IX_Warranties_UserID",
                table: "Warranties");

            migrationBuilder.DropColumn(
                name: "UserID",
                table: "Warranties");

            migrationBuilder.AlterColumn<int>(
                name: "BillID",
                table: "Warranties",
                type: "int",
                nullable: false,
                defaultValue: 0,
                oldClrType: typeof(int),
                oldType: "int",
                oldNullable: true);

            migrationBuilder.AddForeignKey(
                name: "FK_Warranties_BillSummaries_BillID",
                table: "Warranties",
                column: "BillID",
                principalTable: "BillSummaries",
                principalColumn: "ID",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
