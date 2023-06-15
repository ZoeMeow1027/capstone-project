# Capstone Project

- Everything about my final project in DUT.
- Project name: **Phone store manager**.

# Used technologies

- Database: SQLite 3
- API: ASP.NET (based on .NET 6)
- Web: Spring Boot (Java)

# Used tools

- IDE: Visual Studio 2022 (API) & Visual Studio Code (Text editor - Web)
- SQL management: SQLite Studio

# Roadmap

## Progress

| Group         | Components    | Start date    | Updated       | Progress          |
| -             | -             | -             | -             | -                 |
| [API](#api)   | [Services](#services), [Controllers](#controllers) | 2023/04/18    | 2023/06/16    | ![https://progress-bar.dev/90/?scale=100&width=120](https://progress-bar.dev/90/?scale=100&width=120) |
| [Web](#web)   | [Admin Page](#admin-page), [Auth Page](#auth-page), [Main Page](#main-page) | 2023/05/16    | 2023/06/16    | ![https://progress-bar.dev/65/?scale=100&width=120](https://progress-bar.dev/65/?scale=100&width=120) |

## API

### Services

Start date: 2023/04/18

| Group						| Description														| Updated		| Task (tick means completed)	|
| -							| -																	| -				| -								|
| Product Manuafaceturers	| Manage product manufacturer.										| 2023/04/23	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li></ul> |
| Product Categories		| Manage product category (tablet, smartphone, feature phone,...).	| 2023/04/23	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li></ul> |
| Product					| Manage product in inventory.										| 2023/05/27	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Hide (or delete)</li><li>[x] Get (1 or list by query or all)</li></ul> |
| Product Images			| Manage product images per product.    							| 2023/06/12	| <ul><li>[x] Add</li><li>[x] Get image blob</li><li>[x] Get image metadata</li><li>[x] Delete image</li></ul> |
| User						| Manage registered user, staff and administrator account.			| 2023/04/21	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Hide (or delete)</li><li>[x] Get (1 or list by query or all)</li><li>[x] Disable/Enable</li><li>[x] Change account type</li></ul> |
| User Avatar			    | Manage user avatar.										        | 2023/06/11	| <ul><li>[x] Set avatar</li><li>[x] Get avatar</li><li>[x] Get all avatars</li><li>[x] Remove avatar</li></ul> |
| User Address				| Let user manage delivery address.									| 2023/04/21	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li></ul> |
| User Session				| Manage logged in accounts.										| 2023/04/22	| <ul><li>[x] Add token</li><li>[x] Is Account locked</li><li>[x] Is enabled</li><li>[x] Log out by userId</li><li>[x] Get session by token</li><li>[x] Delete session by token</li></ul> |
| User Cart				    | Manage user carts.        										| 2023/06/06	| <ul><li>[x] Add item</li><li>[x] Subtract item</li><li>[x] Remove item</li><li>[x] Get all items</li></ul> |
| Warranty					| Manage all bill warranties.										| 2023/04/23	| <ul><li>[x] Add</li><li>[x] Update</li><li>[x] Delete</li><li>[x] Get (1 or list by query or all)</li><li>[x] Check is expired</li></ul> |

[Go to progress](#progress)

### Controllers

Start date: 2023/04/24

| Group						| Description															| Updated		| Task (tick means completed)	|
| -							| -																		| -				| -								|
| Products					| Manage phone categories, phone manufacturers and phone in intentory.	| 2023/06/16	| <ul><li>[x] Get</li><li>[x] Add (`**`)</li><li>[x] Modify (`**`)</li><li>[x] Delete or hide (`**`)</li></ul> |
| Users and user addresses	| Manage all users and user addresses (administrator account).			| 2023/05/20	| <ul><li>[x] Get (`*`)</li><li>[x] Add (`**`)</li><li>[x] Modify (`**`)</li><li>[x] Delete or hide (`**`)</li><li>[x] Enable/disable user (`**`)</li><li>[x] Change user type (`**`)</li><li>[x] Change password (`*`)</li></ul> |
| Login/Logout              | Account Authorization.                                                | 2023/06/02    | <ul><li>[x] Login</li><li>[x] Logout</li><li>[x] Register</li><li>[ ] Forgot your password?</li><li>[x] Include auth to Product/User controllers</li></ul> |
| Cart                      | User cart.                                                            | 2023/06/06    | <ul><li>[x] Get all items</li><li>[ ] Add item</li><li>[ ] Subtract item</li><li>[ ] Remove item</li><li>[ ] Remove all</li></ul> |
| Warranty                  | Manage all warranties.                                                | 2023/05/18    | <ul><li>[x] Get (all (`**`)/by ID(`*`))</li></ul> |
| Account                   | Logged in account information.                                        | 2023/06/07    | <ul><li>[x] My information</li><li>[x] Avatar (get/set/remove)</li><li>[x] User address</li><li>[x] Change Password</li></ul> |


(`*`): Owner account or administrator only

(`**`): Administrator only

[Go to progress](#progress)

## Web

### Admin page

Start date: 2023/05/16

| Group						| Description															| Updated		| Task (tick means completed)	|
| -							| -																		| -				| -								|
| Users                     | Manage user accounts.                                                 | 2023/05/24    | <ul><li>[x] View</li><li>[x] Add</li><li>[x] Update</li><li>[x] Disable/enable</li></ul> |
| Products                  | Manage all imported products (include category and manufacturer).     | 2023/05/29    | <ul><li>[x] View</li><li>[x] Add</li><li>[x] Update</li><li>[ ] Hide/show/delete</li></ul> |
| Bills                     | Manage all bills created by user when done submitting information.    | ----/--/--    | <ul><li>[ ] View</li><li>[ ] Mark as purchased</li><li>[ ] Mark as delivered</li><li>[ ] Update status</li></ul> |
| Warranties                | Manage all warranties.                                                | ----/--/--    | <ul><li>[ ] View</li><li>[ ] Add</li><li>[ ] Update</li><li>[ ] Mark as disabled</li></ul> |
| Statistics/Dashboard      | View all store states.                                                | ----/--/--    | <ul><li>[ ] View</li><li>[ ] List by date</li></ul> |

[Go to progress](#progress)

### Auth page

Start date: 2023/05/16

| Group						| Description															| Updated		| Task (tick means completed)	|
| -							| -																		| -				| -								|
| Login                     | Login page.                                                           | 2023/05/24    | <ul><li>[x]</li></ul>         |
| Register                  | Register page.                                                        | 2023/06/02    | <ul><li>[x]</li></ul>         |

[Go to progress](#progress)

### Main page

Start date: 2023/06/03

| Group						| Description															| Updated		| Task (tick means completed)	|
| -							| -																		| -				| -								|
| Main page                 | Home web page                                                         | 2023/06/03    | <ul><li>[ ]</li></ul> |
| Search page               | Search result when you return a search query                          | 2023/06/16    | <ul><li>[x] View</li><li>[x] Navigate to product detail</li><li>[x] Product image</li></ul> |
| Product detail            | Detail of a product (specifications, price, image preview, ...)       | 2023/06/16    | <ul><li>[x] View</li><li>[ ] Add to cart</li><li>[ ] Comments</li></ul> |
| Your profile              | Edit your basic profile                                               | 2023/06/13    | <ul><li>[x] View</li><li>[x] Profile data</li><li>[x] Save changes</li><li>[x] Get/set avatar</li></ul> |
| Your address              | Edit your added address                                               | 2023/06/13    | <ul><li>[x] View</li><li>[ ] Add</li><li>[ ] Update</li><li>[ ] Delete</li></ul> |
| Change your password      | Change your password                                                  | 2023/06/13    | <ul><li>[x] View</li><li>[x] Change password</li></ul> |
| Your cart                 | Manage your cart                                                      | 2023/06/05    | <ul><li>[x] View</li><li>[ ] List items in cart</li><li>[ ] Go to checkout</li></ul> |

[Go to progress](#progress)
