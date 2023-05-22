SET IDENTITY_INSERT [dbo].[Product] ON 

INSERT INTO Product ([ID], [Name], [CategoryID], [ManufacturerID], [InventoryCount], [WarrantyMonth], [Price], [ShowInPage], [DateCreated], [DateModified], [Unit])
VALUES (1, N'Galaxy S20 Ultra', 1, 1, 5000, 24, 20000000, 1, DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), N'item')
INSERT INTO Product ([ID], [Name], [CategoryID], [ManufacturerID], [InventoryCount], [WarrantyMonth], [Price], [ShowInPage], [DateCreated], [DateModified], [Unit])
VALUES (2, N'Galaxy S23 Ultra', 1, 1, 0, 12, 31990000, 1, DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), N'item')

SET IDENTITY_INSERT [dbo].[Product] OFF
