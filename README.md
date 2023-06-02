# Capstone Project

- Everything about my final project in DUT.
- Project name: **Phone store manager**.

# Used technologies

- Database: Microsoft SQL Server Local Database
- API: ASP.NET (based on .NET 6)
- Web: Spring Boot (Java)

# Used tools

- IDE: Visual Studio 2022 (API) & Visual Studio Code (Text editor - Web)
- SQL management: Azure Data Studio

# Roadmap

## Progress

| Group         | Components    | Start date    | Updated       | Progress          |
| -             | -             | -             | -             | -                 |
| [API](#api)   | [Services](#services-start-date-20230418), [Controllers](#controllers-start-date-20230424) | 2023/04/18    | 2023/06/02    | ![https://progress-bar.dev/80/?scale=100&width=120](https://progress-bar.dev/80/?scale=100&width=120) |
| [Web](#web)   | [Admin Page](#admin-page-start-date-20230516), [Auth Page](#auth-page-start-date-20230516) | 2023/05/16    | 2023/06/02    | ![https://progress-bar.dev/40/?scale=100&width=120](https://progress-bar.dev/40/?scale=100&width=120) |


## API

### Services (Start date: 2023/04/18)

| Group						| Description														| Updated		| Task (tick means completed)	|
| -							| -																	| -				| -								|
| Product Manuafaceturers	| Manage phone manufacturer.										| 2023/04/23	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li></ul> |
| Product Categories		| Manage phone category (tablet, smartphone, feature phone,...).	| 2023/04/23	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li></ul> |
| Product					| Manage phones in inventory.										| 2023/05/27	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Hide (or delete)</li><li>[x] Get (1 or list by query or all)</li></ul> |
| User						| Manage registered user, staff and administrator account.			| 2023/04/21	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Hide (or delete)</li><li>[x] Get (1 or list by query or all)</li><li>[x] Disable/Enable</li><li>[x] Change account type</li></ul> |
| User Address				| Let user manage delivery address.									| 2023/04/21	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li></ul> |
| User Session				| Manage logged in accounts.										| 2023/04/22	| <ul><li>[x] Add token</li><li>[x] Is Account locked</li><li>[x] Is enabled</li><li>[x] Log out by userId</li><li>[x] Get session by token</li><li>[x] Delete session by token</li></ul> |
| Warranty					| Manage all bill warranties.										| 2023/04/23	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li><li>[x] Check is expired</li></ul> |

[Go to progress](#progress)

### Controllers (Start date: 2023/04/24)

| Group						| Description															| Updated		| Task (tick means completed)	|
| -							| -																		| -				| -								|
| Products					| Manage phone categories, phone manufacturers and phone in intentory.	| 2023/05/20	| <ul><li>[x] Get</li><li>[x] Add (`**`)</li><li>[x] Modify (`**`)</li><li>[x] Delete or hide (`**`)</li></ul> |
| Users and user addresses	| Manage all users and user addresses (administrator account).			| 2023/05/20	| <ul><li>[x] Get (`*`)</li><li>[x] Add (`**`)</li><li>[x] Modify (`**`)</li><li>[x] Delete or hide (`**`)</li><li>[x] Enable/disable user (`**`)</li><li>[x] Change user type (`**`)</li><li>[x] Change password (`*`)</li></ul> |
| Login/Logout              | Account Authorization.                                                | 2023/06/02    | <ul><li>[x] Login</li><li>[x] Logout</li><li>[x] Register</li><li>[ ] Forgot your password?</li><li>[x] Include auth to Product/User controllers</li></ul> |
| Warranty                  | Manage all warranties.                                                | 2023/05/18    | <ul><li>[x] Get (all (`**`)/by ID(`*`))</li></ul> |

(`*`): Owner account or administrator only

(`**`): Administrator only

[Go to progress](#progress)

## Web

### Admin page (Start date: 2023/05/16)

| Group						| Description															| Updated		| Task (tick means completed)	|
| -							| -																		| -				| -								|
| Users                     | Manage user accounts.                                                 | 2023/05/24    | <ul><li>[x] View</li><li>[x] Add</li><li>[x] Update</li><li>[x] Disable/enable</li></ul> |
| Products                  | Manage all imported products (include category and manufacturer).     | 2023/05/29    | <ul><li>[x] View</li><li>[x] Add</li><li>[x] Update</li><li>[ ] Hide/show/delete</li></ul> |
| Bills                     | Manage all bills created by user when done submitting information.    | ----/--/--    | <ul><li>[ ] View</li><li>[ ] Mark as purchased</li><li>[ ] Mark as delivered</li><li>[ ] Update status</li></ul> |
| Warranties                | Manage all warranties.                                                | ----/--/--    | <ul><li>[ ] View</li><li>[ ] Add</li><li>[ ] Update</li><li>[ ] Mark as disabled</li></ul> |
| Statistics/Dashboard      | View all store states.                                                | ----/--/--    | <ul><li>[ ] View</li><li>[ ] List by date</li></ul> |

### Auth page (Start date: 2023/05/16)

| Group						| Description															| Updated		| Task (tick means completed)	|
| -							| -																		| -				| -								|
| Login                     | Login page.                                                           | 2023/05/24    | <ul><li>[x]</li></ul>         |
| Register                  | Register page.                                                        | 2023/06/02    | <ul><li>[x]</li></ul>         |


[Go to progress](#progress)
