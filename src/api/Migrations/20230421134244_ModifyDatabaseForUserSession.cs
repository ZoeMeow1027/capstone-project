using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PhoneStoreManager.Migrations
{
    /// <inheritdoc />
    public partial class ModifyDatabaseForUserSession : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserSession_Users_UserID",
                table: "UserSession");

            migrationBuilder.DropPrimaryKey(
                name: "PK_UserSession",
                table: "UserSession");

            migrationBuilder.RenameTable(
                name: "UserSession",
                newName: "UserSessions");

            migrationBuilder.RenameIndex(
                name: "IX_UserSession_UserID",
                table: "UserSessions",
                newName: "IX_UserSessions_UserID");

            migrationBuilder.AddPrimaryKey(
                name: "PK_UserSessions",
                table: "UserSessions",
                column: "ID");

            migrationBuilder.AddForeignKey(
                name: "FK_UserSessions_Users_UserID",
                table: "UserSessions",
                column: "UserID",
                principalTable: "Users",
                principalColumn: "ID",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserSessions_Users_UserID",
                table: "UserSessions");

            migrationBuilder.DropPrimaryKey(
                name: "PK_UserSessions",
                table: "UserSessions");

            migrationBuilder.RenameTable(
                name: "UserSessions",
                newName: "UserSession");

            migrationBuilder.RenameIndex(
                name: "IX_UserSessions_UserID",
                table: "UserSession",
                newName: "IX_UserSession_UserID");

            migrationBuilder.AddPrimaryKey(
                name: "PK_UserSession",
                table: "UserSession",
                column: "ID");

            migrationBuilder.AddForeignKey(
                name: "FK_UserSession_Users_UserID",
                table: "UserSession",
                column: "UserID",
                principalTable: "Users",
                principalColumn: "ID",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
