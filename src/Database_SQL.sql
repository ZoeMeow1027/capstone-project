--
-- File generated with SQLiteStudio v3.4.4 on Sun Jul 2 12:26:01 2023
--
-- Text encoding used: UTF-8
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: __EFMigrationsHistory
CREATE TABLE IF NOT EXISTS __EFMigrationsHistory (
    MigrationId    TEXT NOT NULL
                        CONSTRAINT PK___EFMigrationsHistory PRIMARY KEY,
    ProductVersion TEXT NOT NULL
);

INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230615111111_UpdateDb20230615181105', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230616134352_UpdateDb20230616204336', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230617104753_UpdateDb20230617174740', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230617135336_UpdateDb20230617205321', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230618090200_UpdateDb20230618160149', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230618091407_UpdateDb20230618161358', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230618092034_UpdateDb20230618162029', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230618115329_UpdateDb20230618185318', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230619060240_UpdateDb20230619130225', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230619060320_UpdateDb20230619130314', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230619170355_UpdateDb20230620000343', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230623132035_UpdateDb20230623202016', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230624090310_UpdateDb20230624160240', '7.0.5');
INSERT INTO __EFMigrationsHistory (MigrationId, ProductVersion) VALUES ('20230624091656_UpdateDb20230624161642', '7.0.5');

-- Table: BillDetails
CREATE TABLE IF NOT EXISTS BillDetails (
    ID        INTEGER NOT NULL
                      CONSTRAINT PK_BillDetails PRIMARY KEY AUTOINCREMENT,
    BillID    INTEGER NOT NULL,
    Count     INTEGER NOT NULL,
    ProductID INTEGER NOT NULL,
    CONSTRAINT FK_BillDetails_BillSummary_BillID FOREIGN KEY (
        BillID
    )
    REFERENCES BillSummary (ID) ON DELETE CASCADE,
    CONSTRAINT FK_BillDetails_Product_ProductID FOREIGN KEY (
        ProductID
    )
    REFERENCES Product (ID) ON DELETE CASCADE
);

INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (4, 4, 2, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (5, 5, 2, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (6, 6, 2, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (7, 6, 1, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (8, 7, 2, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (9, 7, 1, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (10, 8, 1, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (11, 8, 1, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (12, 9, 1, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (13, 9, 1, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (14, 10, 1, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (15, 10, 1, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (16, 11, 1, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (17, 11, 1, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (18, 12, 1, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (19, 12, 3, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (20, 13, 3, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (21, 13, 3, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (22, 14, 2, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (23, 14, 2, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (24, 15, 2, 1);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (25, 15, 2, 4);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (26, 16, 1, 2);
INSERT INTO BillDetails (ID, BillID, Count, ProductID) VALUES (27, 16, 5, 5);

-- Table: BillSummary
CREATE TABLE IF NOT EXISTS BillSummary (
    ID                   INTEGER NOT NULL
                                 CONSTRAINT PK_BillSummary PRIMARY KEY AUTOINCREMENT,
    DateCompleted        INTEGER,
    DateCreated          INTEGER NOT NULL,
    PaymentCompleted     INTEGER NOT NULL,
    PaymentID            TEXT,
    PaymentMethod        INTEGER NOT NULL,
    PaymentMethodName    TEXT,
    Recipient            TEXT    NOT NULL,
    RecipientAddress     TEXT    NOT NULL,
    Status               INTEGER NOT NULL,
    StatusAdditional     TEXT,
    StatusAddress        TEXT,
    TotalPrice           REAL    NOT NULL,
    UserID               INTEGER NOT NULL,
    UserMessage          TEXT,
    VAT                  REAL    NOT NULL,
    RecipientPhone       TEXT    NOT NULL
                                 DEFAULT '',
    ShippingPrice        REAL    NOT NULL
                                 DEFAULT 0.0,
    Discount             REAL    NOT NULL
                                 DEFAULT 0.0,
    RecipientCountryCode TEXT    NOT NULL
                                 DEFAULT '',
    CONSTRAINT FK_BillSummary_User_UserID FOREIGN KEY (
        UserID
    )
    REFERENCES User (ID) ON DELETE CASCADE
);

INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (4, NULL, 1687085252418, 0, NULL, -99, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', -1, 'An admin or staff has cancelled this order.', '', 2297.1, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (5, NULL, 1687104433597, 0, NULL, -99, NULL, 'Quế Tùng', '247 đường Phan Bội Châu, phường Hòa Hiếu, tx. Thái Hòa, Nghệ An', -1, 'User has cancelled this order.', '', 2297.1, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (6, 1687192949774, 1687106197355, 0, NULL, 0, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', 4, '', '', 3104.1499999999996, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (7, NULL, 1687186827124, 0, NULL, -99, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', -1, 'User has cancelled this order.', '', 3104.1499999999996, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (8, NULL, 1687186955098, 0, NULL, -99, NULL, 'Quế Tùng', '247 đường Phan Bội Châu, phường Hòa Hiếu, tx. Thái Hòa, Nghệ An', -1, 'User has cancelled this order.', '', 1957.1, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (9, NULL, 1687187049541, 0, NULL, -99, NULL, 'Quế Tùng', '247 đường Phan Bội Châu, phường Hòa Hiếu, tx. Thái Hòa, Nghệ An', -1, 'User has cancelled this order.', '', 1957.1, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (10, NULL, 1687187253032, 0, NULL, -99, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', -1, 'User has cancelled this order.', '', 1957.1, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (11, NULL, 1687187471653, 0, NULL, -99, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', -1, 'User has cancelled this order.', '', 1957.1, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (12, NULL, 1687187992643, 0, NULL, -99, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', -1, 'User has cancelled this order.', '', 3571.2, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (13, NULL, 1687188195663, 0, NULL, -99, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', -1, 'User has cancelled this order.', '', 5865.299999999999, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (14, NULL, 1687188257279, 0, NULL, -99, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', -1, 'User has cancelled this order.', '', 3911.2, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (15, NULL, 1687188350060, 1, '6UU738854T034801R', 3, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', 1, 'User has completed this order payment.', '', 3911.2, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');
INSERT INTO BillSummary (ID, DateCompleted, DateCreated, PaymentCompleted, PaymentID, PaymentMethod, PaymentMethodName, Recipient, RecipientAddress, Status, StatusAdditional, StatusAddress, TotalPrice, UserID, UserMessage, VAT, RecipientPhone, ShippingPrice, Discount, RecipientCountryCode) VALUES (16, NULL, 1687231647887, 1, '', 0, NULL, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', 1, 'User has completed this order payment.', '', 3612.84, 3, NULL, 0.0, '0987654321', 3.0, 0.0, 'VN');

-- Table: Product
CREATE TABLE IF NOT EXISTS Product (
    ID             INTEGER NOT NULL
                           CONSTRAINT PK_Product PRIMARY KEY AUTOINCREMENT,
    CategoryID     INTEGER NOT NULL,
    DateCreated    INTEGER NOT NULL,
    DateModified   INTEGER NOT NULL,
    InventoryCount INTEGER NOT NULL,
    ManufacturerID INTEGER NOT NULL,
    Specification  TEXT,
    Name           TEXT    NOT NULL,
    Price          REAL    NOT NULL,
    ShowInPage     INTEGER NOT NULL,
    Unit           TEXT    NOT NULL,
    WarrantyMonth  INTEGER NOT NULL,
    Article        TEXT,
    Views          INTEGER NOT NULL
                           DEFAULT 0,
    CONSTRAINT FK_Product_ProductCategory_CategoryID FOREIGN KEY (
        CategoryID
    )
    REFERENCES ProductCategory (ID) ON DELETE CASCADE,
    CONSTRAINT FK_Product_ProductManufacturer_ManufacturerID FOREIGN KEY (
        ManufacturerID
    )
    REFERENCES ProductManufacturer (ID) ON DELETE CASCADE
);

INSERT INTO Product (ID, CategoryID, DateCreated, DateModified, InventoryCount, ManufacturerID, Specification, Name, Price, ShowInPage, Unit, WarrantyMonth, Article, Views) VALUES (1, 1, 1686071371939, 1687677999772, 299, 1, 'For full specification, please click [**here**](https://www.gsmarena.com/samsung_galaxy_s23_ultra-12024.php).

| Product Specification | |
| - | - |
| Display resolution & size | 6.8 inches, 1440 x 3088 pixels (QHD+) |
| Display features | 120Hz, HDR10+, 1750 nits, Gorilla Glass Victus 2 |
| Display technologies | Dynamic AMOLED 2X |
| OS | Android |
| Real Camera | Ultrawide: 12MP F2.2 (Dual Pixel AF)<br>Wide: 200MP F1.7 OIS ±3° (Super Quad Pixel AF)<br>Tele 1: 10MP F4.9 (10X, Dual Pixel AF) OIS<br>Tele 2: 10MP F2.4 (3X, Dual Pixel AF) OIS Zoom 100X |
| Front Camera | 12MP F2.2 (Dual Pixel AF) |
| RAM | 8 GB |
| Storage | 256 GB |
| SIM Card | 2 Nano SIM or 1 Nano + 1 eSIM |
| Pen | S-Pen |
| Battery | 5.000mAh |
| Chipset | Snapdragon 8 Gen 2 (4 nm) |', 'Galaxy S23 Ultra', 1147.0, 1, 'item', 12, '## Điện thoại Samsung Galaxy S23 Ultra liệu có gì mới?
Samsung S23 Ultra là dòng điện thoại cao cấp của Samsung, sở hữu camera độ phân giải 200MP ấn tượng, chip Snapdragon 8 Gen 2 mạnh mẽ, bộ nhớ RAM 8GB mang lại hiệu suất xử lý vượt trội cùng khung viền vuông vức sang trọng. Sản phẩm được ra mắt từ đầu năm 2023.

Thiết kế cao cấp, đường nét thời thượng, tinh tế
Tiếp nối thiết kế từ những chiếc Samsung Galaxy S22 Ultra, những chiếc Samsung S23 Ultra mang dáng vẻ thanh mảnh với những đường nét được gọt giũa chỉnh chu và cực kỳ tinh tế. Với màn hình tràn viền, tỷ lệ màn hình trên thân máy hơn 90% những chiếc điện thoại S23 Ultra hứa hẹn mang đến một thiết kế thời thượng thu hút mọi ánh nhìn.

## Thiết kế Samsung Galaxy S23 Ultra

Vẫn là mặt lưng nguyên khối cùng cụm camera không viền được đặt ở góc trái trên cùng logo Samsung quen thuộc nằm góc dưới mặt lưng tạo cảm giác đơn giản nhưng không kém phần nổi bật cho thiết kế. Thanh lịch nhưng đặc biệt có sức hút, đơn giản mà toát lên sự cao cấp, những chiếc Samsung S23 Ultra chắc chắn là sự lựa chọn hoàn hảo khi bình chọn những thiết kế đỉnh cao trong năm 2023.

Diện mạo của những chiếc Samsung Galaxy S23 Ultra có khả năng thu hút bất tận với chuỗi màu sắc đa dạng mà không kém phần thanh lịch, dòng điện thoại này ngay lập tức tạo nên định nghĩa vẻ đẹp công nghệ mới cho người dùng ngay khi chạm mắt.

Bên cạnh đó, Samsung còn sử dụng vật liệu tái chế thân thiện với môi trường trên S23 Ultra. Theo đó các lớp kính phủ đến màu sơn đều là những điểm nhấn độc đáo trên mẫu flagship này.

Chipset Snapdragon 8 Gen 2, khả năng đa nhiệm đỉnh cao
Để phục vụ tốt nhu cầu đa nhiệm ngày càng nhiều của người tiêu dùng, những chiếc điện thoại Samsung ngày càng sở hữu cho mình chipset khủng và khả năng xử lý mạnh mẽ. Những chiến binh Samsung S23 Ultra cũng không nằm ngoài xu hướng và để đảm bảo khả năng xử lý cho mình, Samsung Galaxy S23 Ultra sử dụng chipset Snapdragon 8 Gen 2 Mobile Platform for Galaxy. Chipset này hứa hẹn mang đến khả năng xử lý mạnh mẽ cân mọi thao tác đặc biệt có thể chạy nền cùng lúc nhiều ứng dụng mà không gây giật lag.

## Hiệu năng Samsung Galaxy S23 Ultra

Ram 8GB đến 12GB được trang bị trên Samsung S23 Ultra hứa hẹn mang đến sự đột phá cho thiết bị này và giúp xử lý dễ dàng mọi thao tác dù là ứng dụng chuyên nghiệp đòi hỏi dung lượng Ram lớn.

Dung lượng bộ nhớ tiêu chuẩn của dòng máy này là 256GB hứa hẹn mang đến khả năng lưu trữ tuyệt vời giúp người dùng như được trang bị thêm thư viện mini có thể mang theo mọi lúc mọi nơi mà không lo lắng về khả năng tràn bộ nhớ.

Máy ảnh 200MP đột phá công nghệ nhiếp ảnh
Siêu smartphone có khả năng nhiếp ảnh đỉnh cao vẫn đang là xu hướng và dự kiến sẽ còn là xu hướng trong tương lai bởi vậy các dòng smartphone ra mắt trên thị trường ngày càng quan tâm đến khả năng nhiếp ảnh. Samsung là cái tên đầu ngành với sự quan tâm đặc biệt đến lĩnh vực này, những chiếc flagship của công ty công nghệ hàng đầu Hàn Quốc này càng ngày càng có khả năng nhiếp ảnh vượt trội.

Samsung Galaxy S23 Ultra dường như bứt phá mọi giới hạn nhiếp ảnh điện thoại khi trang bị cho mình ống kính chính có độ phân giải lên đến 200 MP. Khả năng chụp đêm vượt trội cùng những khung hình góc siêu rộng vẫn được phát triển ở chiếc điện thoại này giúp người dùng có được những bức ảnh cực nghệ và có tính đương đại.
Dàn camera phụ cực chất có thêm các tính năng cảm biến độ rộng, cảm biến chiều sâu giúp bức ảnh chân thực và mang tính thời đại hơn. Camera selfie 12MP mang đến khả năng tự chụp ấn tượng và giúp bạn có những bức ảnh đẹp siêu thực mà không cần trải qua bộ lọc hay ứng dụng chỉnh sửa ảnh.

Đặc biệt, chế độ Expert RAW trên S23 Ultra hỗ trợ chụp ảnh với chi tiết cao. Với Expert RAW người dùng có thể sử dụng để chụp chồng hình, chụp thiên văn (chụp bầu trời, chùm sao) với độ sắc nét cao. Tính năng chụp chồng hình với khả năng chụp 9 tấm hình liên tiếp và kết hợp lại mang lại kết quả là những bức hình đậm chất nghệ thuật.

Màn hình 6.8 inch – độ cong giảm nâng cao trải nghiệm hình ảnh
Galaxy S23 Ultra vẫn được trang bị một màn hình lớn với kích thước lên đến 6.8 inch. Tuy nhiên, thay vì làm cong như thế hệ S22 Ultra thì độ con màn hình trên S23 Ultra sẽ giảm bớt. Nhờ thiết kế này diện tích bề mặt phẳng trên màn hình sẽ gia tăng giúp mang lại những trải nghiệm hình ảnh tốt nhất cho người dùng.

Màn hình Samsung S23 Ultra với công nghệ Dynamic AMOLED 2X mang lại khả năng tái hiện lại các chi tiết một cách hoàn hảo kể cả khi người dùng điều chỉnh độ sáng ở mức cao hoặc thấp nhất. Màn hình với tần số quét 120Hz tối ưu trong các chuyển động cuộn mang lại trải nghiệm giải trí tuyệt vời.

## Pin 5000 mAh – cung cấp năng lượng bền bỉ
Điện thoại Samsung S23 Ultra sẽ được trang bị viên pin dung lượng lớn 5000 mAh. Mức dung lượng này tuy không có sự khác biệt so với người tiền nhiệm S22 Ultra nhưng với nhiều tùy biến về phần mềm cùng con chip mới giúp mang lại thời gian sử dụng bền bỉ.

Không chỉ với dung lượng lớn, điện thoại Samsung S23 Ultra còn được trang bị chế độ siêu tiết kiệm pin giúp kéo dài thời gian sử dụng, đặc biệt ở những trường hợp khẩn cấp.

Samsung Galaxy S23 Ultra - chiếc điện thoại bền bỉ 
Khả năng kháng nước và chống va đập vẫn được trang bị trên S23 Series. Theo đó dòng điện thoại này và Spen đi cùng đều có khả năng kháng nước đạt chuẩn IP68. Combo giáp Armor Aluminum bao quanh khung viền máy kết hợp kính cường lực Corning® Gorilla® Glass Victus®+ hứa hẹn khiến Samsung Galaxy S23 Ultra trở thành 1 tanker với khả năng chống chịu cực bền bỉ.

Bên cạnh đó, SPen vẫn là công cụ phụ trợ không thể thiếu. Với độ nhạy bén và chính xác gần như tuyệt đối, SPen mang đến cho người dùng những quyền năng tối thượng để có thể tối ưu hóa mọi thao tác trên chiếc điện thoại này.

## Samsung Galaxy S23 Ultra - chiếc điện thoại bền bỉ  

Khả năng điều khiển từ xa và đồng bộ hóa mọi dữ liệu đã tạo nên 1 hệ sinh thái Samsung cực đẳng cấp và xuyên suốt giúp bạn có thể làm việc, tiếp nhận thông tin từ mọi nơi chỉ cần có thiết bị điện tử samsung bên cạnh.', 98);
INSERT INTO Product (ID, CategoryID, DateCreated, DateModified, InventoryCount, ManufacturerID, Specification, Name, Price, ShowInPage, Unit, WarrantyMonth, Article, Views) VALUES (2, 1, 1686071403993, 1687535561480, 299, 3, '[**Click here**](https://www.gsmarena.com/apple_iphone_14_pro_max-11773.php) for full specification for this product.

| Product Specification | |
| - | - |
| Size | 160.7 x 77.6 x 7.9 mm |
| Weight | 240 gam |
| Color | Black, purple, yellow, green |
| Display | OLED Retina 6.7 inch<br>Thiết kế viên thuốc<br>Dynamic Island<br>Always-on display |
| Display Resolution | 2796 x 1290 pixel |
| Display DPI | 460 PPI	|
| Max  | 2000 nits |
| Display Rate | Từ 1Hz đến 120Hz |
| CPU | A16 Bionic |
| RAM | 6GB |
| Storage | 128GB, 256GB, 512GB, 1TB |
| Battery | 4352 mAh |
| Charge | 20W (normal charge)<br>15W (Magsafe)<br>7,5W (Qi Wireless) |
| Camera | 48MP + 12MP + 12MP |
| 5G Support | Yes |
', 'iPhone 14 Pro Max', 1168.0, 1, 'item', 12, 'iPhone 14 Pro Max sở hữu thiết kế màn hình Dynamic Island ấn tượng cùng màn hình OLED 6,7 inch hỗ trợ always-on display và hiệu năng vượt trội với chip A16 Bionic. Bên cạnh đó máy còn sở hữu nhiều nâng cấp về camera với cụm camera sau 48MP, camera trước 12MP dùng bộ nhớ RAM 6G đa nhiệm vượt trội.  

## Đánh giá iPhone 14 Pro Max - siêu phẩm cao cấp nhất của Apple hiện tại 
Những dòng iPhone đến từ nhà Apple đều có sức hút đặc biệt ngay từ thời điểm ra mắt và thế hệ iPhone 14 Pro Max cũng không ngoại lệ. Có thể nói, iPhone 14 Pro Max là sự kết hợp hoàn hảo giữa các yếu tố về thiết kế, cấu hình, tính năng, hệ điều hành,... Nếu bạn tò mò về siêu phẩm này, hãy đọc ngay phần đánh giá chi tiết bên dưới nhé. 

### Màn hình viên thuốc độc đáo
Ở dòng iPhone 14 Pro Max 128GB, Apple đã thay thế thiết kế tai thỏ truyền thống trước đó bằng thiết kế hình viên thuốc mới mẻ. Ưu điểm của thiết kế hình viên thuốc chính là không chiếm quá nhiều diện tích màn hình nhằm tối ưu hóa không gian hiển thị nội dung của smartphone.

Ngoài ra, siêu phẩm mới của nhà Táo cũng sở hữu kích thước màn hình đạt 6.7 inch với độ phân giải 2796 x 1290 pixel, tấm nền OLED cùng công nghệ Retina độc quyền. Nhờ vào đó, người dùng sẽ có được những trải nghiệm giải trí và làm việc hoàn hảo với hình ảnh sắc nét và rực rỡ. 

### Đánh giá màn hình iPhone 14 Pro Max

Nhằm giúp người dùng có thể sử dụng iPhone ngay dưới ánh nắng trực tiếp, Apple đã nâng cấp độ sáng tối đa đến 2000 nits ở siêu phẩm mới nhất. Hơn nữa, màn hình của máy cũng có khả năng tự động điều chỉnh ánh sáng phù hợp với môi trường giúp bảo vệ thị lực, cũng như tiết kiệm pin. 

Cũng giống với người tiền nhiệm, thế hệ iPhone 14 Pro Max cũng được trang bị tần số quét đạt 120Hz nhằm tạo nên trải nghiệm chạm vuốt mượt mà cho người dùng. Đặc biệt hơn, tần số quét trên máy có thể tự động thay đổi dao động từ 1Hz đến 120Hz giúp hạn chế tiêu thụ dung lượng pin trên máy. 

### Con chip A16 Bionic tiến trình 4nm mạnh mẽ
Phiên bản iPhone 14 Pro Max 128GB là lần đầu tiên Apple sử dụng con chip được xây dựng trên tiến trình 4nm - A16 Bionic. Con chip này có tổng cộng 6 nhân với 2 nhân hiệu suất cao kết hợp với 4 nhân tiết kiệm điện.

Theo như đánh giá thực tế, hiệu suất xử lý của chip A16 Bionic nhanh hơn đến 10% so với phiên bản A15 trước đó. Ngoài ra, con chip này cũng đi kèm với GPU 5 nhân với băng thông bộ nhớ tăng thêm đến 50% so với dòng A15 giúp smartphone có thể xử lý những tựa game có đồ họa cao. 

Cấu hình ấn tượng, hiệu năng khủng
Bên cạnh trang bị con chip A16 Bionic mạnh mẽ, hiệu suất vượt trội của iPhone 14 Pro Max còn được bổ sung bởi dung lượng RAM 6GB. Nhờ vào đó, thiết bị có thể xử lý đa tác vụ cùng lúc và không gặp phải tình trạng giật, lag. Ngoài ra, chiếc smartphone cũng sở hữu bộ nhớ trong tối đa lên đến 1TB giúp người dùng lưu trữ mọi dữ liệu và ứng dụng yêu thích.

### Đánh giá hiệu năng iPhone 14 Pro Max

Điểm nhấn ở cấu hình của iPhone 14 Pro Max cũng nằm ở khả năng kết nối 5G với tốc độ vượt trội đạt đến 10 Gigabit/ giây. Nhờ vào đó, công nghệ 5G trên chiếc smartphone có thể hỗ trợ người dùng luôn giữ được kết nối mạng khi đang ở bất kỳ quốc gia nào trên thế giới và không gặp gián đoạn. 

Hệ điều hành IOS 16 cho khả năng cá nhân hoá cao
Dòng iPhone 14 Pro Max chạy trên hệ điều hành iOS 16 được giới thiệu lần đầu tiên trong sự kiện công nghệ WWDC 2022. Điểm nổi bật được nhiều người dùng ưa chuộng ở phiên bản iOS 16 nằm ở tính cá nhân hóa cao. Bạn có thể tự do thay đổi màn hình khóa với các tiện ích, hình ảnh, font chữ,... theo ý thích.

### Đánh giá hệ điều hành iPhone 14 Pro Max

Hệ điều hành iOS 16 còn nổi bật với tính năng Live Activities hỗ trợ người dùng cập nhật các dữ liệu quan trọng như tiến độ tập luyện, đơn giao hàng,... Ngoài ra, iOS 16 còn bổ sung thêm tính năng thư viện ảnh chung trên iCloud giúp bạn dễ dàng chia sẻ những bức ảnh chụp với bạn bè, người thân, hay đồng nghiệp của mình.

## Camera chính 48MP sắc nét, zoom quang học 6X
Nhắc đến những chiếc điện thoại iPhone thì không thể không kể đến khả năng chụp ảnh và quay video. Ở thế hệ flagship iPhone 14 Pro Max 128GB mới nhất, Apple đã nâng cấp độ phân giải ở cảm biến chính của smartphone từ 12MP lên đến 48MP. Ống kính 48MP này sẽ hỗ trợ người dùng chụp những khung cảnh ở xa vô cùng sắc nét với khả năng zoom quang học đến 6x.

Hệ thống ống kính của iPhone 14 Pro Max còn được bổ sung một camera góc rộng 12MP và một camera TrueDepth 12MP. Hai ống kính có khả năng lấy nét tự động nhanh chóng, cũng như chụp ảnh trong điều kiện thiếu sáng rất tốt. Cùng với sự hỗ trợ của con chip A16 Bionic, những bức hình được chụp trên dòng máy này trở nên rực rỡ và sinh động hơn. 

Bên cạnh đó, camera chính trên iPhone 14 Pro Max còn được trang bị thêm tính năng ProRAW có khả năng chụp những bức ảnh gốc vô cùng tự nhiên. Ngoài ra, Action Mode ở cụm camera trên máy cũng hỗ trợ chống rung và chống nhiễu khung hình khi quay chụp. 

Với những cải tiến vượt bậc như trên, hệ thống camera trên dòng iPhone thế hệ mới có khả năng quay video với chất lượng 4K cùng với tốc độ 30 khung hình/ giây. Ngoài ra, ống kính của smartphone còn có thêm một số tính năng khác như chế độ HDR, chụp ảnh ban đêm, chụp góc rộng, chụp cận cảnh,...

Về camera selfie, thế hệ iPhone 14 2022 mới nhất của nhà Táo có ống kính với độ phân giải đến 12MP cùng nhiều tính năng như tự động lấy nét, chụp phong cảnh, chụp chân dung,... Nhờ vào đó, người dùng có thể ghi lại những khoảnh khắc đáng nhớ trong cuộc sống hàng ngày vô cùng chân thực và sắc nét. 

## Dung lượng pin vượt trội 4352mAh
Nhờ có dung lượng pin vượt trội 4352mAh và khả năng tiết kiệm năng lượng của chip A16 Bionic, chiếc iPhone 14 Pro Max có thời gian hoạt động dài hơn so với người tiền nhiệm. Cụ thể, thời gian tiêu thụ pin của smartphone đối với từng tác vụ như sau:

** Thời lượng sử dụng thực tế sẽ thay đổi tùy thuộc cài đặt đặt thiết bị cũng như môi trường xung quanh.

Sản phẩm vẫn được trang bị công suất sạc 20W giống với người tiền nhiệm. Tuy nhiên, bạn có thể nạp đầy đến 50% dung lượng pin cho điện thoại chỉ trong vòng 30 phút. Ngoài ra, thiết bị cũng hỗ trợ người dùng sạc không dây Magsafe với 15W và sạc không dây Qi với 7,5W. 

Tính năng màn hình Dynamic Island
Điều làm nên khác biệt ở dòng iPhone 14 Pro Max nằm ở tính năng màn hình Dynamic Island độc đáo. Thông qua tính năng này, bạn có thể theo dõi các thông báo trên điện thoại, điều hướng bản đồ, tùy chỉnh những bản nhạc,... vô cùng tiện lợi. 

Ngoài ra, siêu phẩm mới của nhà Táo khuyết còn được trang bị thêm tính năng màn hình luôn bật (Always-On display). Theo đó, người dùng có thể xem được đồng hồ, các thông báo trên điện thoại,... nhanh chóng và không cần phải khởi động màn hình như trước.

## Tính năng iPhone 14 Pro Max

Gọi điện qua vệ tinh (Emergency SOS) cũng là một tính năng hữu ích được bổ sung ở thế hệ iPhone 14 Pro Max. Khi không thể kết nối với Wifi và dữ liệu di động, chiếc iPhone của bạn sẽ tự động kết nối với hệ thống vệ tinh và thông báo tình trạng khẩn cấp của bạn đến nơi cứu hộ gần nhất.

Thêm vào đó là tính năng Phát hiện va chạm tự động (Crash Detection). Tính năng này sử dụng con quay hồi chuyển và gia tốc kế được trang bị trên iPhone để xác định những va chạm xe ô tô đang xảy ra. Nếu có va chạm xảy ra, chiếc smartphone sẽ tự động gửi thông báo đến trạm cấp cứu, cũng như số điện thoại liên hệ bạn đã thiết lập trước đó. 

## iPhone 14 Pro Max có mấy màu?
Trong lần ra mắt mới nhất, dòng iPhone 14 Pro Max của nhà Táo vẫn thu hút nhiều người dùng bởi thiết kế vô cùng sang trọng và thời thượng. Chiếc smartphone sẽ giúp bạn thể hiện cá tính riêng của mình thông qua 4 gam màu bao gồm: tím đậm, vàng gold, đen và bạc.

Màu tím đậm: Chiếc iPhone 14 Pro Max mang màu tím đậm thể hiện phong cách lịch sự và nhã nhặn. Đây là sự lựa chọn hoàn hảo cho những ai theo đuổi phong cách cá tính, trẻ trung và năng động;

Màu vàng Gold: Phiên bản iPhone màu vàng Gold mang đến nét đẹp vô cùng sang trọng và thanh lịch cho người sử dụng. Đặc biệt hơn, màu vàng Gold có ưu điểm nổi bật là các vết trầy gây mất thẩm mỹ trên máy rất ít bị phát hiện so hơn so với các gam màu khác;

Màu đen: Tông màu đen là gam màu cơ bản ở tất cả các phiên bản iPhone của nhà Apple. Màu đen sở hữu nét đẹp huyền bí và không bao giờ bị lỗi thời;

Màu bạc: Những chiếc iPhone màu bạc sở hữu vẻ ngoài khá độc đáo và tôn lên nét đẹp sang trọng và đẳng cấp cho người sử dụng. Gam màu bạc phù hợp với nhiều đối tượng khách hàng từ những người trẻ đến cao tuổi.

## iPhone 14 Pro Max có bao nhiêu phiên bản dung lượng bộ nhớ?
Ở dòng flagship thế hệ mới, thương hiệu Apple đã có những bước cải tiến vượt bậc về dung lượng bộ nhớ của sản phẩm. Cụ thể hơn, dòng iPhone 14 Pro Max được mở bán với 4 tùy chọn bộ nhớ trong bao gồm: 128GB, 256GB, 512GB và 1TB.

## iPhone 14 Pro Max có bao nhiêu phiên bản dung lượng bộ nhớ
Có thể thấy, dung lượng bộ nhớ tối đa của chiếc flagship này đạt đến 1TB có thể đáp ứng nhu cầu lưu trữ dữ liệu khổng lồ của mọi người tiêu dùng. Giờ đây, với chiếc iPhone này bạn có thể lưu lại mọi thông tin và cài đặt các ứng dụng yêu thích và mang theo đến bất kỳ đâu. 

## iPhone 14 Pro Max hỗ trợ bao nhiêu sim?
Theo như nguồn tin chính thức từ phía nhà sản xuất Apple, các mẫu iPhone 14 Pro Max 128GB chính hãng ở Việt Nam vẫn được trang bị một khe cắm sim vật lý. Chiếc điện thoại sẽ bao gồm 1 eSim và 1 sim vật lý truyền thống. Nhờ vào đó, người dùng có thể sử dụng cùng lúc 2 số điện thoại trên chiếc smartphone. 

## iPhone 14 Pro Max được bán với giá bao nhiêu
Khi nào iPhone 14 Pro Max được mở bán?
iPhone 14 Pro Max sẽ được giới thiệu chung với những sản phẩm khác nằm trong bộ sưu tập iPhone 14. Theo đó, chiếc flagship được giới thiệu với thị trường công nghệ trên toàn thế giới tại sự kiện Far Out của Apple được tổ chức vào ngày 8 tháng 9 năm 2022. 

## Khi nào iPhone 14 Pro Max được mở bán
Tuy nhiên, phiên bản iPhone 14 Pro Max chỉ được mở bán chính thức tại Việt Nam vào ngày 14 tháng 10 năm 2022. Nếu muốn là người sở hữu siêu phẩm iPhone sớm nhất, bạn có thể đặt trước từ ngày 7 đến 13 tháng 10. ', 0);
INSERT INTO Product (ID, CategoryID, DateCreated, DateModified, InventoryCount, ManufacturerID, Specification, Name, Price, ShowInPage, Unit, WarrantyMonth, Article, Views) VALUES (3, 1, 1686071425880, 1686922682964, 300, 3, NULL, 'iPhone 14 Pro', 1083.3, 1, 'item', 12, NULL, 0);
INSERT INTO Product (ID, CategoryID, DateCreated, DateModified, InventoryCount, ManufacturerID, Specification, Name, Price, ShowInPage, Unit, WarrantyMonth, Article, Views) VALUES (4, 1, 1686071450069, 1686922699488, 301, 2, NULL, 'Xiaomi 13 5G', 807.05, 1, 'item', 12, NULL, 0);
INSERT INTO Product (ID, CategoryID, DateCreated, DateModified, InventoryCount, ManufacturerID, Specification, Name, Price, ShowInPage, Unit, WarrantyMonth, Article, Views) VALUES (5, 1, 1686071495193, 1686922712083, 297, 2, NULL, 'Xiaomi 12T 5G 256GB', 488.31, 1, 'item', 12, NULL, 0);
INSERT INTO Product (ID, CategoryID, DateCreated, DateModified, InventoryCount, ManufacturerID, Specification, Name, Price, ShowInPage, Unit, WarrantyMonth, Article, Views) VALUES (6, 1, 1687518057767, 1687518057767, 300, 1, NULL, 'Galaxy S23 Ultra 5G', 1147.0, 1, 'item', 24, NULL, 3);
INSERT INTO Product (ID, CategoryID, DateCreated, DateModified, InventoryCount, ManufacturerID, Specification, Name, Price, ShowInPage, Unit, WarrantyMonth, Article, Views) VALUES (7, 1, 1687518404147, 1687518404147, 300, 4, NULL, 'realme C55', 204.0, 1, 'item', 12, NULL, 3);
INSERT INTO Product (ID, CategoryID, DateCreated, DateModified, InventoryCount, ManufacturerID, Specification, Name, Price, ShowInPage, Unit, WarrantyMonth, Article, Views) VALUES (8, 1, 1687518776751, 1687518776751, 300, 5, NULL, 'OPPO A55', 204.0, 1, 'item', 12, NULL, 0);

-- Table: ProductCategory
CREATE TABLE IF NOT EXISTS ProductCategory (
    ID         INTEGER NOT NULL
                       CONSTRAINT PK_ProductCategory PRIMARY KEY AUTOINCREMENT,
    Name       TEXT    NOT NULL,
    ShowInPage INTEGER NOT NULL
);

INSERT INTO ProductCategory (ID, Name, ShowInPage) VALUES (1, 'Phone', 1);
INSERT INTO ProductCategory (ID, Name, ShowInPage) VALUES (2, 'Headphone', 1);
INSERT INTO ProductCategory (ID, Name, ShowInPage) VALUES (3, 'Tablet', 1);

-- Table: ProductComment
CREATE TABLE IF NOT EXISTS ProductComment (
    ID             INTEGER NOT NULL
                           CONSTRAINT PK_ProductComment PRIMARY KEY AUTOINCREMENT,
    UserID         INTEGER NOT NULL,
    ProductID      INTEGER NOT NULL,
    Comment        TEXT    NOT NULL,
    Rating         REAL    NOT NULL,
    Disabled       INTEGER NOT NULL,
    DisabledReason TEXT,
    CONSTRAINT FK_ProductComment_Product_ProductID FOREIGN KEY (
        ProductID
    )
    REFERENCES Product (ID) ON DELETE CASCADE,
    CONSTRAINT FK_ProductComment_User_UserID FOREIGN KEY (
        UserID
    )
    REFERENCES User (ID) ON DELETE CASCADE
);

INSERT INTO ProductComment (ID, UserID, ProductID, Comment, Rating, Disabled, DisabledReason) VALUES (2, 3, 1, 'hello world!', 5.0, 0, NULL);

-- Table: ProductImageMetadata
CREATE TABLE IF NOT EXISTS ProductImageMetadata (
    ID           INTEGER NOT NULL
                         CONSTRAINT PK_ProductImageMetadata PRIMARY KEY AUTOINCREMENT,
    Name         TEXT    NOT NULL,
    Description  TEXT,
    FilePath     TEXT    NOT NULL,
    ProductID    INTEGER NOT NULL,
    DateCreated  INTEGER NOT NULL,
    DateModified INTEGER NOT NULL,
    CONSTRAINT FK_ProductImageMetadata_Product_ProductID FOREIGN KEY (
        ProductID
    )
    REFERENCES Product (ID) ON DELETE CASCADE
);

INSERT INTO ProductImageMetadata (ID, Name, Description, FilePath, ProductID, DateCreated, DateModified) VALUES (1, 'c9gr5k589tpk1hdvomnib1sozevvdd9ek5xoib3aexrpegsnxlgbvr34tkx18syw.png', NULL, 'c9gr5k589tpk1hdvomnib1sozevvdd9ek5xoib3aexrpegsnxlgbvr34tkx18syw.png', 1, 1686852241493, 1686852241493);
INSERT INTO ProductImageMetadata (ID, Name, Description, FilePath, ProductID, DateCreated, DateModified) VALUES (3, 'vtq873nm8czglzjbhbvstofgdi40sp58fpz7sr6bpakxwt03dpynxwyj3nho8iqm.png', NULL, 'vtq873nm8czglzjbhbvstofgdi40sp58fpz7sr6bpakxwt03dpynxwyj3nho8iqm.png', 4, 1686854947518, 1686854947518);
INSERT INTO ProductImageMetadata (ID, Name, Description, FilePath, ProductID, DateCreated, DateModified) VALUES (5, '5kpf3h1sw2kjp14vcca5zan578sjbjmtz2weh2k9iwsgbr486pd17hc111qb2pnq.png', NULL, '5kpf3h1sw2kjp14vcca5zan578sjbjmtz2weh2k9iwsgbr486pd17hc111qb2pnq.png', 1, 1687192256105, 1687192256105);
INSERT INTO ProductImageMetadata (ID, Name, Description, FilePath, ProductID, DateCreated, DateModified) VALUES (6, 'c4b7bgpomk29inygp4o4562wzn8avvfkakz7amvww6ru61tgmdd6pahozqeduh4z.jpg', NULL, 'c4b7bgpomk29inygp4o4562wzn8avvfkakz7amvww6ru61tgmdd6pahozqeduh4z.jpg', 2, 1687192881252, 1687192881252);
INSERT INTO ProductImageMetadata (ID, Name, Description, FilePath, ProductID, DateCreated, DateModified) VALUES (7, '5ilih4ehhcpu42k4juaj6wdg5cuots5rs33jge0452ctfam82c8dppeofew5i328.jpg', NULL, '5ilih4ehhcpu42k4juaj6wdg5cuots5rs33jge0452ctfam82c8dppeofew5i328.jpg', 3, 1687192889429, 1687192889429);
INSERT INTO ProductImageMetadata (ID, Name, Description, FilePath, ProductID, DateCreated, DateModified) VALUES (8, '3r1y4rxne72fovs3a4n2poltv4af6vm5d4rg1ht0j3gqtvl3yhwjwy6wmhg81ker.jpg', NULL, '3r1y4rxne72fovs3a4n2poltv4af6vm5d4rg1ht0j3gqtvl3yhwjwy6wmhg81ker.jpg', 5, 1687192925856, 1687192925856);
INSERT INTO ProductImageMetadata (ID, Name, Description, FilePath, ProductID, DateCreated, DateModified) VALUES (9, 'v64t4402w2l836f0rm7r58aos1avart863sqhoj0kftrnhrq8vho94cww31i9cku.jpg', NULL, 'v64t4402w2l836f0rm7r58aos1avart863sqhoj0kftrnhrq8vho94cww31i9cku.jpg', 6, 1687518092959, 1687518092959);
INSERT INTO ProductImageMetadata (ID, Name, Description, FilePath, ProductID, DateCreated, DateModified) VALUES (10, 'cbga94r1011laki6klecd3t7jirn2lpclj1flltra2o3vb6mdcjidlae1dvmxptr.jpg', NULL, 'cbga94r1011laki6klecd3t7jirn2lpclj1flltra2o3vb6mdcjidlae1dvmxptr.jpg', 7, 1687518411490, 1687518411490);
INSERT INTO ProductImageMetadata (ID, Name, Description, FilePath, ProductID, DateCreated, DateModified) VALUES (11, 'o7dant131p4o470j8r25kp5jel477dtmz0oltgcz8vt13xn83pwj1i8vvduylwx5.jpg', NULL, 'o7dant131p4o470j8r25kp5jel477dtmz0oltgcz8vt13xn83pwj1i8vvduylwx5.jpg', 8, 1687518804201, 1687518804201);

-- Table: ProductManufacturer
CREATE TABLE IF NOT EXISTS ProductManufacturer (
    ID         INTEGER NOT NULL
                       CONSTRAINT PK_ProductManufacturer PRIMARY KEY AUTOINCREMENT,
    Name       TEXT    NOT NULL,
    ShowInPage INTEGER NOT NULL
);

INSERT INTO ProductManufacturer (ID, Name, ShowInPage) VALUES (1, 'Samsung', 1);
INSERT INTO ProductManufacturer (ID, Name, ShowInPage) VALUES (2, 'Xiaomi', 1);
INSERT INTO ProductManufacturer (ID, Name, ShowInPage) VALUES (3, 'Apple', 1);
INSERT INTO ProductManufacturer (ID, Name, ShowInPage) VALUES (4, 'Realme', 1);
INSERT INTO ProductManufacturer (ID, Name, ShowInPage) VALUES (5, 'OPPO', 1);

-- Table: User
CREATE TABLE IF NOT EXISTS User (
    ID             INTEGER NOT NULL
                           CONSTRAINT PK_User PRIMARY KEY AUTOINCREMENT,
    Username       TEXT    NOT NULL,
    Password       TEXT    NOT NULL,
    PasswordHash   TEXT    NOT NULL,
    Name           TEXT    NOT NULL,
    Email          TEXT,
    Phone          TEXT,
    DateCreated    INTEGER NOT NULL,
    DateModified   INTEGER NOT NULL,
    IsEnabled      INTEGER NOT NULL,
    DisabledReason TEXT,
    UserType       INTEGER NOT NULL
);

INSERT INTO User (ID, Username, Password, PasswordHash, Name, Email, Phone, DateCreated, DateModified, IsEnabled, DisabledReason, UserType) VALUES (1, 'admin', '27eb576aa831aff422249d0f4141a0da1f49264242ea26e8916e55d9672dcd4c', 'p0rygfu06ht9', 'Administrator', NULL, NULL, 1686071053457, 1686071053457, 1, NULL, 2);
INSERT INTO User (ID, Username, Password, PasswordHash, Name, Email, Phone, DateCreated, DateModified, IsEnabled, DisabledReason, UserType) VALUES (2, 'zoemeow', '59f8ad3467a5c92cedc538c543eba31dd3a6e5c8655b8601b4c183af6399b94d', '93apuymq8in9', 'ZoeMeow', 'zoemeow1027@gmail.com', '0987654321', 1686071191969, 1686071191969, 1, NULL, 2);
INSERT INTO User (ID, Username, Password, PasswordHash, Name, Email, Phone, DateCreated, DateModified, IsEnabled, DisabledReason, UserType) VALUES (3, 'zoemeow2', 'f52357d9011ad3fcd45b454922271aec3e1a0edf4ca9bf91f1f3f341f69a2e44', '6psmud7syak6', 'ZoeMeow (User)', 'zoemeow1027@gmail.com', '0987654321', 1686071220985, 1686071220985, 1, NULL, 0);
INSERT INTO User (ID, Username, Password, PasswordHash, Name, Email, Phone, DateCreated, DateModified, IsEnabled, DisabledReason, UserType) VALUES (4, 'user3', '998e3ec10e9860bf25696660dc623365b155e5209f11844f6a7f5855696ffc31', 'kp2pzlrqvn0q', 'User 3', 'user3@gmail.com', '0987654321', 1687845650759, 1687845650759, 1, NULL, 0);

-- Table: UserAddress
CREATE TABLE IF NOT EXISTS UserAddress (
    ID          INTEGER NOT NULL
                        CONSTRAINT PK_UserAddress PRIMARY KEY AUTOINCREMENT,
    UserID      INTEGER NOT NULL,
    Name        TEXT    NOT NULL,
    Address     TEXT    NOT NULL,
    Phone       TEXT    NOT NULL,
    CountryCode TEXT    NOT NULL
                        DEFAULT '',
    CONSTRAINT FK_UserAddress_User_UserID FOREIGN KEY (
        UserID
    )
    REFERENCES User (ID) ON DELETE CASCADE
);

INSERT INTO UserAddress (ID, UserID, Name, Address, Phone, CountryCode) VALUES (2, 3, 'Quế Tùng', 'K120/12 đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, thành phố Đà Nẵng', '0987654321', 'VN');
INSERT INTO UserAddress (ID, UserID, Name, Address, Phone, CountryCode) VALUES (3, 3, 'Quế Tùng', '247 đường Phan Bội Châu, phường Hòa Hiếu, tx. Thái Hòa, Nghệ An', '0987654321', 'VN');
INSERT INTO UserAddress (ID, UserID, Name, Address, Phone, CountryCode) VALUES (5, 3, 'Quế Tùng', '0987654321234567890', '0987654321', 'VN');

-- Table: UserCart
CREATE TABLE IF NOT EXISTS UserCart (
    ID        INTEGER NOT NULL
                      CONSTRAINT PK_UserCart PRIMARY KEY AUTOINCREMENT,
    UserID    INTEGER NOT NULL,
    ProductID INTEGER NOT NULL,
    Count     INTEGER NOT NULL,
    CONSTRAINT FK_UserCart_Product_ProductID FOREIGN KEY (
        ProductID
    )
    REFERENCES Product (ID) ON DELETE CASCADE,
    CONSTRAINT FK_UserCart_User_UserID FOREIGN KEY (
        UserID
    )
    REFERENCES User (ID) ON DELETE CASCADE
);


-- Table: UserSession
CREATE TABLE IF NOT EXISTS UserSession (
    ID          INTEGER NOT NULL
                        CONSTRAINT PK_UserSession PRIMARY KEY AUTOINCREMENT,
    UserID      INTEGER NOT NULL,
    Token       TEXT    NOT NULL,
    DateCreated INTEGER NOT NULL,
    DateExpired INTEGER NOT NULL,
    CONSTRAINT FK_UserSession_User_UserID FOREIGN KEY (
        UserID
    )
    REFERENCES User (ID) ON DELETE CASCADE
);

INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (1, 1, 'c28b074e77049da4ca966d8f4e76cb8385354ddf5a3556e764ba02d24ab7fa7d', 1686071169354, 1717607169355);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (2, 3, 'f32586542283b1e0fba5df9cd1a417368e1bd0893812918b5caca2bfad2963ee', 1686071513626, 1717607513626);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (3, 3, 'b20b050b19bde401e91969ae1c3676ebc4e374856140d9fb9d3f37e6f15a6dfd', 1686083776227, 1717619776227);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (4, 3, '44cc9c27b7528a2e01d59840ba883df541d132c040c49b38c18de60c61a321fe', 1686085440992, 1717621440992);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (5, 2, '8208b5512452be5731829db14f81530451a3b0e9431f5b324ef861de44ef7d37', 1686085909742, 1717621909742);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (6, 3, '17b2f5e00a7c86da63b2350dbddc924e4c185008dce15e5a492ad13a691555eb', 1686087368065, 1717623368065);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (7, 2, '8c66b232b0653c5030d7d606957c9ce7611a99183f8afbcee03e8ff6187f9a87', 1686087683030, 1717623683030);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (8, 3, '40cce5a1e369f7247f0d2b0c439cf77babfac3c6692626a0050c53b6ff731872', 1686087697307, 1717623697307);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (9, 2, 'fb7abbaa5829aa7595bb05f04f86de117f6038b4de4c401283084df15ea1ec26', 1686091389676, 1717627389676);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (10, 3, '573d53a189b005996f11b79c3423491677b1bb56e55c41625b2ef415d71ddede', 1686091405269, 1717627405269);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (11, 2, 'cfa586731d6eca12d8a35d31c7a828a7cfdce1f7aacbeb98a7b49704d29bd20f', 1686149806048, 1717685806048);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (12, 3, '871211aff734a8248bb534cb8cedee0b134fb2d59d64435e4a41021e4d1a2f35', 1686153744983, 1717689744983);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (13, 2, '96391eac06f997b0aa36cb2a7bcec1501a5ba9c8d7a7de8c41c344a7042150a5', 1686419632624, 1717955632624);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (15, 3, '65fccb45ac8b0ed04a55a27f4e9bd54500a2b9db5e9842ec055703849768a233', 1686590068653, 1718126068653);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (16, 3, '80d00c0b5039eb86d91ac120a870feb7aa944814df757929aedf8317575d94ae', 1686725714121, 1718261714121);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (17, 2, '80a73bc9883956acac255de1893b9272f31589967b6654dca122c0272bc64d04', 1686769608036, 1718305608036);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (18, 2, 'ce73b72cd39543870560e5e0dc2d7dbd534b2b28502dbe71e1e762ea43e39179', 1686852206360, 1718388206360);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (20, 3, '769fc9df57ac31a360a921a4fa514cde3accb06f133661dab4eaffbf55137bd3', 1686915502728, 1718451502728);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (21, 2, '67b48a876a7d67ac38084580cc3c6b001f7bcc92a9f5ae0e14a12345db7b7ebf', 1687123026206, 1718659026206);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (22, 3, '5506a26f7b5fbda13eeb3053b698876ab6b8dd975600967968354d99b396c0e0', 1687164019106, 1718700019106);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (26, 3, '5a8d1de765d67f2315a80ef6c36512c9cc29650b81c2ae9ece7a2c10bb7a0f72', 1687195180417, 1718731180417);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (27, 3, '986f1f85f2897517cde9d2800a77160385e75b8e1dbec790076592c92df0e64a', 1687231268111, 1718767268111);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (29, 3, '3645f5c252029c5b23e0d696853ee4fee9f0df29b035a52b095988a3cea3843a', 1687372958757, 1718908958757);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (32, 2, 'ab963d9bcf6afb08df3828ad9d25a32d7100fba64d209cda63b6c915194b8fd3', 1687597555020, 1719133555020);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (34, 2, '13459f4a3fd4dabd807662a65b4ac33643db029576a555d16dba257aa6487df1', 1687631891049, 1719167891049);
INSERT INTO UserSession (ID, UserID, Token, DateCreated, DateExpired) VALUES (37, 2, '7b59653598f527c38a67b7caa597acb8e64e0b49bb4c3892723a9e61ced6c131', 1687639118413, 1719175118413);

-- Table: Warranty
CREATE TABLE IF NOT EXISTS Warranty (
    ID                     INTEGER NOT NULL
                                   CONSTRAINT PK_Warranty PRIMARY KEY AUTOINCREMENT,
    ProductID              INTEGER NOT NULL,
    UserID                 INTEGER,
    BillID                 INTEGER,
    WarrantyMonth          INTEGER NOT NULL,
    DateStart              INTEGER NOT NULL,
    DateEnd                INTEGER NOT NULL,
    SerialNumberOrIMEI     TEXT    NOT NULL,
    WarrantyDisabled       INTEGER NOT NULL,
    WarrantyDisabledReason TEXT    NOT NULL,
    CONSTRAINT FK_Warranty_BillSummary_BillID FOREIGN KEY (
        BillID
    )
    REFERENCES BillSummary (ID),
    CONSTRAINT FK_Warranty_Product_ProductID FOREIGN KEY (
        ProductID
    )
    REFERENCES Product (ID) ON DELETE CASCADE,
    CONSTRAINT FK_Warranty_User_UserID FOREIGN KEY (
        UserID
    )
    REFERENCES User (ID) 
);


-- Index: IX_BillDetails_BillID
CREATE INDEX IF NOT EXISTS IX_BillDetails_BillID ON BillDetails (
    "BillID"
);


-- Index: IX_BillDetails_ProductID
CREATE INDEX IF NOT EXISTS IX_BillDetails_ProductID ON BillDetails (
    "ProductID"
);


-- Index: IX_BillSummary_UserID
CREATE INDEX IF NOT EXISTS IX_BillSummary_UserID ON BillSummary (
    "UserID"
);


-- Index: IX_Product_CategoryID
CREATE INDEX IF NOT EXISTS IX_Product_CategoryID ON Product (
    "CategoryID"
);


-- Index: IX_Product_ManufacturerID
CREATE INDEX IF NOT EXISTS IX_Product_ManufacturerID ON Product (
    "ManufacturerID"
);


-- Index: IX_ProductComment_ProductID
CREATE INDEX IF NOT EXISTS IX_ProductComment_ProductID ON ProductComment (
    "ProductID"
);


-- Index: IX_ProductComment_UserID
CREATE INDEX IF NOT EXISTS IX_ProductComment_UserID ON ProductComment (
    "UserID"
);


-- Index: IX_ProductImageMetadata_ProductID
CREATE INDEX IF NOT EXISTS IX_ProductImageMetadata_ProductID ON ProductImageMetadata (
    "ProductID"
);


-- Index: IX_User_Username
CREATE UNIQUE INDEX IF NOT EXISTS IX_User_Username ON User (
    "Username"
);


-- Index: IX_UserAddress_UserID
CREATE INDEX IF NOT EXISTS IX_UserAddress_UserID ON UserAddress (
    "UserID"
);


-- Index: IX_UserCart_ProductID
CREATE UNIQUE INDEX IF NOT EXISTS IX_UserCart_ProductID ON UserCart (
    "ProductID"
);


-- Index: IX_UserCart_UserID
CREATE INDEX IF NOT EXISTS IX_UserCart_UserID ON UserCart (
    "UserID"
);


-- Index: IX_UserSession_Token
CREATE UNIQUE INDEX IF NOT EXISTS IX_UserSession_Token ON UserSession (
    "Token"
);


-- Index: IX_UserSession_UserID
CREATE INDEX IF NOT EXISTS IX_UserSession_UserID ON UserSession (
    "UserID"
);


-- Index: IX_Warranty_BillID
CREATE INDEX IF NOT EXISTS IX_Warranty_BillID ON Warranty (
    "BillID"
);


-- Index: IX_Warranty_ProductID
CREATE INDEX IF NOT EXISTS IX_Warranty_ProductID ON Warranty (
    "ProductID"
);


-- Index: IX_Warranty_UserID
CREATE INDEX IF NOT EXISTS IX_Warranty_UserID ON Warranty (
    "UserID"
);


COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
