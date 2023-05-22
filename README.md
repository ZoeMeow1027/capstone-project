# Capstone Project

- Everything about final project in DUT.

# Used technologies

- Database: Microsoft SQL Server Local Database
- API: ASP.NET based on .NET Core 6
- Web: Spring Boot

# Used tools

- IDE: Visual Studio 2022 (API) & Visual Studio Code (Text editor - Web)
- SQL Management: Azure Data Studio

# Roadmap

## API

### Services

- Start date: 2023/04/18
- Updated: 2023/04/23

| Group						| Description														| Updated		| Task (tick means completed)	|
| -							| -																	| -				| -								|
| Product Manuafaceturers	| Manage phone manufacturer.										| 2023/04/23	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li></ul> |
| Product Categories		| Manage phone category (tablet, smartphone, feature phone,...).	| 2023/04/23	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li></ul> |
| Product					| Manage phones in inventory.										| 2023/04/21	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Hide (or delete)</li><li>[x] Get (1 or list by query or all)</li></ul> |
| User						| Manage registered user, staff and administrator account.			| 2023/04/21	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Hide (or delete)</li><li>[x] Get (1 or list by query or all)</li><li>[x] Disable/Enable</li><li>[x] Change account type</li></ul> |
| User Address				| Let user manage delivery address.									| 2023/04/21	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li></ul> |
| User Session				| Manage logged in accounts.										| 2023/04/22	| <ul><li>[x] Add token</li><li>[x] Is Account locked</li><li>[x] Is enabled</li><li>[x] Log out by userId</li><li>[x] Get session by token</li><li>[x] Delete session by token</li></ul> |
| Warranty					| Manage all bill warranties.										| 2023/04/23	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li><li>[x] Check is expired</li></ul> |

### Controllers

- Start date: 2023/04/24
- Updated: 2023/05/20

| Group						| Description															| Updated		| Task (tick means completed)	|
| -							| -																		| -				| -								|
| Products					| Manage phone categories, phone manufacturers and phone in intentory.	| 2023/05/20	| <ul><li>[x] Get</li><li>[x] Add (`**`)</li><li>[x] Modify (`**`)</li><li>[x] Delete or hide (`**`)</li></ul> |
| Users and user addresses	| Manage all users and user addresses (administrator account).			| 2023/05/20	| <ul><li>[x] Get (`*`)</li><li>[x] Add (`**`)</li><li>[x] Modify (`**`)</li><li>[x] Delete or hide (`**`)</li><li>[x] Enable/disable user (`**`)</li><li>[x] Change user type (`**`)</li><li>[x] Change password (`*`)</li></ul> |
| Login/Logout              | Account Authorization.                                                | 2023/05/18    | <ul><li>[x] Login</li><li>[x] Logout</li><li>[x] Register</li><li>[ ] Forgot your password?</li><li>[x] Include auth to Product/User controllers</li></ul> |
| Warranty                  | Manage all warranties.                                                | 2023/05/18    | <ul><li>[x] Get (all (`**`)/by ID(`*`))</li></ul> |

(`*`): Owner account or administrator only

(`**`): Administrator only

## Web

- Start date: 2023/05/16
- Updated: 2023/05/23

| Group						| Description															| Updated		| Task (tick means completed)	|
| -							| -																		| -				| -								|
| Admin                     | Administrator page for manage everything on web.                      | 2023/05/23    | <ul><li>[ ] Dashboard</li><li>[ ] Products</li><li>[ ] Bills</li><li>[ ] Warranties</li><li>[ ] Statistics</li><li>[ ] Users</li></ul>

## Mobile (optional)

- Currently not started.
